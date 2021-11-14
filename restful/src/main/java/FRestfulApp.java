import server.HttpServer;

public class FRestfulApp {
  public static void main(String[] args) throws Exception {
    new HttpServer(8888).run();
  }
}
