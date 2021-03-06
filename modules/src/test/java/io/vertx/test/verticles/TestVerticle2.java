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

package io.vertx.test.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Promise;

import java.util.HashSet;
import java.util.Set;
import org.jboss.eap.additional.testsuite.annotations.EapAdditionalTestsuite;

/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
@EapAdditionalTestsuite({"modules/testcases/jdkAll/master/vertx/src/main/java#3.8.1"})
public class TestVerticle2 extends AbstractVerticle {

  private static Set<Context> contexts = new HashSet<>();

  @Override
  public void start() throws Exception {
    synchronized (contexts) {
      if (contexts.contains(context)) {
        throw new IllegalStateException("Same context!");
      } else {
        contexts.add(context);
        vertx.eventBus().send("tvstarted", "started");
      }
    }
  }

  @Override
  public void stop(Promise<Void> stopPromise) throws Exception {
    vertx.eventBus().request("tvstopped", "stopped", reply -> {
      stopPromise.complete(null);
    });
  }
}
