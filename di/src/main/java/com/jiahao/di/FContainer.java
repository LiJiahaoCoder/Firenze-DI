package com.jiahao.di;

import com.jiahao.di.exceptions.CreateException;
import com.jiahao.di.resolver.Register;
import com.jiahao.di.resolver.Resolver;
import com.jiahao.di.utils.CircularDependencyGuard;
import com.jiahao.di.utils.Utils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class FContainer {
  private final Register register = new Register();
  private final CircularDependencyGuard guard = new CircularDependencyGuard();

  public void register(Class<?> clazz) {
    if (Objects.isNull(clazz.getAnnotation(Singleton.class))) {
      register.add(clazz);
    } else {
      register.add(clazz, createInstance(clazz));
    }
  }

  public <T> T getInstance(Class<T> clazz) {
    if (!register.contains(clazz)) {
      throw new CreateException(clazz.getSimpleName() + " is not registered in the container.");
    }

    if (Objects.nonNull(clazz.getAnnotation(Singleton.class))) {
      return (T) register.getSingletonSaver().get(clazz);
    }

    return createInstance(clazz);
  }

  public <T> T createInstance(Class<T> clazz) {
    Constructor<?> constructor = Utils.getConstructorByAnnotation(clazz, Inject.class)
            .orElseGet(() -> Utils.getConstructorWithoutArgs(clazz));
    List<Object> parameters = getParameters(clazz, constructor);

    try {
      return (T) constructor.newInstance(parameters.toArray());
    } catch (Exception e) {
      throw new CreateException("Cannot create instance for " + clazz.getSimpleName());
    }
  }

  public Set<Class<?>> getRegisteredClasses() {
    return register.getClasses();
  }

  private List<Object> getParameters(Class<?> clazz, Constructor<?> constructor) {
    guard.start(clazz);

    List<Object> params = Arrays.stream(constructor.getParameters())
            .map(annotation -> Resolver.resolveClass(annotation, register.getClasses()))
            .map(this::getInstance)
            .collect(Collectors.toList());

    guard.end();

    return params;
  }
}
