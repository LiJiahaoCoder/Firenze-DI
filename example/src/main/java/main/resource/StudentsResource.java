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
  public StudentResource findStudent(@PathParam("id") String id) {
    return new StudentResource(studentsService.getStudent(id));
  }
}
