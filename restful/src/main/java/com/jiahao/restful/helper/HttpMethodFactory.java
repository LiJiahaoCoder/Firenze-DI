package com.jiahao.restful.helper;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import java.lang.annotation.Annotation;
import java.util.Map;

public class HttpMethodFactory {
  private static final Map<Class<? extends Annotation>, HttpMethod> METHOD_MAP = Map.of(
          GET.class, HttpMethod.GET,
          PUT.class, HttpMethod.PUT,
          POST.class, HttpMethod.POST,
          DELETE.class, HttpMethod.DELETE
  );

  public static HttpMethod getMethod(Class<? extends Annotation> methodAnnotation) {
    return METHOD_MAP.get(methodAnnotation);
  }
}
