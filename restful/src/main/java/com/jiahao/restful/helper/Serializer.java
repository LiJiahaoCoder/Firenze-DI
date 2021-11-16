package com.jiahao.restful.helper;

import com.google.gson.Gson;
import com.jiahao.restful.server.Response;

public class Serializer {
  public static String serialize(Response responseData) {
    Gson gson = new Gson();
    return gson.toJson(responseData);
  }
}
