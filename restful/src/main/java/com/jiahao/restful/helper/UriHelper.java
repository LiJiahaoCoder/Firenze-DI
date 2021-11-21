package com.jiahao.restful.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class UriHelper {
  public static String normalize(String uri) {
    String result = uri.startsWith("/") ? uri.substring(1) : uri;
    result = result.endsWith("/") ? result.substring(0, result.length() - 1) : result;

    return result;
  }

  public static boolean match(String uri, String target) {
    String regex = target.replaceAll("\\{\\w+\\}", "([^/]+)");
    return uri.matches(regex);
  }

  public static List<String> parse(String uri, String target) {
    return List.of("");
  }
}
