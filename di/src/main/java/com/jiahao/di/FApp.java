package com.jiahao.di;

import com.jiahao.di.exceptions.AppStartException;
import com.jiahao.di.utils.Utils;

import java.util.List;

public class FApp {
  private final FContainer container;
  private final Class<?> entryClass;

  public FApp(Class<?> entryClass) {
    this.container = new FContainer();
    this.entryClass = entryClass;
  }

  public void start() {
    List<Class<?>> classes = Utils.getAllClasses(entryClass);
    registerClasses(classes);
  }

  public <T> T getInstance(Class<T> clazz) {
    return container.getInstance(clazz);
  }

  private void registerClasses(List<Class<?>> classes) {
    classes.stream()
            .map(Class::getName)
            .filter(className -> !className.startsWith("com.jiahao.di"))
            .map(this::getClassByName)
            .forEach(container::register);
  }

  private Class<?> getClassByName(String className) {
    try {
      return Class.forName(className);
    } catch (ClassNotFoundException e) {
      throw new AppStartException("Start app failed, cannot found " + className);
    }
  }
}
