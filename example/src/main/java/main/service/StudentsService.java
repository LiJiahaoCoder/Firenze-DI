package main.service;

import main.model.Student;

import javax.inject.Singleton;
import java.util.ArrayList;

@Singleton
public class StudentsService {
  private final ArrayList<Student> students;

  public StudentsService() {
    this.students = new ArrayList<>();
    initializeStudents();
  }

  private void initializeStudents() {
    students.add(new Student("1", "Bill"));
    students.add(new Student("2", "George"));
  }

  public ArrayList<Student> getStudents() {
    return this.students;
  }

  public Student getStudent(String id) {
    return this.students.stream()
            .filter(student -> student.getId().equals(id))
            .findFirst()
            .get();
  }

  public void addStudent(Student student) {
    this.students.add(student);
  }
}
