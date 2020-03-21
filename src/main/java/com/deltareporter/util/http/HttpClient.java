package com.deltareporter.util.http;

import com.deltareporter.client.Path;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.util.Map;
import java.util.function.Function;
import javax.ws.rs.core.MultivaluedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClient {
  private static final Logger LOGGER = LoggerFactory.getLogger(HttpClient.class);

  private static final Integer CONNECT_TIMEOUT = Integer.valueOf(60000);
  private static final Integer READ_TIMEOUT = Integer.valueOf(60000);

  private static Client client =
      Client.create((ClientConfig) new DefaultClientConfig(new Class[] {GensonProvider.class}));

  static {
    client.setConnectTimeout(CONNECT_TIMEOUT);
    client.setReadTimeout(READ_TIMEOUT);
  }

  public static Executor uri(Path path, String serviceUrl, Object... parameters) {
    String url = path.build(serviceUrl, parameters);
    return uri(url, null);
  }

  public static Executor uri(
      Path path, Map<String, String> queryParameters, String serviceUrl, Object... parameters) {
    String url = path.build(serviceUrl, parameters);
    return uri(url, queryParameters);
  }

  private static Executor uri(String url, Map<String, String> queryParameters) {
    WebResource webResource = client.resource(url);
    if (queryParameters != null) {
      MultivaluedMapImpl multivaluedMapImpl = new MultivaluedMapImpl();
      queryParameters.forEach(multivaluedMapImpl::add);
      webResource = webResource.queryParams((MultivaluedMap) multivaluedMapImpl);
    }
    return new Executor(webResource);
  }

  public static class Executor {
    private WebResource.Builder builder;
    private String errorMessage;

    public Executor(WebResource webResource) {
      this.builder =
          (WebResource.Builder)
              webResource.type("application/json").accept(new String[] {"application/json"});
    }

    public <R> HttpClient.Response<R> get(Class<R> responseClass) {
      return execute(responseClass, builder -> (ClientResponse) builder.get(ClientResponse.class));
    }

    public <R> HttpClient.Response<R> post(Class<R> responseClass, Object requestEntity) {
      return execute(
          responseClass,
          builder -> (ClientResponse) builder.post(ClientResponse.class, requestEntity));
    }

    public <R> HttpClient.Response<R> put(Class<R> responseClass, Object requestEntity) {
      return execute(
          responseClass,
          builder -> (ClientResponse) builder.put(ClientResponse.class, requestEntity));
    }

    public <R> HttpClient.Response<R> delete(Class<R> responseClass) {
      return execute(
          responseClass, builder -> (ClientResponse) builder.delete(ClientResponse.class));
    }

    public Executor type(String mediaType) {
      this.builder.type(mediaType);
      return this;
    }

    public Executor accept(String mediaType) {
      this.builder.accept(new String[] {mediaType});
      return this;
    }

    public Executor withAuthorization(String authToken) {
      return withAuthorization(authToken, null);
    }

    public Executor withAuthorization(String authToken, String project) {
      initHeaders(this.builder, authToken, project);
      return this;
    }

    private static void initHeaders(WebResource.Builder builder, String authToken, String project) {
      if (!StringUtils.isEmpty(authToken)) {
        builder.header("Authorization", authToken);
      }
      if (!StringUtils.isEmpty(project)) {
        builder.header("Project", project);
      }
    }

    private <R> HttpClient.Response<R> execute(
        Class<R> responseClass, Function<WebResource.Builder, ClientResponse> methodBuilder) {
      HttpClient.Response<R> rs = new HttpClient.Response<>();
      try {
        ClientResponse response = methodBuilder.apply(this.builder);
        int status = response.getStatus();
        rs.setStatus(status);
        if (responseClass != null && !responseClass.isAssignableFrom(Void.class) && status == 200) {
          rs.setObject((R) response.getEntity(responseClass));
        }
      } catch (Exception e) {
        String message =
            (this.errorMessage == null)
                ? e.getMessage()
                : (e.getMessage() + ". " + this.errorMessage);
        HttpClient.LOGGER.error(message, e);
      }
      return rs;
    }

    public Executor onFailure(String message) {
      this.errorMessage = message;
      return this;
    }
  }

  public static class Response<T> {
    private int status;

    private T object;

    public Response() {}

    Response(int status, T object) {
      this.status = status;
      this.object = object;
    }

    public int getStatus() {
      return this.status;
    }

    public void setStatus(int status) {
      this.status = status;
    }

    public T getObject() {
      return this.object;
    }

    public void setObject(T object) {
      this.object = object;
    }
  }
}
