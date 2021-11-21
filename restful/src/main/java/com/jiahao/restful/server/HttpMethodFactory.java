package com.jiahao.restful.server;

import io.netty.handler.codec.http.HttpMethod;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import java.lang.annotation.Annotation;
import java.util.Map;

public class HttpMethodFactory {

  private static final Map<HttpMethod, Class<? extends Annotation>> METHOD_MAP = Map.of(
          HttpMethod.GET, GET.class,
          HttpMethod.PUT, PUT.class,
          HttpMethod.POST, POST.class,
          HttpMethod.DELETE, DELETE.class
  );

  public static Class<? extends Annotation> getMethod(HttpMethod httpMethod) {
    return METHOD_MAP.get(httpMethod);
  }

}
