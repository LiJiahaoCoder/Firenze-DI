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

  public Response(Number status, String errorMessage) {
    this.status = status;
    this.errorMessage = errorMessage;
  }
}
