package spec.model;

import javax.inject.Named;

@Named("V12")
public class V12Engine implements IEngine{
  private final String name = "V12";

  @Override
  public String getName() {
    return name;
  }
}
