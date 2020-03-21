package com.deltareporter.util.http;

import com.owlike.genson.Genson;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
public class GensonProvider implements ContextResolver<Genson> {
  private final Genson genson = (new Genson.Builder()).useTimeInMillis(true).create();

  public Genson getContext(Class<?> type) {
    return this.genson;
  }
}
