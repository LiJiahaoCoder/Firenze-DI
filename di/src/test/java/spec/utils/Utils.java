package spec.utils;

public class Utils {
  public static Class<?> getClassByName(String packageName, String className) throws ClassNotFoundException {
    return Class.forName(String.format("%s.%s", packageName, className));
  }

  public static Class<?> getClassByName(String className) throws ClassNotFoundException {
    return Class.forName(String.format("spec.model.%s", className));
  }
}
