package com.jiahao.di.utils;

import com.google.common.collect.Iterables;
import com.jiahao.di.exceptions.CreateException;

import javax.inject.Qualifier;
import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class Resolver {
  public static Class<?> resolveClass(Parameter parameter, Collection<Class<?>> classes) {
    Class<?> type = parameter.getType();

    if (!type.isInterface()) {
      return type;
    }

    return Arrays.stream(parameter.getDeclaredAnnotations())
            .filter(annotation -> nonNull(annotation.annotationType().getAnnotation(Qualifier.class)))
            .map(annotation -> getMatchedClass(annotation, classes))
            .findFirst()
            .orElseThrow(() -> new CreateException("No qualified implement for interface " + parameter.getClass().getSimpleName()));

  }

  private static Class<?> getMatchedClass(Annotation annotation, Collection<Class<?>> classes) {

    List<Class<?>> matchedClasses = Utils.getClassByAnnotation(annotation, classes, true);

    if (matchedClasses.isEmpty()) {
      throw new CreateException("No proper class found with qualifier @" + annotation.annotationType().getSimpleName());
    }

    if (matchedClasses.size() > 1) {
      throw new CreateException(String.format("More than one qualified class found for qualifier @%s:\n%s",
              annotation.annotationType().getSimpleName(),
              matchedClasses.stream()
                      .map(Class::getName)
                      .collect(Collectors.joining("\n"))
      ));
    }

    return Iterables.getLast(matchedClasses);
  }
}
