package com.deltareporter.listener.domain;

import com.deltareporter.listener.adapter.SuiteAdapter;

interface Configuration<T> {
  boolean canOverride();

  String getConfigName();

  T getDefaultValue();

  Class<T> getConfigClass();

  default T get(SuiteAdapter adapter) {
    String configValue = adapter.getSuiteParameter(getConfigName());
    return getConfigClass().cast(configValue);
  }

  default T get(org.apache.commons.configuration2.Configuration config) {
    return (T) config.get(getConfigClass(), getConfigName(), getDefaultValue());
  }

  default T get(org.apache.commons.configuration2.Configuration config, SuiteAdapter adapter) {
    return (canOverride() && get(adapter) != null) ? get(adapter) : get(config);
  }
}
