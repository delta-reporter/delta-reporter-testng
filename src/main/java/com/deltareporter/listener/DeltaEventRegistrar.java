package com.deltareporter.listener;

import com.deltareporter.client.DeltaClient;
import com.deltareporter.client.DeltaSingleton;
import com.deltareporter.config.IConfigurator;
import com.deltareporter.listener.adapter.SuiteAdapter;
import com.deltareporter.listener.adapter.TestContextAdapter;
import com.deltareporter.listener.adapter.TestResultAdapter;
import com.deltareporter.listener.domain.DeltaConfiguration;
import com.deltareporter.listener.service.*;
import com.deltareporter.listener.service.impl.*;
import com.deltareporter.models.Data;
import com.deltareporter.models.TestCaseType;
import com.deltareporter.models.TestSuiteHistoryType;
import com.deltareporter.util.ConfigurationUtil;
import org.apache.commons.configuration2.CombinedConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBException;
import java.util.*;


public class DeltaEventRegistrar
  implements TestLifecycleAware
{
  private static final Logger LOGGER = LoggerFactory.getLogger(DeltaEventRegistrar.class);

  private boolean DELTA_ENABLED;

  private String DELTA_TEST_TYPE;
  
  private String DELTA_PROJECT;

  private Integer DELTA_TEST_RUN_ID;
  
  private boolean DELTA_RERUN_FAILURES;

  private String DELTA_CONFIGURATOR;

  private IConfigurator configurator;

  private TestSuiteHistoryType suiteHistory;

  private Map<String, TestCaseType> registeredTests = new HashMap<>();

  private static ThreadLocal<TestCaseType> threadTest = new ThreadLocal<>();
  
  private TestRunTypeService testRunTypeService;

  private TestSuiteHistoryTypeService testSuiteHistoryTypeService;

  private LaunchTypeService launchTypeService;

  private TestCaseTypeService testCaseTypeService;
  
  public void onTestRunStarted(SuiteAdapter adapter) {
    boolean initialized = initializeDelta(adapter);
    
    if (!initialized) {
      return;
    }
    
    try {
      this.configurator = (IConfigurator)Class.forName(this.DELTA_CONFIGURATOR).newInstance();
      
      String project = (String)DeltaConfiguration.PROJECT.get(adapter);
      project = !StringUtils.isEmpty(project) ? project : this.DELTA_PROJECT;
      Integer DELTA_LAUNCH_ID = NumberUtils.createInteger(System.getenv("DELTA_LAUNCH_ID"));

      if (DELTA_LAUNCH_ID == null) {
        Long date = (new Date()).getTime();
        String name = "Launch " + date;
        DELTA_LAUNCH_ID = this.launchTypeService.register(name, project);
      }
  
      String datetime = new Date().toString();

      this.DELTA_TEST_RUN_ID = this.testRunTypeService.register(this.DELTA_TEST_TYPE, DELTA_LAUNCH_ID, datetime);

    } catch (Throwable e) {
      this.DELTA_ENABLED = false;
      LOGGER.error("Undefined error during test run registration!", e);
    } 
  }

  public void onTestContextStart(TestContextAdapter test_context_adapter) {
    if (!this.DELTA_ENABLED) {
      return;
    }
    
    try {
      String datetime = new Date().toString();

      String name = test_context_adapter.getSuiteName() + "/" + test_context_adapter.getTestContextName();

      this.suiteHistory = this.testSuiteHistoryTypeService.register(name, this.DELTA_TEST_TYPE, datetime, this.DELTA_TEST_RUN_ID);

    } catch (Throwable e) {
      LOGGER.error("Undefined error during test case context start!", e);
    } 
  }


  public void onTestStart(TestResultAdapter adapter) {
    if (!this.DELTA_ENABLED) {
      return;
    }
    
    try {
      TestCaseType startedTest = null;
      
      String testName = this.configurator.getTestName(adapter);
      
      TestCaseType testCase = registerTestCase(adapter);
      
      if (this.registeredTests.containsKey(testName)) {
        startedTest = this.registeredTests.get(testName);
        
        if (this.DELTA_RERUN_FAILURES && !startedTest.isNeedRerun()) {
          throw adapter.getSkipExceptionInstance("ALREADY_PASSED: " + testName);
        }
        
        startedTest.setTest_history_id(testCase.getTest_history_id());
        startedTest.setEnd_datetime(null);
        startedTest.setStart_datetime(new Date().toString());
      } 
      
      if (startedTest == null) {
        startedTest = testCase;
      } 

      threadTest.set(startedTest);
      this.registeredTests.put(testName, startedTest);
    } catch (Throwable e) {
      if (adapter.getSkipExceptionInstance(null).getClass().isAssignableFrom(e.getClass())) {
        throw e;
      }
      LOGGER.error("Undefined error during test case/method start!", e);
    } 
  }

  
  public void onTestSuccess(TestResultAdapter adapter) {
    if (!this.DELTA_ENABLED) {
      return;
    }
    
    try {
      finishTest(adapter, "Passed");
    } catch (Throwable e) {
      LOGGER.error("Undefined error during test case/method finish!", e);
    } 
  }

  
  public void onTestFailure(TestResultAdapter adapter) {
    processResultOnTestFailure(adapter);
  }

  
  public void onTestSkipped(TestResultAdapter adapter) {
    if (!this.DELTA_ENABLED) {
      return;
    }
    if (adapter.getThrowable() != null && adapter.getThrowable().getMessage() != null && adapter
      .getThrowable().getMessage().startsWith("ALREADY_PASSED")) {
      return;
    }

    
    try {
      finishTest(adapter, "Skipped");
    } catch (Throwable e) {
      LOGGER.error("Undefined error during test case/method finish!", e);
    } 
  }

  public void onTestContextFinish(TestContextAdapter testContextAdapter) {
    String end_datetime = new Date().toString();
    this.testSuiteHistoryTypeService.finish(this.suiteHistory.getTest_suite_history_id(), end_datetime, testContextAdapter.testContextStatus());
  }

  public void onTestSuiteContextFinish(SuiteAdapter testSuiteAdapter) {
    String end_datetime = new Date().toString();
    this.testRunTypeService.finish(this.DELTA_TEST_RUN_ID, end_datetime, testSuiteAdapter.testSuiteContextStatus());
  }

  
  public void onTestHook(TestHookable hookCallBack, TestResultAdapter adapter) {
    if (!this.DELTA_ENABLED) {
      LOGGER.info("IHookCallBack: Delta Reporter not connected so running the test body");
      hookCallBack.runTestMethod(adapter);
    } else {
      String testName = this.configurator.getTestName(adapter);
      TestCaseType startedTest = this.registeredTests.get(testName);
      
      if (this.DELTA_RERUN_FAILURES && startedTest != null && !startedTest.isNeedRerun()) {
        LOGGER.info("IHookCallBack: test will not be executed since it already passed in previous run");
      } else {
        
        LOGGER.debug("IHookCallBack: default execution of test body");
        hookCallBack.runTestMethod(adapter);
      } 
    } 
  }
  
  private boolean initializeDelta(SuiteAdapter adapter) {
    try {
      CombinedConfiguration config = ConfigurationUtil.getConfiguration();
      
      this.DELTA_ENABLED = (Boolean) DeltaConfiguration.ENABLED.get(config, adapter);
      this.DELTA_TEST_TYPE = (String)DeltaConfiguration.TEST_TYPE.get(config, adapter);
      this.DELTA_PROJECT = (String)DeltaConfiguration.PROJECT.get(config, adapter);
      this.DELTA_RERUN_FAILURES = (Boolean) DeltaConfiguration.RERUN_FAILURES.get(config, adapter);
      this.DELTA_CONFIGURATOR = (String)DeltaConfiguration.CONFIGURATOR.get(config, adapter);
      
      if (this.DELTA_ENABLED) {
        DeltaClient dc = DeltaSingleton.INSTANCE.getClient();
        if (dc != null) {
          this.DELTA_ENABLED = dc.isAvailable();
          
          this.testRunTypeService = new TestRunTypeServiceImpl(dc);
          this.testSuiteHistoryTypeService = new TestSuiteHistoryTypeServiceImpl(dc);
          this.launchTypeService = new LaunchTypeServiceImpl(dc);
          this.testCaseTypeService = new TestCaseTypeServiceImpl(dc);
        } 
        LOGGER.info("Delta Reporter is " + (this.DELTA_ENABLED ? "available" : "unavailable"));
      }
    
    } catch (NoSuchElementException e) {
      LOGGER.error("Unable to find config property: ", e);
    } 
    
    return this.DELTA_ENABLED;
  }

  private String getFullStackTrace(TestResultAdapter adapter) {
    StringBuilder sb = new StringBuilder();
    if (adapter.getThrowable() == null) {
      return null;
    } else {
      
      sb.append(adapter.getThrowable().getMessage()).append("\n");
      for (StackTraceElement elem : adapter.getThrowable().getStackTrace()) {
        sb.append("\n").append(elem.toString());
      }
    } 
    return !StringUtils.isEmpty(sb.toString()) ? sb.toString() : null;
  }
  
  private void processResultOnTestFailure(TestResultAdapter adapter) {
    if (!this.DELTA_ENABLED) {
      return;
    }
    try {
      finishTest(adapter, "Failed");
    } catch (Throwable e) {
      LOGGER.error("Undefined error during test case/method finish!", e);
    } 
  }
  
  private TestCaseType populateTestResult(TestResultAdapter adapter, String status, String message) throws JAXBException {
    long threadId = Thread.currentThread().getId();
    TestCaseType test = threadTest.get();
    String finishTime = new Date().toString();
    
    String testName = this.configurator.getTestName(adapter);
    LOGGER.debug("testName registered with current thread is: " + testName);
    
    if (test == null) {
      throw new RuntimeException("Unable to find TestType result to mark test as finished! name: '" + testName + "'; threadId: " + threadId);
    }
    
    this.configurator.clearArtifacts();
    
    String testDetails = "testHistoryId: %d; thread: %s; status: %s, finishTime: %s \n message: %s";
    String logMessage = String.format(testDetails, test.getTest_history_id(), threadId, status, finishTime, message);

    LOGGER.debug("Log message:" + logMessage);
    
    Data data = new Data(logMessage);

    test.setStatus(status);
    test.setData(data);
    test.setEnd_datetime(finishTime);
    
    threadTest.remove();
    
    return test;
  }
  
  private void finishTest(TestResultAdapter adapter, String status) throws JAXBException {
    String fullStackTrace = getFullStackTrace(adapter);
    TestCaseType finishedTest = populateTestResult(adapter, status, fullStackTrace);
    this.testCaseTypeService.finishTest(finishedTest);
  }
  
  private TestCaseType registerTestCase(TestResultAdapter adapter) {    
    String testClass = adapter.getMethodAdapter().getTestClassName();
    String testMethod = this.configurator.getTestMethodName(adapter);
    String name = testClass + ":" + testMethod;
    String datetime = new Date().toString();

    return this.testCaseTypeService.registerTestCase(name, datetime, this.suiteHistory.getTest_suite_id(), this.DELTA_TEST_RUN_ID);

  }

  public static Optional<TestCaseType> getTest() {
    return Optional.ofNullable(threadTest.get());
  }
}
