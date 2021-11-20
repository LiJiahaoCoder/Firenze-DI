package com.jiahao.restful;

import com.jiahao.restful.helper.Register;

public class FRestfulApp {
  private final Register register;

  public FRestfulApp() {
    this.register = new Register();
  }

  public void start(Class<?> entry) throws Exception {
    DIContainer.initContainer(entry);
    register(entry);
    createServer().run();
  }

  private void register(Class<?> entry) {
    register.register(entry);
  }

  private static FRestfulServer createServer() {
    return new FRestfulServer();
  }
}
