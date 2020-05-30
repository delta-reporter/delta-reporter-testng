package com.deltareporter.listener.service.impl;

import com.deltareporter.client.DeltaClient;
import com.deltareporter.listener.service.TestCaseTypeService;
import com.deltareporter.models.TestCaseType;
import com.deltareporter.util.http.HttpClient;

public class TestCaseTypeServiceImpl implements TestCaseTypeService {
  private final DeltaClient deltaClient;

  public TestCaseTypeServiceImpl(DeltaClient deltaClient) {
    this.deltaClient = deltaClient;
  }

  public TestCaseType registerTestCase(
      String name, String datetime, String parameters, Integer test_suite_id, Integer test_run_id, Integer test_suite_history_id) {
    return this.deltaClient.registerTestCase(name, datetime, parameters, test_suite_id, test_run_id, test_suite_history_id);
  }

  public TestCaseType finishTest(TestCaseType test) {
    HttpClient.Response<TestCaseType> result = this.deltaClient.finishTest(test);
    if (result.getStatus() != 200 && result.getObject() == null) {
      throw new RuntimeException(
          "Unable to register test "
              + test.getName()
              + " for delta service: "
              + this.deltaClient.getServiceUrl());
    }
    return (TestCaseType) result.getObject();
  }
}
