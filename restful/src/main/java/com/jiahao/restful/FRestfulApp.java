package com.jiahao.restful;

import com.jiahao.di.annotation.Entry;
import com.jiahao.restful.annotation.RestfulEntry;
import com.jiahao.restful.util.Utils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FRestfulApp {
  private final FRestfulServer server;
  private final Class<?> entryClass;

  public FRestfulApp(Class<?> entryClass) {
    this.server = new FRestfulServer();
    this.entryClass = entryClass;
  }

  @Entry
  public void start(Object... args) throws Exception {
    List<String> classes = Utils.getAllClasses(entryClass.getPackage());

    scanPackages(classes);

    List<Method> methods = Arrays.stream(entryClass.getMethods())
            .filter(method -> Objects.nonNull(method.getDeclaredAnnotation(RestfulEntry.class)))
            .collect(Collectors.toList());

    if (methods.isEmpty()) {
      throw new RuntimeException("No @RestfulEntry");
    } else if (methods.size() > 1) {
      throw new RuntimeException("Can only exist 1 @RestfulEntry");
    }

    server.run();
  }

  private List<Class> scanPackages(List<String> classNames) {
    return classNames.stream()
            .filter(className -> !className.startsWith("com.jiahao.restful"))
            .map(this::getClass)
            .collect(Collectors.toList());
  }

  private Class<?> getClass(String cls) {
    try {
      return Class.forName(cls);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("Cannot found " + cls);
    }
  }
}
