package main;

import com.jiahao.restful.FRestfulApp;
import com.jiahao.restful.annotation.RestfulEntry;

public class Main {
  @RestfulEntry
  public static void main(String[] args) throws Exception {
    new FRestfulApp().start(Main.class);
  }
}
