package main.resource;

import main.model.Student;
import main.service.StudentsService;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.List;

@Path("students")
public class StudentsResource {
  private final StudentsService studentsService;

  @Inject
  public StudentsResource(StudentsService studentsService) {
    this.studentsService = studentsService;
    initializeStudents();
  }

  private void initializeStudents() {
    studentsService.addStudent(new Student("1", "Bill"));
    studentsService.addStudent(new Student("2", "George"));
  }

  @GET
  public List<Student> getStudents() {
    return studentsService.getStudents();
  }

  @POST
  public void addStudent(@BeanParam Student student) {
    studentsService.addStudent(student);
  }

  @Path("{id}")
  public StudentResource findStudent(@PathParam("id") Integer id) {
    return new StudentResource(studentsService.getStudent(id));
  }
}
