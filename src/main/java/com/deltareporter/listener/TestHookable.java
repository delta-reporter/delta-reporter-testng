package com.deltareporter.listener;

import com.deltareporter.listener.adapter.TestResultAdapter;

public interface TestHookable {
  void runTestMethod(TestResultAdapter paramTestResultAdapter);
}
