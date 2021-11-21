package main.model;

public class Student {
  private final String id;
  private final String name;

  public Student(String id, String name) {
    this.id = id;
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public String getId() {
    return id;
  }
}
