package com.jiahao.restful.helper;

import com.jiahao.restful.DIContainer;
import com.jiahao.restful.UriTable;
import com.jiahao.restful.util.Utils;
import org.javatuples.Quartet;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public class Dispatcher {
  private final UriTable uriTable;
  private final DIContainer container;

  public Dispatcher() {
    this.uriTable = UriTable.getUriTable();
    this.container = DIContainer.getContainer();
  }

  public Object dispatch(String uri, Class<? extends Annotation> httpMethod, String dataString) throws IllegalAccessException, InvocationTargetException {
    String targetUri = UriHelper.normalize(uri);

    Quartet<String, Class<? extends Annotation>, Method, Class<?>> quartet = uriChecker(targetUri, httpMethod);

    Object target = null;
    Method method = quartet.getValue2();
    Class<?> clz = quartet.getValue3();
    Class<?>[] parameterTypes = method.getParameterTypes();
    if (parameterTypes.length != 0) {
      target = Utils.deserialize(dataString, parameterTypes[0]);
    }

    if (Objects.isNull(target)) {
      return method.invoke(container.getInstance(clz));
    } else {
      return method.invoke(container.getInstance(clz), target);
    }
  }

  private Quartet<String, Class<? extends Annotation>, Method, Class<?>> uriChecker(String targetUri, Class<? extends Annotation> httpMethod) {
    for (Quartet<String, Class<? extends Annotation>, Method, Class<?>> quartet : uriTable.getTable()) {
      if (
              targetUri.equals(quartet.getValue0()) &&
              httpMethod.equals(quartet.getValue1())
      ) {
        return quartet;
      }
    }

    throw new RuntimeException("Not found");
  }
}
