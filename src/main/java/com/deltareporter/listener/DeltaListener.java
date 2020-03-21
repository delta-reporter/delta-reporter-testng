package com.deltareporter.listener;


import com.deltareporter.listener.adapter.impl.SuiteAdapterImpl;
import com.deltareporter.listener.adapter.impl.TestContextAdapterImpl;
import com.deltareporter.listener.adapter.impl.TestResultAdapterImpl;
import org.testng.*;


public class DeltaListener
  implements ISuiteListener, ITestListener, IHookable, IInvokedMethodListener
{
  private final TestLifecycleAware listener = new DeltaEventRegistrar();

  
  public void onStart(ISuite suiteContext) {
    SuiteAdapterImpl suiteAdapterImpl = new SuiteAdapterImpl(suiteContext);
    this.listener.onTestRunStarted(suiteAdapterImpl);
  }

  public void onStart(ITestContext testContext) {
    TestContextAdapterImpl testContextAdapterImpl = new TestContextAdapterImpl(testContext);
    this.listener.onTestContextStart(testContextAdapterImpl);
  }

  
  public void onTestStart(ITestResult result) {
    TestResultAdapterImpl testResultAdapterImpl = new TestResultAdapterImpl(result);
    this.listener.onTestStart(testResultAdapterImpl);
  }

  
  public void onTestSuccess(ITestResult result) {
    TestResultAdapterImpl testResultAdapterImpl = new TestResultAdapterImpl(result);
    this.listener.onTestSuccess(testResultAdapterImpl);
  }

  
  public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    TestResultAdapterImpl testResultAdapterImpl = new TestResultAdapterImpl(result);
    this.listener.onTestFailure(testResultAdapterImpl);
  }

  
  public void onTestFailure(ITestResult result) {
    TestResultAdapterImpl testResultAdapterImpl = new TestResultAdapterImpl(result);
    this.listener.onTestFailure(testResultAdapterImpl);
  }

  
  public void onTestSkipped(ITestResult result) {
    TestResultAdapterImpl testResultAdapterImpl = new TestResultAdapterImpl(result);
    this.listener.onTestSkipped(testResultAdapterImpl);
  }

  
  public void onFinish(ISuite suiteContext) {
    SuiteAdapterImpl suiteAdapterImpl = new SuiteAdapterImpl(suiteContext);
    this.listener.onTestSuiteContextFinish(suiteAdapterImpl);
  }


  public void onFinish(ITestContext testContext) {
    TestContextAdapterImpl testContextAdapterImpl = new TestContextAdapterImpl(testContext);
    this.listener.onTestContextFinish(testContextAdapterImpl);
  }


  public void run(IHookCallBack hookCallBack, ITestResult result) {
    TestResultAdapterImpl testResultAdapterImpl = new TestResultAdapterImpl(result);
    this.listener.onTestHook(adapterToRun -> hookCallBack.runTestMethod(result), testResultAdapterImpl);
  }
  

  public void beforeInvocation(IInvokedMethod invokedMethod, ITestResult result) {
  }
  

  public void afterInvocation(IInvokedMethod invokedMethod, ITestResult testResult) {}
}
