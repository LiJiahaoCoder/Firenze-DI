package com.jiahao.restful;

import com.jiahao.di.FApp;

import java.util.Objects;

public class DIContainer {

  private final FApp fApp;

  private static DIContainer container;

  private DIContainer(Class<?> entry) {
    this.fApp = new FApp(entry);
    fApp.start();
  }

  public static void initContainer(Class<?> entry) {
    if (Objects.isNull(container)) {
      container = new DIContainer(entry);
    }
  }

  public static DIContainer getContainer() {
    return container;
  }

  public <T> T getInstance(Class<T> clazz) {
    return fApp.getInstance(clazz);
  }
}
