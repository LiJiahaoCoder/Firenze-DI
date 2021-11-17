package com.jiahao.di.utils;

import com.jiahao.di.exceptions.CreateException;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class Utils {
  public static List<Class<?>> getAllClasses(Class<?> entry) {

    Reflections reflections = new Reflections(entry.getPackage().getName(), new SubTypesScanner(false));

    return new ArrayList<>(reflections.getSubTypesOf(Object.class));

  }

  public static boolean compareAnnotation(Annotation left, Annotation right) throws InvocationTargetException, IllegalAccessException {

    if (left.getClass() != right.getClass()) {
      return false;
    }

    for (Method method : left.annotationType().getDeclaredMethods()) {

      Object leftValue = method.invoke(left);
      Object rightValue = method.invoke(right);

      if (leftValue == rightValue) {
        continue;
      } else if (leftValue == null) {
        return false;
      }

      if (!leftValue.equals(rightValue)) {
        return false;
      }
    }
    return true;
  }

  public static Optional<Constructor<?>> getConstructorByAnnotation(Class<?> clazz, Class<?> annotationClass) {
    return Arrays.stream(clazz.getDeclaredConstructors())
            .filter(
                    constructor -> Arrays.stream(constructor.getDeclaredAnnotations())
                            .anyMatch(annotation -> annotation.annotationType().equals(annotationClass))
            )
            .findFirst()
            .map(constructor -> {
              constructor.setAccessible(true);
              return constructor;
            });
  }

  public static Constructor<?> getConstructorWithoutArgs(Class<?> clazz) {
    try {
      return clazz.getConstructor();
    } catch (NoSuchMethodException e) {
      throw new CreateException("Cannot find constructor in " + clazz.getSimpleName());
    }
  }

  public static List<Class<?>> getClassByAnnotation(Annotation annotation, Collection<Class<?>> classes, boolean match) {
    return classes
            .stream()
            .filter(clazz -> isClassMatchWithAnnotation(clazz, annotation, match))
            .collect(Collectors.toList());
  }

  private static boolean isClassMatchWithAnnotation(Class<?> clz, Annotation annotation, boolean strictMatch) {
    Annotation declaredAnnotation = clz.getDeclaredAnnotation(annotation.annotationType());
    if (Objects.isNull(declaredAnnotation)) {
      return false;
    }

    try {
      return strictMatch ? Utils.compareAnnotation(declaredAnnotation, annotation) :
              declaredAnnotation.annotationType().equals(annotation.annotationType());
    } catch (Exception e) {
      return false;
    }
  }
}
