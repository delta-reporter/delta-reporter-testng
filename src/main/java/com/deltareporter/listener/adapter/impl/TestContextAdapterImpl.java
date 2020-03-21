package com.deltareporter.listener.adapter.impl;

import com.deltareporter.listener.adapter.TestContextAdapter;
import org.testng.ITestContext;

public class TestContextAdapterImpl implements TestContextAdapter {
  private final ITestContext test_context;

  public TestContextAdapterImpl(ITestContext test_context) {
    this.test_context = test_context;
  }

  public String getTestContextName() {
    testContextNotNull();
    return this.test_context.getName();
  }

  public String getSuiteName() {
    testContextNotNull();
    return this.test_context.getSuite().getName();
  }

  public String testContextStatus() {
    testContextNotNull();
    return ((this.test_context.getFailedTests().size() == 0) ? "Successful" : "Failed");
  }

  private void testContextNotNull() {
    if (this.test_context == null)
      throw new RuntimeException("TestNG test context is required to apply its data");
  }
}
