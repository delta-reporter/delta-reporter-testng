package com.deltareporter.util;

import org.apache.commons.configuration2.CombinedConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.SystemConfiguration;
import org.apache.commons.configuration2.builder.BuilderParameters;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.tree.MergeCombiner;
import org.apache.commons.configuration2.tree.NodeCombiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurationUtil {
  private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationUtil.class);

  private static final String ERR_MSG_INIT_CONFIG = "Unable to initialize a configuration '%s'";

  private static CombinedConfiguration configuration;

  public static CombinedConfiguration getConfiguration() {
    return getConfiguration(true);
  }

  public static CombinedConfiguration getConfiguration(boolean throwExceptionOnMissing) {
    if (configuration != null) {
      return configuration;
    }
    CombinedConfiguration config = new CombinedConfiguration((NodeCombiner) new MergeCombiner());
    try {
      config.setThrowExceptionOnMissing(throwExceptionOnMissing);
      config.addConfiguration((Configuration) new SystemConfiguration());
      config.addConfiguration((Configuration) getDeltaPropertiesConfiguration());
    } catch (ConfigurationException e) {
      String message =
          String.format(
              "Unable to initialize a configuration '%s'", new Object[] {"delta.properties"});
      LOGGER.error(message, (Throwable) e);
    }
    configuration = config;
    return config;
  }

  public static void addSystemConfiguration(String key, String value) {
    System.setProperty(key, value);
    if (configuration != null) {
      configuration.addConfiguration((Configuration) new SystemConfiguration());
    }
  }

  private static FileBasedConfiguration getDeltaPropertiesConfiguration()
      throws ConfigurationException {
    return (FileBasedConfiguration)
        (new FileBasedConfigurationBuilder(PropertiesConfiguration.class))
            .configure(
                new BuilderParameters[] {
                  (BuilderParameters)
                      (new Parameters()).properties().setFileName("delta.properties")
                })
            .getConfiguration();
  }
}
