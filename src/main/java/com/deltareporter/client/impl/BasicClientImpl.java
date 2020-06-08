package com.deltareporter.client.impl;

import com.deltareporter.client.BasicClient;
import com.deltareporter.client.Path;
import com.deltareporter.models.*;
import com.deltareporter.util.http.HttpClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicClientImpl implements BasicClient {
  private static final Logger LOGGER = LoggerFactory.getLogger(BasicClientImpl.class);

  private final String serviceURL;
  private String project = "UNKNOWN";

  public BasicClientImpl(String serviceURL) {
    this.serviceURL = serviceURL;
  }

  public boolean isAvailable() {
    try{
      HttpClient.Response response =
        HttpClient.uri(Path.STATUS_PATH, this.serviceURL, new Object[0])
            .onFailure("Unable to send ping")
            .get(String.class);

        return response.getStatus() == 200;
    } catch (Exception e) {
      LOGGER.info("Cannot connect to Delta Reporter:", e);
      return false;
    }
  }

  public synchronized HttpClient.Response<LaunchType> createLaunch(LaunchType launch) {
    return HttpClient.uri(Path.LAUNCH_PATH, this.serviceURL, new Object[0])
        .onFailure("Unable to create launch")
        .post(LaunchType.class, launch);
  }

  public synchronized HttpClient.Response<TestRunType> createTestRun(TestRunType testRun) {
    return HttpClient.uri(Path.TEST_RUNS_PATH, this.serviceURL, new Object[0])
        .onFailure("Unable to create test run")
        .post(TestRunType.class, testRun);
  }

  public synchronized HttpClient.Response<TestSuiteHistoryType> createTestSuiteHistory(
      TestSuiteHistoryType testHistory) {
    return HttpClient.uri(Path.TEST_SUITE_HISTORY_PATH, this.serviceURL, new Object[0])
        .onFailure("Unable to create test suite history")
        .post(TestSuiteHistoryType.class, testHistory);
  }

  public synchronized HttpClient.Response<TestSuiteHistoryType> finishTestSuiteHistory(
      TestSuiteHistoryType testHistory) {
    return HttpClient.uri(Path.TEST_SUITE_HISTORY_PATH, this.serviceURL, new Object[0])
        .onFailure("Unable to update test suite history")
        .put(TestSuiteHistoryType.class, testHistory);
  }

  public HttpClient.Response<TestCaseType> finishTest(TestCaseType test) {
    return HttpClient.uri(Path.TEST_CASE_HISTORY_PATH, this.serviceURL, new Object[0])
        .onFailure("Unable to finish test")
        .put(TestCaseType.class, test);
  }

  public synchronized HttpClient.Response<TestRunType> finishTestRun(TestRunType testRun) {
    return HttpClient.uri(Path.TEST_RUNS_PATH, this.serviceURL, new Object[0])
        .onFailure("Unable to finish test run")
        .put(TestRunType.class, testRun);
  }

  public synchronized HttpClient.Response<LaunchType> finishLaunch(LaunchType launch) {
    return HttpClient.uri(Path.LAUNCH_FINISH, this.serviceURL, new Object[0])
        .onFailure("Unable to finish launch")
        .put(LaunchType.class, launch);
  }

  public synchronized HttpClient.Response<TestCaseType> createTestCase(TestCaseType testCase) {
    return HttpClient.uri(Path.TEST_CASE_HISTORY_PATH, this.serviceURL, new Object[0])
        .onFailure("Unable to create test case")
        .post(TestCaseType.class, testCase);
  }

  public String getProject() {
    return this.project;
  }

  public HttpClient.Response<ProjectType> getProjectByName(String name) {
    return HttpClient.uri(Path.PROJECTS_PATH, this.serviceURL, new Object[] {name})
        .onFailure("Unable to get project by name")
        .get(ProjectType.class);
  }

  public BasicClient initProject(String project) {
    if (!StringUtils.isEmpty(project)) {
      HttpClient.Response<ProjectType> rs = getProjectByName(project);
      if (rs.getStatus() == 200) {
        this.project = ((ProjectType) rs.getObject()).getName();
      }
    }
    return this;
  }

  public String getServiceUrl() {
    return this.serviceURL;
  }
}
