package com.deltareporter.client.impl;

import com.deltareporter.client.BasicClient;
import com.deltareporter.client.ExtendedClient;
import com.deltareporter.models.*;
import com.deltareporter.util.http.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtendedClientImpl implements ExtendedClient {
  private static final Logger LOGGER = LoggerFactory.getLogger(ExtendedClientImpl.class);

  private final BasicClient client;

  public ExtendedClientImpl(BasicClient client) {
    this.client = client;
  }

  public Integer registerLaunch(String name, String project) {
    LaunchType launch = new LaunchType(name, project);
    String launchDetails = "Name: %s, Project: %s";
    LOGGER.debug(
        "Launch details to create:" + String.format(launchDetails, new Object[] {name, project}));

    HttpClient.Response<LaunchType> response = this.client.createLaunch(launch);
    Integer launch_id = (int) response.getObject().getId();

    if (launch_id == null) {
      throw new RuntimeException(
          "Unable to create launch: '"
              + name
              + "' for delta service: "
              + this.client.getServiceUrl());
    }
    LOGGER.debug("Launch created! Launch ID:" + launch_id);

    return launch_id;
  }

  public Integer registerTestRun(String test_type, Integer launch_id, String datetime) {
    TestRunType test_run = new TestRunType(test_type, launch_id, datetime);
    String testRunDetails = "Test Type: %s, Launch ID: %s, DateTime: %s";
    LOGGER.debug(
        "Test Run details to create:"
            + String.format(testRunDetails, new Object[] {test_type, launch_id, datetime}));

    HttpClient.Response<TestRunType> response = this.client.createTestRun(test_run);
    Integer test_run_id = (int) response.getObject().getId();

    if (test_run_id == null) {
      throw new RuntimeException(
          "Unable to create test run for delta service: " + this.client.getServiceUrl());
    }
    LOGGER.debug("Test Run created! Test Run ID:" + test_run_id);

    return test_run_id;
  }

  public TestSuiteHistoryType registerTestSuiteHistory(
      String name, String test_type, String start_datetime, Integer test_run_id, String project) {
    TestSuiteHistoryType test_suite_history =
        new TestSuiteHistoryType(name, test_type, start_datetime, test_run_id, project);
    String suiteHistoryDetails = "Name: %s, Test Type: %s, DateTime: %s, TestRunId: %s, Project: %s";
    LOGGER.debug(
        "Test Suite History details to create:"
            + String.format(
                suiteHistoryDetails, new Object[] {name, test_type, start_datetime, test_run_id, project}));

    HttpClient.Response<TestSuiteHistoryType> response =
        this.client.createTestSuiteHistory(test_suite_history);
    test_suite_history = (TestSuiteHistoryType) response.getObject();

    if (test_suite_history == null) {
      throw new RuntimeException(
          "Unable to create test suite history for delta service: " + this.client.getServiceUrl());
    }
    LOGGER.debug(
        "Test Suite History created! Test Suite History ID:"
            + test_suite_history.getTest_suite_history_id());

    return test_suite_history;
  }

  public void finishTestSuiteHistory(
      Integer test_suite_history_id, String end_datetime, String test_suite_status) {
    TestSuiteHistoryType test_suite_history =
        new TestSuiteHistoryType(test_suite_history_id, end_datetime, test_suite_status);
    String suiteHistoryDetails = "ID: %d, End Datetime: %s, Test Suite Status: %s";
    LOGGER.debug(
        "Test Suite History details to update:"
            + String.format(
                suiteHistoryDetails,
                new Object[] {test_suite_history_id, end_datetime, test_suite_status}));

    HttpClient.Response<TestSuiteHistoryType> response =
        this.client.finishTestSuiteHistory(test_suite_history);
  }

  public void finishTestRun(Integer test_run_id, String end_datetime, String test_run_status) {
    TestRunType test_run = new TestRunType(test_run_id, end_datetime, test_run_status);
    String testRunDetails = "Test Run ID: %s, End DateTime: %s, Test Run Status: %s";
    LOGGER.debug(
        "Test Run details:"
            + String.format(
                testRunDetails, new Object[] {test_run_id, end_datetime, test_run_status}));

    HttpClient.Response<TestRunType> response = this.client.finishTestRun(test_run);
  }

  public void finishLaunch(Integer launch_id) {
    LaunchType launch = new LaunchType(launch_id);
    String launchDetails = "Launch ID: %s";
    LOGGER.debug(
        "Launch details:"
            + String.format(
                launchDetails, new Object[] {launch_id}));

    HttpClient.Response<LaunchType> response = this.client.finishLaunch(launch);
    LOGGER.debug("Launch finished! Response: " + response);
  }

  public TestCaseType registerTestCase(
      String name, String datetime, String parameters, Integer test_suite_id, Integer test_run_id, Integer test_suite_history_id) {
    TestCaseType testCase = new TestCaseType(name, datetime, parameters, test_suite_id, test_run_id, test_suite_history_id);
    String testCaseDetails = "name: %s, datetime: %s, parameters: %s, test_suite_id: %d, test_run_id: %d, test_suite_history_id: %d";
    LOGGER.debug(
        "Test Case details for registration:"
            + String.format(
                testCaseDetails, new Object[] {name, datetime, parameters, test_suite_id, test_run_id, test_suite_history_id}));
    HttpClient.Response<TestCaseType> response = this.client.createTestCase(testCase);
    testCase = (TestCaseType) response.getObject();
    if (testCase == null) {
      throw new RuntimeException(
          "Unable to register test case '"
              + String.format(
                  testCaseDetails, new Object[] {name, datetime, parameters, test_suite_id, test_run_id, test_suite_history_id})
              + "' for delta service: "
              + this.client.getServiceUrl());
    }
    LOGGER.debug(
        "Registered test case details:"
            + String.format(
                testCaseDetails, new Object[] {name, datetime, parameters, test_suite_id, test_run_id, test_suite_history_id}));

    return testCase;
  }
}
