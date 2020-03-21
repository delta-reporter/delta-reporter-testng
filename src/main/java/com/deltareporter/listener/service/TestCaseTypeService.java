package com.deltareporter.listener.service;

import com.deltareporter.models.TestCaseType;

public interface TestCaseTypeService {
  TestCaseType registerTestCase(String paramString1, String paramString2, Integer paramInteger1, Integer paramInteger2);

  TestCaseType finishTest(TestCaseType test);
}
