package com.deltareporter.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestCaseType extends AbstractType {
  @NotNull private String name;
  @NotNull private String start_datetime;
  @NotNull private Integer test_suite_id;
  @NotNull private Integer test_run_id;
  private Integer test_id;
  private Integer test_history_id;
  private boolean needRerun;
  private String status;
  private String end_datetime;
  private Data data;

  public void setTest_history_id(Integer test_history_id) {
    this.test_history_id = test_history_id;
  }

  public void setStart_datetime(String datetime) {
    this.start_datetime = datetime;
  }

  public void setEnd_datetime(String datetime) {
    this.end_datetime = datetime;
  }

  public void setNeedRerun(boolean needRerun) {
    this.needRerun = needRerun;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setData(Data data) {
    this.data = data;
  }

  public TestCaseType() {}

  public String getName() {
    return this.name;
  }

  public String getStart_datetime() {
    return this.start_datetime;
  }

  public String getEnd_datetime() {
    return this.end_datetime;
  }

  public Integer getTest_suite_id() {
    return this.test_suite_id;
  }

  public Integer getTest_run_id() {
    return this.test_run_id;
  }

  public Integer getTest_history_id() {
    return this.test_history_id;
  }

  public Integer getTest_id() {
    return this.test_id;
  }

  public String getTest_status() {
    return this.status;
  }

  public Data getData() {
    return this.data;
  }

  public boolean isNeedRerun() {
    return this.needRerun;
  }

  public TestCaseType(String name, String datetime, Integer test_suite_id, Integer test_run_id) {
    this.name = name;
    this.start_datetime = datetime;
    this.test_suite_id = test_suite_id;
    this.test_run_id = test_run_id;
  }
}
