package com.deltareporter.client;

import com.deltareporter.models.*;
import com.deltareporter.util.http.HttpClient;

public interface BasicClient {

  boolean isAvailable();

  HttpClient.Response<LaunchType> createLaunch(LaunchType paramLaunchType);

  HttpClient.Response<TestRunType> createTestRun(TestRunType paramTestRunType);

  HttpClient.Response<TestSuiteHistoryType> createTestSuiteHistory(
      TestSuiteHistoryType paramTestSuiteHistoryType);

  HttpClient.Response<TestSuiteHistoryType> finishTestSuiteHistory(
      TestSuiteHistoryType paramTestSuiteHistoryType);

  HttpClient.Response<TestCaseType> finishTest(TestCaseType paramTestType);

  HttpClient.Response<TestRunType> finishTestRun(TestRunType paramTestRunType);

  HttpClient.Response<LaunchType> finishLaunch(LaunchType paramLaunchType);

  HttpClient.Response<TestCaseType> createTestCase(TestCaseType paramTestCaseType);

  HttpClient.Response<ProjectType> getProjectByName(String paramString);

  String getProject();

  BasicClient initProject(String paramString);

  String getServiceUrl();
}
