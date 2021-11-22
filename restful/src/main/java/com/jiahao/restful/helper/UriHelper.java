package com.jiahao.restful.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UriHelper {
  public static String normalize(String uri) {
    String result = uri.startsWith("/") ? uri.substring(1) : uri;
    result = result.endsWith("/") ? result.substring(0, result.length() - 1) : result;

    return result;
  }

  private static boolean isPathParam(String target) {
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
