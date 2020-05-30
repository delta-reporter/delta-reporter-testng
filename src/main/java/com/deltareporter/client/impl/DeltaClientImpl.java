package com.deltareporter.client.impl;

import com.deltareporter.client.BasicClient;
import com.deltareporter.client.DeltaClient;
import com.deltareporter.client.ExtendedClient;
import com.deltareporter.models.*;
import com.deltareporter.util.http.HttpClient;

public class DeltaClientImpl implements DeltaClient {
  private final BasicClient basicClient;
  private final ExtendedClient extendedClient;

  public DeltaClientImpl(String serviceUrl) {
    this.basicClient = new BasicClientImpl(serviceUrl);
    this.extendedClient = new ExtendedClientImpl(this.basicClient);
  }

  public boolean isAvailable() {
    return this.basicClient.isAvailable();
  }

  public HttpClient.Response<LaunchType> createLaunch(LaunchType launch) {
    return this.basicClient.createLaunch(launch);
  }

  public HttpClient.Response<TestRunType> createTestRun(TestRunType testRun) {
    return this.basicClient.createTestRun(testRun);
  }

  public HttpClient.Response<TestSuiteHistoryType> createTestSuiteHistory(
      TestSuiteHistoryType testSuiteHistory) {
    return this.basicClient.createTestSuiteHistory(testSuiteHistory);
  }

  public HttpClient.Response<TestSuiteHistoryType> finishTestSuiteHistory(
      TestSuiteHistoryType testSuiteHistory) {
    return this.basicClient.finishTestSuiteHistory(testSuiteHistory);
  }

  public HttpClient.Response<TestRunType> finishTestRun(TestRunType testRun) {
    return this.basicClient.finishTestRun(testRun);
  }

  public HttpClient.Response<LaunchType> finishLaunch(LaunchType testLaunch) {
    return this.basicClient.finishLaunch(testLaunch);
  }

  public HttpClient.Response<TestCaseType> finishTest(TestCaseType test) {
    return this.basicClient.finishTest(test);
  }

  public HttpClient.Response<TestCaseType> createTestCase(TestCaseType testCase) {
    return this.basicClient.createTestCase(testCase);
  }

  public HttpClient.Response<ProjectType> getProjectByName(String name) {
    return this.basicClient.getProjectByName(name);
  }

  public String getProject() {
    return this.basicClient.getProject();
  }

  public BasicClient initProject(String project) {
    return this.basicClient.initProject(project);
  }

  public String getServiceUrl() {
    return this.basicClient.getServiceUrl();
  }

  public Integer registerLaunch(String name, String project) {
    return this.extendedClient.registerLaunch(name, project);
  }

  public Integer registerTestRun(String test_type, Integer launch_id, String datetime) {
    return this.extendedClient.registerTestRun(test_type, launch_id, datetime);
  }

  public void finishTestRun(Integer test_run_id, String end_datetime, String test_run_status) {
    this.extendedClient.finishTestRun(test_run_id, end_datetime, test_run_status);
  }

  public void finishLaunch(Integer launch_id) {
    this.extendedClient.finishLaunch(launch_id);
  }

  public TestSuiteHistoryType registerTestSuiteHistory(
      String name, String test_type, String start_datetime, Integer test_run_id, String project) {
    return this.extendedClient.registerTestSuiteHistory(
        name, test_type, start_datetime, test_run_id, project);
  }

  public void finishTestSuiteHistory(
      Integer test_suite_history_id, String end_datetime, String test_suite_status) {
    this.extendedClient.finishTestSuiteHistory(
        test_suite_history_id, end_datetime, test_suite_status);
  }

  public TestCaseType registerTestCase(
      String name, String datetime, String parameters, Integer test_suite_id,  Integer test_run_id, Integer test_suite_history_id) {
    return this.extendedClient.registerTestCase(name, datetime, parameters, test_suite_id, test_run_id, test_suite_history_id);
  }
}
