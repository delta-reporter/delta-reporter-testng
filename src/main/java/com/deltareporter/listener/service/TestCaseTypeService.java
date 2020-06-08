package com.deltareporter.listener.service;

import com.deltareporter.models.TestCaseType;

public interface TestCaseTypeService {
  TestCaseType registerTestCase(
      String paramString1, String paramString2, String paramString3, Integer paramInteger1, Integer paramInteger2, Integer paramInteger3);

  TestCaseType finishTest(TestCaseType test);
}
