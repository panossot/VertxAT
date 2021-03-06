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

import io.vertx.core.spi.observability.HttpRequest;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.jboss.eap.additional.testsuite.annotations.EapAdditionalTestsuite;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@EapAdditionalTestsuite({"modules/testcases/jdkAll/master/vertx/src/main/java#4.0.0"})
public class HttpClientMetric {

  public final EndpointMetric endpoint;
  public final HttpRequest request;
  public final AtomicInteger requestEnded = new AtomicInteger();
  public final AtomicInteger responseBegin = new AtomicInteger();
  public final AtomicBoolean failed = new AtomicBoolean();

  public HttpClientMetric(EndpointMetric endpoint, HttpRequest request) {
    this.endpoint = endpoint;
    this.request = request;
  }
}
