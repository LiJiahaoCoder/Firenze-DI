import exceptions.CreateException;
import utils.Utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FContainer {
  private final Set<Class<?>> registeredClasses = new HashSet<>();

  public void register(Class<?> clazz) {
    registeredClasses.add(clazz);
  }

  public <T> T getInstance(Class<T> clazz) {
    if (!registeredClasses.contains(clazz)) {
      throw new CreateException(clazz.getSimpleName() + " is not registered in the container.");
    }

    try {
      return clazz.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new CreateException("Failed to create instance of " + clazz.getClass().getSimpleName());
    }
  }

  public <T> List<T> getInstances(Class<T> clazz) {
    if (!registeredClasses.contains(clazz)) {
      throw new CreateException(clazz.getSimpleName() + " is not registered in the container.");
    }

    return List.of();
  }
}
