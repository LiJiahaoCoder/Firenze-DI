package com.jiahao.restful.helper;

import com.jiahao.restful.UriTree;
import com.jiahao.restful.util.Utils;
import org.javatuples.Quartet;
import org.javatuples.Triplet;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

public class Register {

  public void register(Class<?> entry) {
    registerResource(Utils.getAllClasses(entry));
  }

  private void registerResource(List<Class<?>> classes) {
    classes.stream()
            .filter(clz -> clz.isAnnotationPresent(Path.class))
            .forEach(this::registerMethod);
  }

  private void registerMethod(Class<?> resource) {
    String path = resource.getAnnotation(Path.class).value();
    UriTree uriTree = UriTree.getInstance();
    Method[] methods = resource.getMethods();

    for (Method method : methods) {
      Annotation[] annotations = method.getAnnotations();

      for (Annotation annotation : annotations) {

        if (isHttpMethodAnnotation(annotation)) {

          Quartet<String, Class<? extends Annotation>, Method, Class<?>> quartet = new Quartet<>(path, annotation.annotationType(), method, resource);
          uriTree.add(quartet);
          break;

        }

      }

    }

  }

  private boolean isHttpMethodAnnotation(Annotation annotation) {
    return GET.class.equals(annotation.annotationType()) ||
            POST.class.equals(annotation.annotationType());
  }

}
