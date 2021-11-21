package com.jiahao.restful.helper;

public class UriResolver {
  public static String resolve(String uri) {
    String result = uri.startsWith("/") ? uri.substring(1) : uri;
    result = result.endsWith("/") ? result.substring(0, result.length() - 1) : result;

    return result;
  }
}
