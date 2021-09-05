import com.thoughtworks.fusheng.integration.junit5.FuShengTest;
import exceptions.CreateException;
import model.*;

import java.util.List;

@FuShengTest
public class FirenzeDITest {
  private FContainer container;
  private List<ICar> carsList;


  public FirenzeDITest() {
    this.container = new FContainer();
  }

  public void registerCar() {
    container.register(Car.class);
  }

  public String getRegisteredCarClass() {
    Car car = container.getInstance(Car.class);
    return car.getClass().getSimpleName();
  }

  public String getUnregisteredBikeClass() {
    try {
      Bike bike = container.getInstance(Bike.class);
      return bike.getClass().getSimpleName();
    } catch (CreateException e) {
      return e.getMessage();
    }
  }

  public void initializeICar() {
    container.register(ICar.class);
    carsList = container.getInstances(ICar.class);
  }

  public int getInstancesCountOfICar() {
    return carsList.size();
  }

  public boolean containsClassInCarsList(String className) {
    return carsList.stream()
            .map(car -> car.getName())
            .equals(className);
  }
}
