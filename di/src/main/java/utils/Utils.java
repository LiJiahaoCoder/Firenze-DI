package utils;

import com.google.common.reflect.ClassPath;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {
  public static List<String> getAllClasses(Package p) {
    ClassPath classPath = null;

    try {
      classPath = ClassPath.from(ClassLoader.getSystemClassLoader());
    } catch (IOException e) {
      e.printStackTrace();
    }

    return classPath.getAllClasses()
            .stream()
            .map(ClassPath.ClassInfo::getName)
            .filter(className -> className.startsWith(p.getName()))
            .collect(Collectors.toList());
  }
}
