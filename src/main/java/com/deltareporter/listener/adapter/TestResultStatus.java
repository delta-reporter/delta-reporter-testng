package com.deltareporter.listener.adapter;

public enum TestResultStatus {
  UNKNOWN(-2),
  SKIP(3);

  private final int code;

  TestResultStatus(int code) {
    this.code = code;
  }

  public int getCode() {
    return this.code;
  }
}
