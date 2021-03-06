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

package io.vertx.core.cli.converters;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

import org.jboss.eap.additional.testsuite.annotations.EapAdditionalTestsuite;

@EapAdditionalTestsuite({"modules/testcases/jdkAll/master/vertx/src/main/java#3.6.0*3.7.1"})
public class StringConverterTest {

  @Test
  public void testFromString() throws Exception {
    assertThat(StringConverter.INSTANCE.fromString("hello")).isEqualTo("hello");
    assertThat(StringConverter.INSTANCE.fromString("")).isEqualTo("");
    assertThat(StringConverter.INSTANCE.fromString(null)).isEqualTo(null);
  }
}
