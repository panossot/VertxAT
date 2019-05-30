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
import io.vertx.core.impl.IsolatingClassLoader;
import org.junit.Assert;
import org.jboss.eap.additional.testsuite.annotations.EapAdditionalTestsuite;

/**
* @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
@EapAdditionalTestsuite({"modules/testcases/jdkAll/master/vertx/src/main/java"})
public class ExtraCPVerticleNotInParentLoader extends AbstractVerticle {
  @Override
  public void start() throws Exception {
    IsolatingClassLoader cl = (IsolatingClassLoader) Thread.currentThread().getContextClassLoader();
    Class extraCPClass = cl.loadClass("MyVerticle");
    Assert.assertSame(extraCPClass.getClassLoader(), cl);
    try {
      cl.getParent().loadClass("MyVerticle");
      Assert.fail("Parent classloader should not see this class");
    } catch (ClassNotFoundException expected) {
      //
    }
  }
}
