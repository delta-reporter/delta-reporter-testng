package com.deltareporter.listener.adapter;

import java.util.Set;

public interface TestResultAdapter {
  Object getTestResult();

  String getName();

  Throwable getThrowable();

  String getFile();

  TestResultStatus getStatus();

  Set<String> getKnownClassNames();

  RuntimeException getSkipExceptionInstance(String paramString);

  MethodAdapter getMethodAdapter();

  Object[] getParameters();
}
