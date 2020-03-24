package com.deltareporter.listener.service.impl;

import com.deltareporter.client.DeltaClient;
import com.deltareporter.listener.service.TestSuiteHistoryTypeService;
import com.deltareporter.models.TestSuiteHistoryType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestSuiteHistoryTypeServiceImpl implements TestSuiteHistoryTypeService {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(TestSuiteHistoryTypeServiceImpl.class);

  private final DeltaClient deltaClient;

  public TestSuiteHistoryTypeServiceImpl(DeltaClient deltaClient) {
    this.deltaClient = deltaClient;
  }

  public TestSuiteHistoryType register(
      String name, String test_type, String start_datetime, Integer test_run_id, String project) {
    return this.deltaClient.registerTestSuiteHistory(name, test_type, start_datetime, test_run_id, project);
  }

  public void finish(Integer test_suite_history_id, String end_datetime, String test_suite_status) {
    this.deltaClient.finishTestSuiteHistory(test_suite_history_id, end_datetime, test_suite_status);
  }
}
