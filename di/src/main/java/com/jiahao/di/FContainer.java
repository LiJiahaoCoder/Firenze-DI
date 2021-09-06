package com.jiahao.di;

import com.jiahao.di.exceptions.CreateException;
import com.jiahao.di.utils.CircularDependencyGuard;
import com.jiahao.di.utils.Resolver;
import com.jiahao.di.utils.Utils;

import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FContainer {
  private final Set<Class<?>> registeredClasses = new HashSet<>();
  private final CircularDependencyGuard guard = new CircularDependencyGuard();

  public void register(Class<?> clazz) {
    registeredClasses.add(clazz);
  }

  public <T> T getInstance(Class<T> clazz) {
    if (!registeredClasses.contains(clazz)) {
      throw new CreateException(clazz.getSimpleName() + " is not registered in the container.");
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

  /*public <T> List<T> getInstances(Class<T> clazz) {
    if (!registeredClasses.contains(clazz)) {
      throw new CreateException(clazz.getSimpleName() + " is not registered in the container.");
    }

    return List.of();
  }*/

  private List<Object> getParameters(Class<?> clazz, Constructor<?> constructor) {
    guard.start(clazz);

    List<Object> params = Arrays.stream(constructor.getParameters())
            .map(annotation -> Resolver.resolveRealClass(annotation, registeredClasses))
            .map(this::getInstance)
            .collect(Collectors.toList());

    guard.end();

    return params;
  }
}
