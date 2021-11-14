import server.HttpServer;

public class Main {
  public static void main(String[] args) throws Exception {
    new HttpServer(8888).run();
  }
}
