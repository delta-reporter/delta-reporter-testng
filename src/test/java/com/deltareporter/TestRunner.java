package com.deltareporter;

import com.deltareporter.listener.DeltaListenerjUnit;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {
    public static void main(String[] args) {

        JUnitCore runner = new JUnitCore();

        Result result = JUnitCore.runClasses(JunitTestSuite.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        runner.addListener(new DeltaListenerjUnit());

        // runner.run(TestJunit1.class, TestJunit2.class);
        runner.run(JunitTestSuite.class);

        System.out.println(result.wasSuccessful());
    }
}
