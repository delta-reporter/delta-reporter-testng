package com.deltareporter.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestRunType extends AbstractType {
  @NotNull private String test_type;
  @NotNull private Integer launch_id;
  @NotNull private String datetime;
  private String test_run_status;
  private String end_datetime;
  private Integer test_run_id;

  public TestRunType() {}

  public String getTest_type() {
    return this.test_type;
  }

  public Integer getLaunch_id() {
    return this.launch_id;
  }

  public String getStart_datetime() {
    return this.datetime;
  }

  public String getTest_run_status() {
    return this.test_run_status;
  }

  public String getEnd_datetime() {
    return this.end_datetime;
  }

  public Integer getTest_run_id() {
    return this.test_run_id;
  }

  public TestRunType(String test_type, Integer launch_id, String datetime) {
    this.test_type = test_type;
    this.launch_id = launch_id;
    this.datetime = datetime;
  }

  public TestRunType(Integer test_run_id, String end_datetime, String test_run_status) {
    this.test_run_id = test_run_id;
    this.end_datetime = end_datetime;
    this.test_run_status = test_run_status;
  }
}
