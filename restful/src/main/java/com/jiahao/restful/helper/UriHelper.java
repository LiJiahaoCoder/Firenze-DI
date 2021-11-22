package com.jiahao.restful.helper;

import java.util.ArrayList;
import java.util.List;

public class UriHelper {
  public static String normalize(String uri) {
    String result = uri.startsWith("/") ? uri.substring(1) : uri;
    result = result.endsWith("/") ? result.substring(0, result.length() - 1) : result;

    return result;
  }

  public static boolean isPathParam(String target) {
    return target.matches("\\{\\w+\\}");
  }

  private static String transform(String target) {
    return target.replaceAll("\\{\\w+\\}", "(\\\\w+)");
  }

  public static boolean match(String uri, String target) {
    String regex = transform(target);
    return uri.matches(regex);
  }

  public static List<String> parse(String uri, String target) {
    String[] splitUri = uri.split("/");
    String[] splitTarget = target.split("/");
    List<String> result = new ArrayList<>();

    for (int i = 0; i < splitTarget.length; ++i) {
      if (isPathParam(splitTarget[i])) {
        result.add(splitUri[i]);
      }
    }

    return result;
  }
}
