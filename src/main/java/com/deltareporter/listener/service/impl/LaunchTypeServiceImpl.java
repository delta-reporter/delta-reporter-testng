package com.deltareporter.listener.service.impl;

import com.deltareporter.client.DeltaClient;
import com.deltareporter.listener.service.LaunchTypeService;

public class LaunchTypeServiceImpl implements LaunchTypeService {
  private final DeltaClient deltaClient;

  public LaunchTypeServiceImpl(DeltaClient deltaClient) {
    this.deltaClient = deltaClient;
  }

  public Integer register(String name, String project) {
    return this.deltaClient.registerLaunch(name, project);
  }

  public void finish(Integer launch_id) {
    this.deltaClient.finishLaunch(launch_id);
  }
}
