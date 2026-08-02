package com.student.demo.service.impl;

import com.student.demo.entity.Student;
import com.student.demo.exception.DuplicateEmailException;
import com.student.demo.exception.InvalidStudentAgeException;
import com.student.demo.exception.ResourceNotFoundException;
import com.student.demo.repository.StudentRepository;
import com.student.demo.service.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
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
    @Transactional
    public Student createStudent(Student student) {

                if (studentRepository.existsByEmail(student.getEmail())) {
                    throw new DuplicateEmailException(student.getEmail());
                }

        return studentRepository.save(student);
    }

    @Override
    @Transactional
    public Student updateStudent(Long id, Student student) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student", id));

        if (!existingStudent.getEmail().equals(student.getEmail())
                && studentRepository.existsByEmail(student.getEmail())) {

            throw new DuplicateEmailException(student.getEmail());
        }

        existingStudent.setFirstName(student.getFirstName());
        existingStudent.setLastName(student.getLastName());
        existingStudent.setEmail(student.getEmail());
        existingStudent.setAge(student.getAge());

        return studentRepository.save(existingStudent);

    }

    @Override
    @Transactional
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student", id));

        studentRepository.delete(student);

    }
}
