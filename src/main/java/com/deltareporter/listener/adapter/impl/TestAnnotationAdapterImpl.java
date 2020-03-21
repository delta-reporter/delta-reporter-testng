package com.deltareporter.listener.adapter.impl;

import com.deltareporter.listener.adapter.TestAnnotationAdapter;
import org.testng.annotations.Test;

import java.lang.annotation.Annotation;


public class TestAnnotationAdapterImpl
  implements TestAnnotationAdapter
{
  private final Test annotation;

  public TestAnnotationAdapterImpl(Test annotation) {
    this.annotation = annotation;
  }


  public Class<? extends Annotation> getTestAnnotationClass() {
    return (Class)Test.class;
  }


  public String getDataProviderName() {
    annotationNotNull();
    return this.annotation.dataProvider();
  }


  public boolean isEnabled() {
    annotationNotNull();
    return this.annotation.enabled();
  }

  private void annotationNotNull() {
    if (this.annotation == null)
      throw new RuntimeException("TestNG test annotation is required to apply its data");
  }
}

