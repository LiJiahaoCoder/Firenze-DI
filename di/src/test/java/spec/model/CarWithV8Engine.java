package spec.model;

import javax.inject.Inject;

public class CarWithV8Engine {
  private IEngine engine;

  @Inject
  public CarWithV8Engine(@V8 IEngine engine) {
    this.engine = engine;
  }

  public IEngine getEngine() {
    return engine;
  }
}
