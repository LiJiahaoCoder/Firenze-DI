package spec.model;

import javax.inject.Inject;
import javax.inject.Named;

public class CarWithV12Engine {
  private IEngine engine;

  @Inject
  public CarWithV12Engine(@Named("V12") IEngine engine) {
    this.engine = engine;
  }

  public IEngine getEngine() {
    return engine;
  }
}
