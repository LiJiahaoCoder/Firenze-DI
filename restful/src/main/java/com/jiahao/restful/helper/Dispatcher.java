package com.jiahao.restful.helper;

import com.jiahao.restful.DIContainer;
import com.jiahao.restful.UriTable;
import org.javatuples.Quartet;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Dispatcher {
  private final UriTable uriTable;
  private final DIContainer container;

  public Dispatcher() {
    this.uriTable = UriTable.getUriTable();
    this.container = DIContainer.getContainer();
  }

  public Object dispatch(String uri, Class<? extends Annotation> httpMethod) throws IllegalAccessException, InvocationTargetException {
    String targetUri = UriResolver.resolve(uri);

    uriChecker(targetUri);

    for (Quartet<String, Class<? extends Annotation>, Method, Class<?>> quartet : uriTable.getTable()) {
      if (
              quartet.getValue0().equals(targetUri) &&
              quartet.getValue1().equals(httpMethod)
      ) {
        Method method = quartet.getValue2();
        Class<?> clz = quartet.getValue3();

        return method.invoke(container.getInstance(clz));
      }

    }

    return null;
  }

  private void uriChecker(String targetUri) {
    for (Quartet<String, Class<? extends Annotation>, Method, Class<?>> quartet : uriTable.getTable()) {
      if (targetUri.equals(quartet.getValue0())) return;
    }

    throw new RuntimeException("Not found");
  }
}
