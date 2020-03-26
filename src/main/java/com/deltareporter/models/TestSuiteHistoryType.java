package com.deltareporter.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestSuiteHistoryType extends AbstractType {
  @NotNull private String name;
  @NotNull private String test_type;
  @NotNull private String start_datetime;
  @NotNull private Integer test_run_id;
  private Integer test_suite_history_id;
  private Integer test_suite_id;
  private String end_datetime;
  private String test_suite_status;
  private String project;

  public void setTest_suite_history_id(Integer test_suite_history_id) {
    this.test_suite_history_id = test_suite_history_id;
  }

  public void setTest_suite_id(Integer test_suite_history_id) {
    this.test_suite_id = test_suite_history_id;
  }

  public TestSuiteHistoryType() {}

  public String getName() {
    return this.name;
  }

  public String getTest_type() {
    return this.test_type;
  }

  public String getStart_datetime() {
    return this.start_datetime;
  }

  public Integer getTest_run_id() {
    return this.test_run_id;
  }

  public String getProject() {
    return this.project;
  }

  public Integer getTest_suite_history_id() {
    return this.test_suite_history_id;
  }

  public Integer getTest_suite_id() {
    return this.test_suite_id;
  }

  public String getEnd_datetime() {
    return this.end_datetime;
  }

  public String getTest_suite_status() {
    return this.test_suite_status;
  }

  public TestSuiteHistoryType(
      String name, String test_type, String start_datetime, Integer test_run_id, String project) {
    this.name = name;
    this.test_type = test_type;
    this.start_datetime = start_datetime;
    this.test_run_id = test_run_id;
    this.project = project;
  }

  public TestSuiteHistoryType(
      Integer test_suite_history_id, String end_datetime, String test_suite_status) {
    this.test_suite_history_id = test_suite_history_id;
    this.end_datetime = end_datetime;
    this.test_suite_status = test_suite_status;
  }
}
