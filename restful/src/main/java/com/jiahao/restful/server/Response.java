package com.jiahao.restful.server;

import io.netty.handler.codec.http.HttpResponseStatus;

public class Response {
  public Object data;
  public Number status;
  public String errorMessage;

  public Response(Object data, HttpResponseStatus status) {
    this.data = data;
    this.status = status.code();
  }

  public Response(String errorMessage, HttpResponseStatus status) {
    this.status = status.code();
    this.errorMessage = errorMessage;
  }
}
