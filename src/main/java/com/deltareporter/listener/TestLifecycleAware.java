package com.deltareporter.listener;

import com.deltareporter.listener.adapter.SuiteAdapter;
import com.deltareporter.listener.adapter.TestContextAdapter;
import com.deltareporter.listener.adapter.TestResultAdapter;

public interface TestLifecycleAware {
  void onTestRunStarted(SuiteAdapter paramSuiteAdapter);

  void onTestSuiteContextFinish(SuiteAdapter paramSuiteAdapter);

  void onTestContextStart(TestContextAdapter paramTestContextAdapter);

  void onTestContextFinish(TestContextAdapter paramTestContextAdapter);

  void onTestStart(TestResultAdapter paramTestResultAdapter);

  void onTestSuccess(TestResultAdapter paramTestResultAdapter);

  void onTestFailure(TestResultAdapter paramTestResultAdapter);

  void onTestSkipped(TestResultAdapter paramTestResultAdapter);

  void onTestHook(TestHookable paramTestHookable, TestResultAdapter paramTestResultAdapter);
}
