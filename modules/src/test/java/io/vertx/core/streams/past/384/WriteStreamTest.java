/*
 * Copyright (c) 2011-2018 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
 * which is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
 */
package io.vertx.core.streams;

import io.vertx.codegen.annotations.Nullable;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.test.core.AsyncTestBase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.jboss.eap.additional.testsuite.annotations.EapAdditionalTestsuite;

@EapAdditionalTestsuite({"modules/testcases/jdkAll/master/vertx/src/main/java#3.8.4*3.8.4"})
public class WriteStreamTest extends AsyncTestBase {

  static class StreamBase<T> implements WriteStream<T> {
    @Override public StreamBase<T> exceptionHandler(Handler<Throwable> handler) { throw new UnsupportedOperationException(); }
    @Override public StreamBase<T> write(T data) { throw new UnsupportedOperationException(); }
    @Override public StreamBase<T> write(T data, Handler<AsyncResult<Void>> handler) { throw new UnsupportedOperationException(); }
    @Override public void end() { throw new UnsupportedOperationException(); }
    @Override public void end(Handler<AsyncResult<Void>> handler) { throw new UnsupportedOperationException(); }
    @Override public StreamBase<T> setWriteQueueMaxSize(int maxSize) { throw new UnsupportedOperationException(); }
    @Override public boolean writeQueueFull() { throw new UnsupportedOperationException(); }
    @Override public StreamBase<T> drainHandler(@Nullable Handler<Void> handler) { throw new UnsupportedOperationException(); }
  }

  static class EndWithItemStreamAsync extends StreamBase<Object> {
    AtomicInteger writeCount = new AtomicInteger();
    Promise<Void> writeFut = Promise.promise();
    AtomicInteger endCount = new AtomicInteger();
    Promise<Void> endFut = Promise.promise();
    AtomicInteger resolvedCount = new AtomicInteger();
    Promise<Void> resolvedFut = Promise.promise();
    @Override
    public StreamBase<Object> write(Object data, Handler<AsyncResult<Void>> handler) {
      writeCount.incrementAndGet();
      writeFut.future().setHandler(handler);
      return this;
    }
    @Override
    public void end(Handler<AsyncResult<Void>> handler) {
      endCount.incrementAndGet();
      endFut.future().setHandler(handler);
    }
    public void end(Object item) {
      end(item, ar -> {
        resolvedCount.incrementAndGet();
        resolvedFut.handle(ar);
      });
    }
  }

  @Test
  public void testEndWithItemStreamAsync() {
    Object item = new Object();
    Throwable cause = new Throwable();

    EndWithItemStreamAsync src = new EndWithItemStreamAsync();
    src.end(item);
    assertEquals(1, src.writeCount.get());
    assertEquals(0, src.endCount.get());
    assertEquals(0, src.resolvedCount.get());
    src.writeFut.complete();
    assertEquals(1, src.writeCount.get());
    assertEquals(1, src.endCount.get());
    assertEquals(0, src.resolvedCount.get());
    src.endFut.complete();
    assertEquals(1, src.writeCount.get());
    assertEquals(1, src.endCount.get());
    assertEquals(1, src.resolvedCount.get());
    assertTrue(src.resolvedFut.future().succeeded());
    assertNull(src.resolvedFut.future().result());

    src = new EndWithItemStreamAsync();
    src.end(item);
    src.writeFut.fail(cause);
    assertEquals(1, src.writeCount.get());
    assertEquals(0, src.endCount.get());
    assertEquals(1, src.resolvedCount.get());
    assertTrue(src.resolvedFut.future().failed());
    assertSame(cause, src.resolvedFut.future().cause());

    src = new EndWithItemStreamAsync();
    src.end(item);
    src.writeFut.complete();
    src.endFut.fail(cause);
    assertEquals(1, src.writeCount.get());
    assertEquals(1, src.endCount.get());
    assertEquals(1, src.resolvedCount.get());
    assertTrue(src.resolvedFut.future().failed());
    assertSame(cause, src.resolvedFut.future().cause());
  }

  static class EndStreamSync extends StreamBase<Object> {
    AtomicInteger writeCount = new AtomicInteger();
    AtomicInteger endCount = new AtomicInteger();
    List<Object> items = new ArrayList<>();
    @Override
    public StreamBase<Object> write(Object data) {
      items.add(data);
      writeCount.incrementAndGet();
      return this;
    }
    @Override
    public void end() {
      endCount.incrementAndGet();
    }
  }

  @Test
  public void testEndStreamAsync() {
    Object item = new Object();
    EndStreamSync src = new EndStreamSync();
    src.end(item);
    assertEquals(1, src.writeCount.get());
    assertEquals(1, src.endCount.get());
    assertEquals(Arrays.asList(item), src.items);
  }

  static class EndStreamSync2 extends StreamBase<Object> {
    AtomicInteger writeCount = new AtomicInteger();
    AtomicInteger endCount = new AtomicInteger();
    List<Object> items = new ArrayList<>();
    RuntimeException cause = new RuntimeException();
    @Override
    public StreamBase<Object> write(Object data) {
      items.add(data);
      writeCount.incrementAndGet();
      throw cause;
    }
    @Override
    public void end() {
      endCount.incrementAndGet();
    }
  }

  @Test
  public void testEndStreamAsync2() {
    Object item = new Object();
    EndStreamSync2 src = new EndStreamSync2();
    try {
      src.end(item);
      fail();
    } catch (Exception e) {
      assertSame(e, src.cause);
    }
    assertEquals(1, src.writeCount.get());
    assertEquals(0, src.endCount.get());
    assertEquals(Collections.singletonList(item), src.items);
  }

  static class EndStreamSync3 extends StreamBase<Object> {
    AtomicInteger writeCount = new AtomicInteger();
    AtomicInteger endCount = new AtomicInteger();
    List<Object> items = new ArrayList<>();
    RuntimeException cause = new RuntimeException();
    @Override
    public StreamBase<Object> write(Object data) {
      items.add(data);
      writeCount.incrementAndGet();
      return this;
    }
    @Override
    public void end() {
      endCount.incrementAndGet();
      throw cause;
    }
  }

  @Test
  public void testEndStreamAsync3() {
    Object item = new Object();
    EndStreamSync3 src = new EndStreamSync3();
    try {
      src.end(item);
      fail();
    } catch (Exception e) {
      assertSame(e, src.cause);
    }
    assertEquals(1, src.writeCount.get());
    assertEquals(1, src.endCount.get());
    assertEquals(Collections.singletonList(item), src.items);
  }
}
