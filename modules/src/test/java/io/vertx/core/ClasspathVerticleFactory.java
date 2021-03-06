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

package io.vertx.core;

import io.vertx.core.spi.VerticleFactory;

import java.util.concurrent.Callable;

/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
import org.jboss.eap.additional.testsuite.annotations.EapAdditionalTestsuite;

@EapAdditionalTestsuite({"modules/testcases/jdkAll/master/vertx/src/main/java#4.0.0"})
public class ClasspathVerticleFactory implements VerticleFactory {

  @Override
  public void init(Vertx vertx) {
  }

  @Override
  public String prefix() {
    return "wibble";
  }

  @Override
  public void createVerticle(String verticleName, ClassLoader classLoader, Promise<Callable<Verticle>> promise) {
    promise.complete();
  }

  @Override
  public void close() {

  }
}
