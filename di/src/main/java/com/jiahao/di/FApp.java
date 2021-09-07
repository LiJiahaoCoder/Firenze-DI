package com.jiahao.di;

import com.jiahao.di.annotation.Entry;
import com.jiahao.di.exceptions.AppStartException;
import com.jiahao.di.utils.Utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FApp {
  private final FContainer container;
  private final Class<?> entryClass;

  public FApp(Class<?> entryClass) {
    this.container = new FContainer();
    this.entryClass = entryClass;
  }

  public void start(Object... args) {
    List<String> classes = Utils.getAllClasses(entryClass.getPackage());

    scanPackages(classes);

    List<Method> methods = Arrays.stream(entryClass.getMethods())
            .filter(method -> Objects.nonNull(method.getDeclaredAnnotation(Entry.class)))
            .collect(Collectors.toList());

    if (methods.isEmpty()) {
      throw new AppStartException("Start app failed, no @Entry");
    } else if (methods.size() > 1) {
      throw new AppStartException("Start app failed, multiple @Entry");
    }

    Method main = methods.get(0);
    try {
      main.invoke(container.getInstance(entryClass), args);
    } catch (InvocationTargetException e) {
      throw new AppStartException("Start app failed, invoke entry method failed");
    } catch (IllegalAccessException e) {
      throw new AppStartException("Start app failed, invoke entry method failed");
    }
  }

  public <T> T getInstance(Class<T> clazz) {
    return container.getInstance(clazz);
  }

  private void scanPackages(List<String> classNames) {
    classNames.stream()
            .filter(className -> !className.startsWith("com.jiahao.di"))
            .map(this::getClass)
            .forEach(container::register);
  }

  private Class<?> getClass(String className) {
    try {
      return Class.forName(className);
    } catch (ClassNotFoundException e) {
      throw new AppStartException("Start app failed, cannot found " + className);
    }
  }
}
