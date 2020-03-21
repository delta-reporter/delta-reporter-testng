package com.deltareporter.listener.domain;

public enum DeltaConfiguration implements Configuration {
  ENABLED("delta_enabled", Boolean.valueOf(false), Boolean.class, false),
  SERVICE_URL("delta_service_url", new Object(), String.class, false),
  TEST_TYPE("delta_test_type", new Object(), String.class, false),
  PROJECT("delta_project", "", String.class, true),
  CONFIGURATOR(
      "delta_configurator", "com.deltareporter.config.DefaultConfigurator", String.class, true);

  private final String configName;

  private final Object defaultValue;

  private final Class configurationClass;

  private final boolean canOverride;

  DeltaConfiguration(
      String configName, Object defaultValue, Class configurationClass, boolean canOverride) {
    this.configName = configName;
    this.defaultValue = defaultValue;
    this.configurationClass = configurationClass;
    this.canOverride = canOverride;
  }

  public boolean canOverride() {
    return this.canOverride;
  }

  public String getConfigName() {
    return this.configName;
  }

  public Object getDefaultValue() {
    return this.defaultValue;
  }

  public Class getConfigClass() {
    return this.configurationClass;
  }
}
