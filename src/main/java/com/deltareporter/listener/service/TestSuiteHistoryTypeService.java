package com.deltareporter.listener.service;

import com.deltareporter.models.TestSuiteHistoryType;

public interface TestSuiteHistoryTypeService {

  TestSuiteHistoryType register(
      String paramString1, String paramString2, String paramString3, Integer paramInteger4, String paramString5);

  void finish(Integer paramInteger1, String paramString2, String paramString3);
}
