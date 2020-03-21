package com.deltareporter.config;

import com.deltareporter.listener.adapter.TestResultAdapter;

public interface IConfigurator {

  String getTestName(TestResultAdapter paramTestResultAdapter);

  String getTestMethodName(TestResultAdapter paramTestResultAdapter);

  void clearArtifacts();
}
