/*
 * Copyright (c) 2014 Red Hat, Inc. and others
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
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;

/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
import org.jboss.eap.additional.testsuite.annotations.EapAdditionalTestsuite;

@EapAdditionalTestsuite({"modules/testcases/jdkAll/master/vertx/src/main/java"})
public class ClasspathVerticleFactoryTest extends VertxTestBase {

  @Test
  public void testLoadedFromClasspath() {
    assertEquals(1, vertx.verticleFactories().size());
    VerticleFactory fact = vertx.verticleFactories().iterator().next();
    assertTrue(fact instanceof ClasspathVerticleFactory);
  }
}
