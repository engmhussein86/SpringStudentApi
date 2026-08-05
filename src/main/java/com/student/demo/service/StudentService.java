package com.student.demo.service;

import com.student.demo.dto.StudentRequestDTO;
import com.student.demo.dto.StudentResponseDTO;
import com.student.demo.entity.Student;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface StudentService {

     Page<StudentResponseDTO> getAllStudents(int page,
                                             int size,
                                             String sortBy,
                                             String direction);
     StudentResponseDTO getStudentById(Long id);
     StudentResponseDTO  createStudent(StudentRequestDTO student);
     StudentResponseDTO updateStudent(Long id, StudentRequestDTO student);
     void deleteStudent(Long id);

}
