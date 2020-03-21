package com.deltareporter.util;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsyncUtil {
  private static final Logger LOGGER = LoggerFactory.getLogger(AsyncUtil.class);

  private static final int CONNECT_TIMEOUT = 60000;

  public static <I> I get(
      CompletableFuture<I> async, Supplier<CompletableFuture<I>> initFunctionality) {
    if (async == null) {
      async = initFunctionality.get();
    }
    return getAsync(async);
  }

  public static <I> I getAsync(CompletableFuture<I> async) {
    return getAsync(async, null);
  }

  public static <I> I getAsync(CompletableFuture<I> async, String errorMessage) {
    I result = null;
    if (async != null) {
      try {
        result = async.get(60000L, TimeUnit.MILLISECONDS);
      } catch (InterruptedException
          | java.util.concurrent.ExecutionException
          | java.util.concurrent.TimeoutException e) {
        String message =
            (errorMessage != null) ? (e.getMessage() + ". " + errorMessage) : e.getMessage();
        LOGGER.error(message, e);
      }
    }
    return result;
  }
}
