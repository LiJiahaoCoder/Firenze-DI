package com.jiahao.di.utils;

import com.google.common.reflect.ClassPath;
import com.jiahao.di.exceptions.CreateException;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class Utils {
  public static List<String> getAllClasses(Package p) {
    ClassPath classPath = null;

    try {
      classPath = ClassPath.from(ClassLoader.getSystemClassLoader());
    } catch (IOException e) {
      e.printStackTrace();
    }

    return classPath.getAllClasses()
            .stream()
            .map(ClassPath.ClassInfo::getName)
            .filter(className -> className.startsWith(p.getName()))
            .collect(Collectors.toList());
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
