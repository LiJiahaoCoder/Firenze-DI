package spec.model;

public class Benz implements ICar {
  private static final String name = "Benz";

  @Override
  public String getName() {
    return name;
  }
}
