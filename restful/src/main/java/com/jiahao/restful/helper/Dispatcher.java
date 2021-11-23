package com.jiahao.restful.helper;

import com.jiahao.restful.DIContainer;
import com.jiahao.restful.UriTable;
import com.jiahao.restful.util.Utils;
import org.javatuples.Quartet;

import javax.ws.rs.Path;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

public class Dispatcher {
  private final UriTable uriTable;
  private final DIContainer container;

  public Dispatcher() {
    this.uriTable = UriTable.getUriTable();
    this.container = DIContainer.getContainer();
  }

  public Object dispatch(String requestUri, Class<? extends Annotation> httpMethod, String dataString) throws IllegalAccessException, InvocationTargetException {

    String targetUri = UriHelper.normalize(requestUri);

    Quartet<String, Class<? extends Annotation>, Method, Class<?>> quartet = uriChecker(targetUri, httpMethod);

    Object params = null;
    Method method = quartet.getValue2();
    Class<?> clz = quartet.getValue3();
    Class<?>[] parameterTypes = method.getParameterTypes();
    Class<? extends Annotation> httpMethodClass = quartet.getValue1();

    if (parameterTypes.length != 0) {
      if (isPathClass(httpMethodClass)) {
        params = UriHelper.parse(targetUri, quartet.getValue0());
      } else {
        params = Utils.deserialize(dataString, parameterTypes[0]);
      }
    }

    if (Objects.isNull(params)) {
      return method.invoke(container.getInstance(clz));
    } else if (isPathClass(httpMethodClass)) {
      return handleSubResource(targetUri, httpMethod, method, (String[]) params, clz, dataString);
    } else {
      return method.invoke(container.getInstance(clz), params);
    }

  }

  private Object handleSubResource(String uri, Class<? extends Annotation> httpMethod, Method method, String[] params, Class<?> clz, String dataString) {

    String path = uri + "/" + UriHelper.normalize(method.getAnnotation(Path.class).value());
    Object subResource = null;

    try {
      subResource = method.invoke(container.getInstance(clz), params);
    } catch (IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }

    for (Method m : Objects.requireNonNull(subResource).getClass().getMethods()) {
      for (Annotation annotation : m.getAnnotations()) {
        if (Register.isHttpMethodAnnotation(annotation)) {
          if (isPathClass(annotation.annotationType())) {
            return handleSubResource(path, httpMethod, m, params, subResource.getClass(), dataString);
          } else {
            if (httpMethod.equals(annotation.annotationType())) {
              try {
                if (m.getParameterCount() == 0) {
                  return m.invoke(subResource);
                } else {
                  return m.invoke(subResource, params);
                }
              } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
              }
            }
          }
        }
      }
    }

    return null;
  }

  private Quartet<String, Class<? extends Annotation>, Method, Class<?>> uriChecker(String targetUri, Class<? extends Annotation> httpMethod) {

    for (Quartet<String, Class<? extends Annotation>, Method, Class<?>> quartet : uriTable.getTable()) {

      Class<? extends Annotation> method = quartet.getValue1();

      if (UriHelper.match(targetUri, quartet.getValue0())) {

        if (isPathClass(method)) {
          return quartet;
        } else if (method.equals(httpMethod)) {
          return quartet;
        }

      }

    }

    throw new RuntimeException("Not found");

  }

  private boolean isPathClass(Class<? extends Annotation> httpMethodClass) {
    return httpMethodClass.equals(Path.class);
  }

}
