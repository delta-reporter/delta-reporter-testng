package com.deltareporter.listener.service.impl;

import com.deltareporter.client.DeltaClient;
import com.deltareporter.listener.service.ProjectTypeService;

public class ProjectTypeServiceImpl implements ProjectTypeService {
  private final DeltaClient deltaClient;

  public ProjectTypeServiceImpl(DeltaClient deltaClient) {
    this.deltaClient = deltaClient;
  }

  public void initProject(String projectName) {
    this.deltaClient.initProject(projectName);
  }
}
