package com.deltareporter.client;

import com.deltareporter.client.impl.DeltaClientImpl;
import com.deltareporter.listener.domain.DeltaConfiguration;
import com.deltareporter.util.AsyncUtil;
import com.deltareporter.util.ConfigurationUtil;
import java.util.concurrent.CompletableFuture;
import org.apache.commons.configuration2.CombinedConfiguration;

public enum DeltaSingleton {
  INSTANCE;

  private DeltaClient deltaClient;
  private final CompletableFuture<Integer> INIT_FUTURE;

  DeltaSingleton() {
    this.INIT_FUTURE =
        CompletableFuture.supplyAsync(
            () -> {
              Integer result = null;

              try {
                CombinedConfiguration config = ConfigurationUtil.getConfiguration(false);

                boolean enabled = ((Boolean) DeltaConfiguration.ENABLED.get(config)).booleanValue();

                String url = (String) DeltaConfiguration.SERVICE_URL.get(config);

                this.deltaClient = new DeltaClientImpl(url);
                if (enabled && this.deltaClient.isAvailable()) {
                  result = 1;
                }
              } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
              }

              return result;
            });
  }

  public DeltaClient getClient() {
    return isRunning() ? this.deltaClient : null;
  }

  public boolean isRunning() {
    Integer response;
    try {
      response = AsyncUtil.getAsync(this.INIT_FUTURE, "Cannot connect to Delta Reporter");
    } catch (Exception e) {
      return false;
    }
    return (response != null);
  }
}
