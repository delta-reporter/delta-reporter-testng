package com.deltareporter.listener.adapter;

public interface SuiteAdapter {

  String getSuiteParameter(String paramString);

  String[] getSuiteDependsOnMethods();

  String testSuiteContextStatus();

}

