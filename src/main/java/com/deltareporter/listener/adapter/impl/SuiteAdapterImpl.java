package com.deltareporter.listener.adapter.impl;

import com.deltareporter.listener.adapter.MethodAdapter;
import com.deltareporter.listener.adapter.SuiteAdapter;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;

public class SuiteAdapterImpl implements SuiteAdapter {
  private final ISuite suite;

  public SuiteAdapterImpl(ISuite suite) {
    this.suite = suite;
  }

  public Object getSuite() {
    suiteNotNull();
    return this.suite;
  }

  public String getSuiteParameter(String name) {
    suiteNotNull();
    return this.suite.getParameter(name);
  }

  public String getSuiteFileName() {
    suiteNotNull();
    return this.suite.getXmlSuite().getFileName();
  }

  public String getSuiteName() {
    suiteNotNull();
    return this.suite.getName();
  }

  public String[] getSuiteDependsOnMethods() {
    suiteNotNull();

    String[] allDependentMethods =
        this.suite.getAllMethods().stream()
            .map(ITestNGMethod::getMethodsDependedUpon)
            .reduce(ArrayUtils.EMPTY_STRING_ARRAY, ArrayUtils::addAll);
    return allDependentMethods;
  }

  public List<MethodAdapter> getMethodAdapters() {
    suiteNotNull();

    return (List<MethodAdapter>) this.suite.getAllMethods().stream();
  }

  public String testSuiteContextStatus() {
    suiteNotNull();

    int failed = 0;

    for (ISuiteResult result : this.suite.getResults().values()) {
      final ITestContext testContext = result.getTestContext();
      failed += testContext.getFailedTests().size();
    }
    return ((failed == 0) ? "Passed" : "Failed");
  }

  private void suiteNotNull() {
    if (this.suite == null)
      throw new RuntimeException("TestNG suite is required to apply its data");
  }
}
