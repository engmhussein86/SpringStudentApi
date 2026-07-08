package com.student.demo.service.impl;

import com.student.demo.entity.Student;
import com.student.demo.exception.DuplicateEmailException;
import com.student.demo.exception.ResourceNotFoundException;
import com.student.demo.repository.StudentRepository;
import com.student.demo.service.StudentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    private  final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository){
        this.studentRepository = studentRepository;
    }

    public List<Student> getAllStudents(){
        return studentRepository.findAll();
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Student", id));
    }

    @Override
    public Student createStudent(Student student) {
        if(studentRepository.existsByEmail(student.getEmail())){
            throw new DuplicateEmailException(student.getEmail());
        }
        return studentRepository.save(student);
    }

    @Override
    public Student updateStudent(Long id, Student student) {
        return null;
    }

    @Override
    public void deleteStudent(Long id) {

    }
}
