package spec.model;

@V8
public class V8Engine implements IEngine{
  private final String name = "V8";

  @Override
  public String getName() {
    return name;
  }
}
