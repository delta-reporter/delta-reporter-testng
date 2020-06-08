package com.deltareporter.client;

public enum Path {
  PROJECTS_PATH("/api/v1/project"),
  STATUS_PATH("/api/v1/status"),
  TEST_SUITE_HISTORY_PATH("/api/v1/test_suite_history"),
  TEST_CASE_HISTORY_PATH("/api/v1/test_history"),
  LAUNCH_PATH("/api/v1/launch"),
  LAUNCH_FINISH("/api/v1/finish_launch"),
  TEST_RUNS_PATH("/api/v1/test_run");

  private final String relativePath;

  Path(String relativePath) {
    this.relativePath = relativePath;
  }

  public String build(String serviceUrl, Object... parameters) {
    return serviceUrl + String.format(this.relativePath, parameters);
  }
}
