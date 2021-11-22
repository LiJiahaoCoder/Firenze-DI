package com.jiahao.di.resolver;

import javax.inject.Singleton;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Register {
  private final Set<Class<?>> classes = new HashSet<>();
  private final SingletonSaver singletonSaver = new SingletonSaver();

  public void add(Class<?> clazz) {
    classes.add(clazz);
  }

  public void add(Class<?> clazz, Object instance) {
    singletonSaver.add(clazz, instance);
  }

  public boolean contains(Class<?> clz) {
    return classes.contains(clz) || singletonSaver.contains(clz);
  }

  public SingletonSaver getSingletonSaver() {
    return singletonSaver;
  }

  public Set<Class<?>> getClasses() {
    return classes;
  }
}
