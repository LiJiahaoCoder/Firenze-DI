package com.jiahao.di.resolver;

import java.util.HashMap;

public class SingletonSaver {
  private final HashMap<Class<?>, Object> saver = new HashMap<>();

  public void add(Class<?> clz, Object instance) {
    saver.put(clz, instance);
  }

  public Object get(Class<?> clz) {
    return saver.get(clz);
  }

  public boolean contains(Class<?> clz) {
    return saver.containsKey(clz);
  }
}
