package com.jiahao.restful.util;

import com.google.gson.Gson;
import com.jiahao.restful.server.Response;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.util.ArrayList;
import java.util.List;

public class Utils {
  public static List<Class<?>> getAllClasses(Class<?> entry) {

    Reflections reflections = new Reflections(entry.getPackage().getName(), new SubTypesScanner(false));

    return new ArrayList<>(reflections.getSubTypesOf(Object.class));

  }

  public static String serialize(Response responseData) {

    return new Gson().toJson(responseData);

  }
}
