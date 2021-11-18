package com.jiahao.restful.helper;

import com.jiahao.restful.DIContainer;
import com.jiahao.restful.UriTree;
import org.javatuples.Quartet;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Dispatcher {
  private final UriTree uriTree;
  private final DIContainer container;

  public Dispatcher() {
    this.uriTree = UriTree.getInstance();
    this.container = DIContainer.getContainer();
  }

  public Object dispatch(String uri, Class<? extends Annotation> httpMethod) throws InstantiationException, IllegalAccessException, InvocationTargetException {
    for (Quartet<String, Class<? extends Annotation>, Method, Class<?>> quartet : uriTree.getTable()) {
      if (quartet.getValue0().equals(uri.split("/")[1]) && quartet.getValue1().equals(httpMethod)) {
        Method method = quartet.getValue2();
        Class<?> clz = quartet.getValue3();

        return method.invoke(container.getInstance(clz));
      }
    }

    return null;
  }

  private static void invoke(Method method) {
    try {
      method.invoke(null);
    } catch (IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
  }
}
