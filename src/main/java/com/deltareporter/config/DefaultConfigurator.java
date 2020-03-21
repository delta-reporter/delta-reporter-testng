package com.deltareporter.config;

import com.deltareporter.listener.adapter.TestResultAdapter;
import org.testng.ITestResult;


public class DefaultConfigurator
  implements IConfigurator
{

  public String getTestName(TestResultAdapter adapter) {
    ITestResult testResult = (ITestResult)adapter.getTestResult();
    return testResult.getName();
  }


  public String getTestMethodName(TestResultAdapter adapter) {
    return adapter.getMethodAdapter().getMethodName();
  }

  public void clearArtifacts() {}

}
