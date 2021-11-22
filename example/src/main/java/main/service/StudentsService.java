package main.service;

import main.model.Student;

import javax.inject.Singleton;
import java.util.ArrayList;

@Singleton
public class StudentsService {
  private final ArrayList<Student> students;

  public StudentsService() {
    this.students = new ArrayList<Student>();
    initializeStudents();
  }

  private void initializeStudents() {
    students.add(new Student("1", "Bill"));
    students.add(new Student("2", "George"));
  }

  public ArrayList<Student> getStudents() {
    return this.students;
  }

  public Student getStudent(Integer id) {
    return this.students.get(id);
  }

  public void addStudent(Student student) {
    this.students.add(student);
  }
}
