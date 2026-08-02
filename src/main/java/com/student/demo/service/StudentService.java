package com.student.demo.service;

import com.student.demo.dto.StudentRequestDTO;
import com.student.demo.dto.StudentResponseDTO;
import com.student.demo.entity.Student;
import java.util.List;
import java.util.Optional;

public interface StudentService {

     List<Student> getAllStudents();
     Student getStudentById(Long id);
     StudentResponseDTO  createStudent(StudentRequestDTO student);
     Student updateStudent(Long id, Student student);
     void deleteStudent(Long id);

}
