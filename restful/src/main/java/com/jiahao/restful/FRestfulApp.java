package com.jiahao.restful;

import com.jiahao.di.annotation.DIEntry;
import com.jiahao.restful.annotation.RestfulEntry;
import com.jiahao.restful.util.Utils;
import io.netty.handler.codec.http.HttpMethod;
import org.javatuples.Triplet;

import javax.ws.rs.Path;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FRestfulApp {
  @DIEntry
  public void start(Class<?> entry) throws Exception {
    DIContainer.initContainer(entry);
    registerResource(entry);
    List<Method> methods = Arrays.stream(entry.getMethods())
            .filter(method -> Objects.nonNull(method.getDeclaredAnnotation(RestfulEntry.class)))
            .collect(Collectors.toList());

    if (methods.isEmpty()) {
      throw new RuntimeException("No @RestfulEntry");
    } else if (methods.size() > 1) {
      throw new RuntimeException("Can only exist 1 @RestfulEntry");
    }

    createServer().run();
  }

  private void registerResource(Class entry) {
    UriTree uriTree = UriTree.getInstance();
    List<Class<?>> classes = Utils.getAllClasses(entry)
            .stream()
            .filter(clz -> clz.isAnnotationPresent(Path.class))
            .collect(Collectors.toList());
    for (Class<?> clz : classes) {
      uriTree.add(new Triplet<String, HttpMethod, Class<?>>(clz.getAnnotation(Path.class).value(), HttpMethod.GET, clz));
    }
  }

  private static FRestfulServer createServer() {
    return new FRestfulServer();
  }
}
