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

package io.vertx.test.fakemetrics;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
import org.jboss.eap.additional.testsuite.annotations.EapAdditionalTestsuite;

@EapAdditionalTestsuite({"modules/testcases/jdkAll/master/vertx/src/main/java#4.0.0"})
public class HttpServerMetric {

  public final HttpServerRequest request;
  public final SocketMetric socket;
  public final AtomicBoolean failed = new AtomicBoolean();
  public final AtomicReference<HttpServerResponse> response = new AtomicReference<>();
  public final AtomicReference<String> route = new AtomicReference<>();

  public HttpServerMetric(HttpServerRequest request, SocketMetric socket) {
    this.request = request;
    this.socket = socket;
  }
}
