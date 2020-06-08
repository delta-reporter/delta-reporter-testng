package com.deltareporter.listener.adapter.impl;

import com.deltareporter.listener.adapter.MethodAdapter;
import com.deltareporter.listener.adapter.TestResultAdapter;
import com.deltareporter.listener.adapter.TestResultStatus;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.xml.XmlClass;

public class TestResultAdapterImpl implements TestResultAdapter {
  private final ITestResult testResult;

  public TestResultAdapterImpl(ITestResult testResult) {
    this.testResult = testResult;
  }

  public ITestResult getTestResult() {
    testResultNotNull();
    return this.testResult;
  }

  public String getName() {
    testResultNotNull();
    return this.testResult.getName();
  }

  public Throwable getThrowable() {
    testResultNotNull();
    return this.testResult.getThrowable();
  }

  public String getFile() {
    testResultNotNull();
    return this.testResult.getTestClass().getName() + ".java";
  }
  
  public TestResultStatus getStatus() {
    testResultNotNull();
    return Arrays.<TestResultStatus>stream(TestResultStatus.values())
        .filter(testResultStatus -> (testResultStatus.getCode() == this.testResult.getStatus()))
        .findFirst()
        .orElse(TestResultStatus.UNKNOWN);
  }

  public Set<String> getKnownClassNames() {
    testResultNotNull();
    return (Set<String>)
        this.testResult.getTestContext().getCurrentXmlTest().getClasses().stream()
            .map(XmlClass::getName)
            .collect(Collectors.toSet());
  }

  public RuntimeException getSkipExceptionInstance(String message) {
    return (RuntimeException) new SkipException(message);
  }

  public MethodAdapter getMethodAdapter() {
    testResultNotNull();
    return new MethodAdapterImpl(this.testResult.getMethod());
  }

  public Object[] getParameters() {
    testResultNotNull();
    return this.testResult.getParameters();
  }

  private void testResultNotNull() {
    if (this.testResult == null)
      throw new RuntimeException("TestNG test result is required to apply its data");
  }
}
