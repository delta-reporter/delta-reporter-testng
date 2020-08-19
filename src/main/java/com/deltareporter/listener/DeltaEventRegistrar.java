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
import com.deltareporter.models.TestCaseType;
import com.deltareporter.models.TestSuiteHistoryType;
import com.deltareporter.util.ConfigurationUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.xml.bind.JAXBException;
import org.apache.commons.configuration2.CombinedConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeltaEventRegistrar implements TestLifecycleAware {
  private static final Logger LOGGER = LoggerFactory.getLogger(DeltaEventRegistrar.class);

  private boolean DELTA_ENABLED;

  private boolean GENERATED_LAUNCH;

  private String DELTA_TEST_TYPE;

  private String DELTA_PROJECT;

  private Integer DELTA_LAUNCH_ID;

  private Integer DELTA_TEST_RUN_ID;

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
      this.configurator = (IConfigurator) Class.forName(this.DELTA_CONFIGURATOR).newInstance();

      String project = (String) DeltaConfiguration.PROJECT.get(adapter);
      project = !StringUtils.isEmpty(project) ? project : this.DELTA_PROJECT;
      this.DELTA_LAUNCH_ID = NumberUtils.createInteger(System.getenv("DELTA_LAUNCH_ID"));

      if (this.DELTA_LAUNCH_ID == null) {
        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat ("E dd-MM-yyyy hh:mm:ss a");
        String name = this.DELTA_TEST_TYPE + " | " + ft.format(date);
        this.DELTA_LAUNCH_ID = this.launchTypeService.register(name, project);
        this.GENERATED_LAUNCH = true;
      }

      if (System.getenv("TEST_TYPE")!=null) {
        this.DELTA_TEST_TYPE = System.getenv("TEST_TYPE");
        System.out.println("Test type for this run: " + this.DELTA_TEST_TYPE);
      }

      String datetime = new Date().toString();

      this.DELTA_TEST_RUN_ID =
          this.testRunTypeService.register(this.DELTA_TEST_TYPE, this.DELTA_LAUNCH_ID, datetime);

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

      String name =
          test_context_adapter.getSuiteName() + "/" + test_context_adapter.getTestContextName();

      this.suiteHistory =
          this.testSuiteHistoryTypeService.register(
              name, this.DELTA_TEST_TYPE, datetime, this.DELTA_TEST_RUN_ID, this.DELTA_PROJECT);

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
    if (adapter.getThrowable() != null
        && adapter.getThrowable().getMessage() != null
        && adapter.getThrowable().getMessage().startsWith("ALREADY_PASSED")) {
      return;
    }

    try {
      finishTest(adapter, "Skipped");
    } catch (Throwable e) {
      LOGGER.error("Undefined error during test case/method finish!", e);
    }
  }

  public void onTestContextFinish(TestContextAdapter testContextAdapter) {
    if (!this.DELTA_ENABLED) {
      return;
    }
    try {
      String end_datetime = new Date().toString();
      this.testSuiteHistoryTypeService.finish(
          this.suiteHistory.getTest_suite_history_id(),
          end_datetime,
          testContextAdapter.testContextStatus());
        } catch (Throwable e) {
          LOGGER.error("Undefined error during test context finish!", e);
        }
  }

  public void onTestSuiteContextFinish(SuiteAdapter testSuiteAdapter) {
    if (!this.DELTA_ENABLED) {
      return;
    }
    try {
      String end_datetime = new Date().toString();
      this.testRunTypeService.finish(
          this.DELTA_TEST_RUN_ID, end_datetime, testSuiteAdapter.testSuiteContextStatus());
          if (this.GENERATED_LAUNCH){
            this.launchTypeService.finish(this.DELTA_LAUNCH_ID);
          }
        } catch (Throwable e) {
          LOGGER.error("Undefined error during test suite finish!", e);
        }
  }

  public void onTestHook(TestHookable hookCallBack, TestResultAdapter adapter) {
    if (!this.DELTA_ENABLED) {
      LOGGER.info("IHookCallBack: Delta Reporter not connected so running the test body");
      hookCallBack.runTestMethod(adapter);
    } else {
        LOGGER.debug("IHookCallBack: default execution of test body");
        hookCallBack.runTestMethod(adapter);
    }
  }

  private boolean initializeDelta(SuiteAdapter adapter) {
    try {
      CombinedConfiguration config = ConfigurationUtil.getConfiguration();

      this.DELTA_ENABLED = (Boolean) DeltaConfiguration.ENABLED.get(config, adapter);
      this.DELTA_TEST_TYPE = (String) DeltaConfiguration.TEST_TYPE.get(config, adapter);
      this.DELTA_PROJECT = (String) DeltaConfiguration.PROJECT.get(config, adapter);
      this.DELTA_CONFIGURATOR = (String) DeltaConfiguration.CONFIGURATOR.get(config, adapter);

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

  private TestCaseType populateTestResult(TestResultAdapter adapter, String status)
      throws JAXBException {
    long threadId = Thread.currentThread().getId();
    TestCaseType test = threadTest.get();
    String finishTime = new Date().toString();
    StringBuilder sb = new StringBuilder();
    String trace;
    String message;

    if (adapter.getThrowable() == null) {
      trace = null;
      message = null;
    } else {
      message = adapter.getThrowable().getMessage();
      for (StackTraceElement elem : adapter.getThrowable().getStackTrace()) {
        sb.append("\n").append(elem.toString());
      }
    }
    trace = !StringUtils.isEmpty(sb.toString()) ? sb.toString() : null;

    String testName = this.configurator.getTestName(adapter);
    LOGGER.debug("testName registered with current thread is: " + testName);

    if (test == null) {
      throw new RuntimeException(
          "Unable to find TestType result to mark test as finished! name: '"
              + testName
              + "'; threadId: "
              + threadId);
    }

    this.configurator.clearArtifacts();

    test.setStatus(status);
    test.setTrace(trace);
    test.setFile(adapter.getFile());
    test.setMessage(message);
    test.setError_type(null);
    test.setEnd_datetime(finishTime);

    threadTest.remove();

    return test;
  }

  private void finishTest(TestResultAdapter adapter, String status) throws JAXBException {
    TestCaseType finishedTest = populateTestResult(adapter, status);
    this.testCaseTypeService.finishTest(finishedTest);
  }

  private TestCaseType registerTestCase(TestResultAdapter adapter) {
    ObjectMapper mapper = new ObjectMapper();

    String testClass = adapter.getMethodAdapter().getTestClassName();
    String testMethod = this.configurator.getTestMethodName(adapter);
    Object[] testParameters = adapter.getParameters();
    String params = null;
    String name = null;
    if (testParameters.length > 0) {
      try {
        params = mapper.writeValueAsString(testParameters);
        name = testClass + ":" + testMethod + " [" + params;
        name = name.substring(0, Math.min(name.length(), 299));
        name = name + "]";
        params = params.substring(0, Math.min(params.length(), 3000));
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
    } else {
      name = testClass + ":" + testMethod;
      name = name.substring(0, Math.min(name.length(), 300));

    }
    String datetime = new Date().toString();

    return this.testCaseTypeService.registerTestCase(
        name, datetime, params, this.suiteHistory.getTest_suite_id(), this.DELTA_TEST_RUN_ID, this.suiteHistory.getTest_suite_history_id());
  }

  public static Optional<TestCaseType> getTest() {
    return Optional.ofNullable(threadTest.get());
  }
}
