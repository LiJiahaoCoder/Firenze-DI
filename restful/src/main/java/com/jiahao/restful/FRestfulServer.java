package com.jiahao.restful;

import com.jiahao.restful.server.HttpServer;

public class FRestfulServer {
  public void run() throws Exception {
    new HttpServer(8888).run();
  }
}
