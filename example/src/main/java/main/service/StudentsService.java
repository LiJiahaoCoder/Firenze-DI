package main.service;

import main.model.Student;

import java.util.ArrayList;

public class StudentsService {
  private final ArrayList<Student> students;

  public StudentsService() {
    this.students = new ArrayList<Student>();
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
