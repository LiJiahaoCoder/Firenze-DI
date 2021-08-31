import com.thoughtworks.fusheng.integration.junit5.FuShengTest;
import models.Car;

@FuShengTest
public class FirenzeDITest {
  private FContainer container;
  private Car car;

  public FirenzeDITest() {
    this.container = new FContainer();
  }

  public void initializeCar() {
    car = new Car();
  }

  public void registerCar() {
    container.register(car.getClass());
  }

  public String getRegisteredCarClass() {
    Car car = container.getInstance(Car.class);
    return car.getClass().getSimpleName();
  }
}
