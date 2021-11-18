package com.jiahao.restful.helper;

import com.jiahao.restful.DIContainer;
import com.jiahao.restful.UriTree;
import io.netty.handler.codec.http.HttpMethod;
import org.javatuples.Triplet;

import javax.ws.rs.GET;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Dispatcher {
  private final UriTree uriTree;
  private final DIContainer container;

  public Dispatcher() {
    this.uriTree = UriTree.getInstance();
    this.container = DIContainer.getContainer();
  }

  public Object dispatch(String uri, HttpMethod httpMethod) throws InstantiationException, IllegalAccessException, InvocationTargetException {
    for (Triplet<String, HttpMethod, Class<?>> triplet : uriTree.getTable()) {
      if (triplet.getValue0().equals(uri.split("/")[1]) && triplet.getValue1().equals(httpMethod)) {
        Class<?> clz = triplet.getValue2();

        Method method = Arrays.stream(clz
                .getMethods())
                .filter(m -> m.isAnnotationPresent(GET.class))
                .findFirst()
                .get();

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
