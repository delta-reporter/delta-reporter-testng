package com.deltareporter.listener.service.impl;

import com.deltareporter.client.DeltaClient;
import com.deltareporter.listener.service.TestRunTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestRunTypeServiceImpl implements TestRunTypeService {
  private static final Logger LOGGER = LoggerFactory.getLogger(TestRunTypeServiceImpl.class);

  private final DeltaClient deltaClient;

  public TestRunTypeServiceImpl(DeltaClient deltaClient) {
    this.deltaClient = deltaClient;
  }

  public Integer register(String test_type, Integer launch_id, String datetime) {
    return this.deltaClient.registerTestRun(test_type, launch_id, datetime);
  }

  public void finish(Integer test_run_id, String end_datetime, String test_run_status) {
    this.deltaClient.finishTestRun(test_run_id, end_datetime, test_run_status);
  }
}
