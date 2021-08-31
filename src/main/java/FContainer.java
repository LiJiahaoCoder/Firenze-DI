import java.util.HashSet;
import java.util.Set;

public class FContainer {
  private final Set<Class<?>> registeredClasses = new HashSet<>();

  public void register(Class<?> clazz) {
    registeredClasses.add(clazz);
  }

  public <T> T getInstance(Class<T> clazz) {
    try {
      return clazz.newInstance();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }

    return null;
  }
}
