package com.student.demo.service.impl;

import com.student.demo.dto.StudentRequestDTO;
import com.student.demo.dto.StudentResponseDTO;
import com.student.demo.entity.Student;
import com.student.demo.exception.DuplicateEmailException;
import com.student.demo.exception.InvalidStudentAgeException;
import com.student.demo.exception.ResourceNotFoundException;
import com.student.demo.mapper.StudentMapper;
import com.student.demo.repository.StudentRepository;
import com.student.demo.service.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class StudentServiceImpl implements StudentService {
    private  final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    public StudentServiceImpl(StudentRepository studentRepository,StudentMapper studentMapper){
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }

    public List<StudentResponseDTO> getAllStudents(){

        return studentRepository.findAll().stream()
                .map(studentMapper::toDTO)
                .toList();
    }

    @Override
    public StudentResponseDTO getStudentById(Long id) {
        return studentMapper.toDTO(studentRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Student", id)));
    }

    @Override
    @Transactional
    public StudentResponseDTO createStudent(StudentRequestDTO studentRequestDTO) {

                if (studentRepository.existsByEmail(studentRequestDTO.getEmail())) {
                    throw new DuplicateEmailException(studentRequestDTO.getEmail());
                }

                Student student = studentMapper.toEntity(studentRequestDTO);
                Student savedStudent = studentRepository.save(student);

        return studentMapper.toDTO(savedStudent);
    }

    @Override
    @Transactional
    public StudentResponseDTO updateStudent(Long id, StudentRequestDTO studentRequestDTO) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student", id));

        Student student = studentMapper.toEntity(studentRequestDTO);

        if (!existingStudent.getEmail().equals(student.getEmail())
                && studentRepository.existsByEmail(student.getEmail())) {

            throw new DuplicateEmailException(student.getEmail());
        }

        existingStudent.setFirstName(student.getFirstName());
        existingStudent.setLastName(student.getLastName());
        existingStudent.setEmail(student.getEmail());
        existingStudent.setAge(student.getAge());

        Student savedStudent = studentRepository.save(existingStudent);

        return studentMapper.toDTO(savedStudent);

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
