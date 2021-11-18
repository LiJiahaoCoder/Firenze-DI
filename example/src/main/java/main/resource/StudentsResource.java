package main.resource;

import main.model.Student;
import main.service.StudentsService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
    studentsService.addStudent(new Student("Bill"));
    studentsService.addStudent(new Student("George"));
  }

  @GET
  public List<Student> getStudents() {
    return studentsService.getStudents();
  }

  @Path("{id}")
  public StudentResource findStudent(@PathParam("id") Integer id) {
    return new StudentResource(studentsService.getStudent(id));
  }
}
