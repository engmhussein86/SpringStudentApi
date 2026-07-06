package com.student.demo.service;

import com.student.demo.entity.Student;
import java.util.List;
import java.util.Optional;

public interface StudentService {

     List<Student> getAllStudents();
     Student getStudentById(Long id);
     Student createStudent(Student student);
     Student updateStudent(Long id, Student student);
     void deleteStudent(Long id);

}
