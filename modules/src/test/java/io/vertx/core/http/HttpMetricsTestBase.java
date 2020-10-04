/*
 * Copyright (c) 2011-2019 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
 * which is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
 */

package io.vertx.core.http;

import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.VertxOptions;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.metrics.MetricsOptions;
import io.vertx.core.net.SocketAddress;
import io.vertx.test.core.AsyncTestBase;
import io.vertx.test.core.TestUtils;
import io.vertx.test.fakemetrics.FakeHttpClientMetrics;
import io.vertx.test.fakemetrics.FakeMetricsBase;
import io.vertx.test.fakemetrics.FakeMetricsFactory;
import io.vertx.test.fakemetrics.HttpClientMetric;
import io.vertx.test.fakemetrics.HttpServerMetric;
import io.vertx.test.fakemetrics.SocketMetric;
import org.junit.Test;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
import org.jboss.eap.additional.testsuite.annotations.EapAdditionalTestsuite;

@EapAdditionalTestsuite({"modules/testcases/jdkAll/master/vertx/src/main/java#4.0.0"})
public abstract class HttpMetricsTestBase extends HttpTestBase {

  private final HttpVersion protocol;

  public HttpMetricsTestBase(HttpVersion protocol) {
    this.protocol = protocol;
  }

  @Override
  protected VertxOptions getOptions() {
    VertxOptions options = super.getOptions();
    options.setMetricsOptions(new MetricsOptions().setEnabled(true).setFactory(new FakeMetricsFactory()));
    return options;
  }

  @Test
  public void testHttpClientLifecycle() throws Exception {

    // The test cannot pass for HTTP/2 upgrade for now
    HttpClientOptions opts = createBaseClientOptions();
    if (opts.getProtocolVersion() == HttpVersion.HTTP_2 &&
      !opts.isSsl() &&
      opts.isHttp2ClearTextUpgrade()) {
      return;
    }

    CountDownLatch requestBeginLatch = new CountDownLatch(1);
    CountDownLatch requestBodyLatch = new CountDownLatch(1);
    CountDownLatch requestEndLatch = new CountDownLatch(1);
    CompletableFuture<Void> beginResponse = new CompletableFuture<>();
    CompletableFuture<Void> endResponse = new CompletableFuture<>();
    server.requestHandler(req -> {
      assertEquals(protocol, req.version());
      requestBeginLatch.countDown();
      req.handler(buff -> {
        requestBodyLatch.countDown();
      });
      req.endHandler(v -> {
        requestEndLatch.countDown();
      });
      Context ctx = vertx.getOrCreateContext();
      beginResponse.thenAccept(v1 -> {
        ctx.runOnContext(v2 -> {
          req.response().setChunked(true).write(TestUtils.randomAlphaString(1024));
        });
      });
      endResponse.thenAccept(v1 -> {
        ctx.runOnContext(v2 -> {
          req.response().end();
        });
      });
    });
    CountDownLatch listenLatch = new CountDownLatch(1);
    server.listen(8080, "localhost", onSuccess(s -> { listenLatch.countDown(); }));
    awaitLatch(listenLatch);
    FakeHttpClientMetrics clientMetrics = FakeMetricsBase.getMetrics(client);
    CountDownLatch responseBeginLatch = new CountDownLatch(1);
    CountDownLatch responseEndLatch = new CountDownLatch(1);
    Future<HttpClientRequest> request = client.request(new RequestOptions()
      .setMethod(HttpMethod.POST)
      .setPort(8080)
      .setHost("localhost")
      .setURI("/somepath")).onComplete(onSuccess(req -> {
      req
        .onComplete(onSuccess(resp -> {
          responseBeginLatch.countDown();
          resp.endHandler(v -> {
            responseEndLatch.countDown();
          });
        }))
        .setChunked(true);
      req.sendHead();
    }));
    awaitLatch(requestBeginLatch);
    HttpClientMetric reqMetric = clientMetrics.getMetric(request.result());
    waitUntil(() -> reqMetric.requestEnded.get() == 0);
    waitUntil(() -> reqMetric.responseBegin.get() == 0);
    request.result().write(TestUtils.randomAlphaString(1024));
    awaitLatch(requestBodyLatch);
    assertEquals(0, reqMetric.requestEnded.get());
    assertEquals(0, reqMetric.responseBegin.get());
    request.result().end();
    awaitLatch(requestEndLatch);
    waitUntil(() -> reqMetric.requestEnded.get() == 1);
    assertEquals(0, reqMetric.responseBegin.get());
    beginResponse.complete(null);
    awaitLatch(responseBeginLatch);
    assertEquals(1, reqMetric.requestEnded.get());
    waitUntil(() -> reqMetric.responseBegin.get() == 1);
    endResponse.complete(null);
    awaitLatch(responseEndLatch);
    waitUntil(() -> clientMetrics.getMetric(request.result()) == null);
    assertEquals(1, reqMetric.requestEnded.get());
    assertEquals(1, reqMetric.responseBegin.get());
  }

  @Test
  public void testClientConnectionClosed() throws Exception {
    server.requestHandler(req -> {
      req.response().setChunked(true).write(Buffer.buffer("some-data"));
    });
    startServer();
    client = vertx.createHttpClient(createBaseClientOptions().setIdleTimeout(2));
    FakeHttpClientMetrics metrics = FakeMetricsBase.getMetrics(client);
    client.request(requestOptions).onComplete(onSuccess(req -> {
      req.onComplete(onSuccess(resp -> {
        HttpClientMetric metric = metrics.getMetric(resp.request());
        assertNotNull(metric);
        assertFalse(metric.failed.get());
        resp.exceptionHandler(err -> {
          assertNull(metrics.getMetric(resp.request()));
          assertTrue(metric.failed.get());
          testComplete();
        });
      })).end();
    }));
    await();
  }
  
}
