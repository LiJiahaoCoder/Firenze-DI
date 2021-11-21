package com.jiahao.restful.helper;

import com.jiahao.restful.UriTable;
import com.jiahao.restful.util.Utils;
import org.javatuples.Quartet;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

public class Register {

  private final static List<Class<? extends Annotation>> httpAnnotations = List.of(GET.class, POST.class, Path.class);

  public void register(Class<?> entry) {
    registerResource(Utils.getAllClasses(entry), "", false);
  }

  private void registerResource(List<Class<?>> classes, String basePath, boolean isSubResource) {
    classes.stream()
            .filter(clz -> isSubResource ||clz.isAnnotationPresent(Path.class))
            .forEach(clz -> registerMethod(clz, basePath));
  }

  private void registerMethod(Class<?> resource, String basePath) {
    Path pathAnnotation = resource.getAnnotation(Path.class);
    String path = Objects.isNull(pathAnnotation) ? basePath : getSubPath(basePath, pathAnnotation);
    UriTable uriTable = UriTable.getUriTable();
    Method[] methods = resource.getMethods();

    for (Method method : methods) {
      Annotation[] annotations = method.getAnnotations();

      for (Annotation annotation : annotations) {

        if (isHttpMethodAnnotation(annotation)) {

          if (Path.class.equals(annotation.annotationType())) {
            registerResource(List.of(method.getReturnType()), getSubPath(path, method.getAnnotation(Path.class)), true);
          } else {
            Quartet<String, Class<? extends Annotation>, Method, Class<?>> quartet = new Quartet<>(
                    UriResolver.resolve(path),
                    annotation.annotationType(),
                    method,
                    resource
            );

            uriTable.add(quartet);
          }

          break;

        }

      }

    }

  }

  private boolean isHttpMethodAnnotation(Annotation annotation) {
    return httpAnnotations.contains(annotation.annotationType());
  }

  private String getSubPath(String path, Path annotation) {
    return UriResolver.resolve(path + "/" + UriResolver.resolve(annotation.value()));
  }

}
