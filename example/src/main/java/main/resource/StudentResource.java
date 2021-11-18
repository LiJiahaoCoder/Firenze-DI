package main.resource;

import main.model.Student;

import javax.ws.rs.GET;

public class StudentResource {
  private final Student student;

  public StudentResource(Student student) {
    this.student = student;
  }

  @GET
  public Student getStudent() {
    return student;
  }
}
