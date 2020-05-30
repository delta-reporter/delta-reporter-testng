package com.deltareporter.client;

import com.deltareporter.models.TestCaseType;
import com.deltareporter.models.TestSuiteHistoryType;

public interface ExtendedClient {

  TestCaseType registerTestCase(
      String paramString1, String paramString2, String paramString3, Integer paramInteger1, Integer paramInteger2, Integer paramInteger3);

  Integer registerLaunch(String paramString1, String paramString2);

  Integer registerTestRun(String paramString1, Integer paramString2, String paramString3);

  TestSuiteHistoryType registerTestSuiteHistory(
      String paramString1, String paramString2, String paramString3, Integer paramInteger4, String paramString5);

  void finishTestSuiteHistory(Integer paramInteger1, String paramString2, String paramString3);

  void finishTestRun(Integer paramInteger1, String paramString2, String paramString3);

  void finishLaunch(Integer paramInteger1);
}
