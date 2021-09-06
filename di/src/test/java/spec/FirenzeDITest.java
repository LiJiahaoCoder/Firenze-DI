package spec;

import com.jiahao.di.FContainer;
import com.thoughtworks.fusheng.integration.junit5.FuShengTest;
import spec.model.CarWithInjectedEngine;
import spec.model.Engine;
import spec.utils.Utils;

@FuShengTest
public class FirenzeDITest {
  private FContainer container;

  public FirenzeDITest() {
    this.container = new FContainer();
  }

  public void registerClass(String className) throws ClassNotFoundException {
    container.register(Utils.getClassByName(className));
  }

  public Object getInstance(String className) {
    try {
      return container.getInstance(Utils.getClassByName(className));
    } catch (Exception e) {
      return e.getMessage();
    }
  }

  public String getClassName(String className) {
    return getInstance(className).getClass().getSimpleName();
  }

  public String getInjectedEngineClassName() {
    CarWithInjectedEngine car = container.getInstance(CarWithInjectedEngine.class);
    return car.getEngine().getClass().getSimpleName();
  }
}
