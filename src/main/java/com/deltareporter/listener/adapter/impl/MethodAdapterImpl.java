package com.deltareporter.listener.adapter.impl;

import com.deltareporter.listener.adapter.MethodAdapter;
import com.deltareporter.listener.adapter.TestAnnotationAdapter;
import java.lang.annotation.Annotation;
import org.testng.ITestNGMethod;
import org.testng.annotations.Test;

public class MethodAdapterImpl implements MethodAdapter {
  private final ITestNGMethod method;

  public MethodAdapterImpl(ITestNGMethod method) {
    this.method = method;
  }

  public Object getMethod() {
    methodNotNull();
    return this.method;
  }

  public Annotation[] getMethodAnnotations() {
    methodNotNull();
    return this.method.getConstructorOrMethod().getMethod().getAnnotations();
  }

  public String getMethodName() {
    methodNotNull();
    return this.method.getConstructorOrMethod().getMethod().getName();
  }

  public String getDeclaredClassName() {
    methodNotNull();
    return this.method.getConstructorOrMethod().getDeclaringClass().getName();
  }

  public String getTestClassName() {
    methodNotNull();
    return this.method.getTestClass().getName();
  }

  public String getRealClassName() {
    methodNotNull();
    return this.method.getRealClass().getName();
  }

  public String[] getMethodDependsOnMethods() {
    methodNotNull();
    return this.method.getMethodsDependedUpon();
  }

  public boolean isBeforeClassConfiguration() {
    methodNotNull();
    return this.method.isBeforeClassConfiguration();
  }

  public boolean isAfterClassConfiguration() {
    methodNotNull();
    return this.method.isAfterClassConfiguration();
  }

  public boolean isBeforeTestConfiguration() {
    methodNotNull();
    return this.method.isBeforeTestConfiguration();
  }

  public boolean isAfterTestConfiguration() {
    methodNotNull();
    return this.method.isAfterTestConfiguration();
  }

  public TestAnnotationAdapter getTestAnnotationAdapter() {
    methodNotNull();
    Test testAnnotation =
        this.method.getConstructorOrMethod().getMethod().<Test>getAnnotation(Test.class);
    return new TestAnnotationAdapterImpl(testAnnotation);
  }

  private void methodNotNull() {
    if (this.method == null)
      throw new RuntimeException("TestNG method is required to apply its data");
  }
}
