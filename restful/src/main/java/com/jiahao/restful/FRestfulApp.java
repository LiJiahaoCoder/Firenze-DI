package com.jiahao.restful;

import com.jiahao.di.FApp;
import com.jiahao.di.annotation.DIEntry;
import com.jiahao.restful.annotation.RestfulEntry;
import com.jiahao.restful.util.Utils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FRestfulApp {
  private final FApp diApp;

  public FRestfulApp() {
    this.diApp = new FApp(FRestfulApp.class);
  }

  @DIEntry
  public void start(Class<?> entry) throws Exception {
    diApp.start();
    List<Class<?>> classes = Utils.getAllClasses(entry);
    List<Method> methods = Arrays.stream(entry.getMethods())
            .filter(method -> Objects.nonNull(method.getDeclaredAnnotation(RestfulEntry.class)))
            .collect(Collectors.toList());

    if (methods.isEmpty()) {
      throw new RuntimeException("No @RestfulEntry");
    } else if (methods.size() > 1) {
      throw new RuntimeException("Can only exist 1 @RestfulEntry");
    }

    createServer().run();
  }

  private static FRestfulServer createServer() {
    return new FRestfulServer();
  }
}
