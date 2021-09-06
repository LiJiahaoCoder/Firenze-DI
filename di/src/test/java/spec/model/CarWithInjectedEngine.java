package spec.model;

import javax.inject.Inject;

public class CarWithInjectedEngine {
  private Engine engine;

  @Inject
  public CarWithInjectedEngine(Engine engine) {
    this.engine = engine;
  }

  public Engine getEngine() {
    return engine;
  }
}
