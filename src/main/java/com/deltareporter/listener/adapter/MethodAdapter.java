package com.deltareporter.listener.adapter;

public interface MethodAdapter {

  String getMethodName();

  String getDeclaredClassName();

  String getTestClassName();

  String[] getMethodDependsOnMethods();

  boolean isBeforeClassConfiguration();

  boolean isAfterClassConfiguration();

  boolean isBeforeTestConfiguration();

  boolean isAfterTestConfiguration();
}
