package com.jiahao.restful;

import com.jiahao.di.annotation.DIEntry;
import com.jiahao.restful.annotation.RestfulEntry;
import com.jiahao.restful.helper.Register;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FRestfulApp {
  private final Register register;

  public FRestfulApp() {
    this.register = new Register();
  }

  @DIEntry
  public void start(Class<?> entry) throws Exception {
    DIContainer.initContainer(entry);
    register(entry);
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

  private void register(Class<?> entry) {
    register.register(entry);
  }

  private static FRestfulServer createServer() {
    return new FRestfulServer();
  }
}
