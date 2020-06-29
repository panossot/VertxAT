/*
 * Copyright (c) 2011-2017 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
 * which is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
 */

package io.vertx.test.fakemetrics;

import io.vertx.core.http.WebSocketBase;
import org.jboss.eap.additional.testsuite.annotations.EapAdditionalTestsuite;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@EapAdditionalTestsuite({"modules/testcases/jdkAll/master/vertx/src/main/java#3.6.0*3.8.5"})
public class WebSocketMetric {

  public final SocketMetric soMetric;
  public final WebSocketBase ws;

  public WebSocketMetric(SocketMetric soMetric, WebSocketBase ws) {
    this.soMetric = soMetric;
    this.ws = ws;
  }
}
