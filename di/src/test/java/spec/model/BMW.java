package spec.model;

public class BMW implements ICar {
  private static final String name = "BMW";

  @Override
  public String getName() {
    return name;
  }
}
