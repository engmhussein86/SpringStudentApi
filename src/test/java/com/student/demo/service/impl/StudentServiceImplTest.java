package com.student.demo.service.impl;

import com.student.demo.dto.StudentRequestDTO;
import com.student.demo.dto.StudentResponseDTO;
import com.student.demo.entity.Student;
import com.student.demo.exception.DuplicateEmailException;
import com.student.demo.exception.ResourceNotFoundException;
import com.student.demo.mapper.StudentMapper;
import com.student.demo.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;


    private StudentMapper studentMapper;


    private StudentServiceImpl studentService;


    @BeforeEach
    void setUp() {

        studentMapper = new StudentMapper();

        studentService =
                new StudentServiceImpl(
                        studentRepository,
                        studentMapper
                );
    }

    @Test
    void createStudent_shouldCreateStudentSuccessfully() {

        StudentRequestDTO request = new StudentRequestDTO();

        request.setFirstName("Ali");
        request.setLastName("Ahmad");
        request.setEmail("ali@test.com");
        request.setAge(20);

        Student savedStudent = new Student();

        savedStudent.setId(1L);
        savedStudent.setFirstName("Ali");
        savedStudent.setLastName("Ahmad");
        savedStudent.setEmail("ali@test.com");
        savedStudent.setAge(20);

        when(studentRepository.existsByEmail("ali@test.com"))
                .thenReturn(false);

        when(studentRepository.save(any(Student.class)))
                .thenReturn(savedStudent);

        StudentResponseDTO result =
                studentService.createStudent(request);

        assertNotNull(result);

        assertEquals(savedStudent.getId(), result.getId());
        assertEquals(savedStudent.getFirstName(), result.getFirstName());
        assertEquals(savedStudent.getLastName(), result.getLastName());
        assertEquals(savedStudent.getEmail(), result.getEmail());
        assertEquals(savedStudent.getAge(), result.getAge());

        verify(studentRepository).existsByEmail("ali@test.com");

        verify(studentRepository).save(any(Student.class));
    }
    @Test
    void createStudent_shouldThrowException_whenEmailExists() {

        StudentRequestDTO request = new StudentRequestDTO();

        request.setFirstName("Ali");
        request.setLastName("Ahmad");
        request.setEmail("existing@test.com");
        request.setAge(20);

        when(studentRepository.existsByEmail("existing@test.com"))
                .thenReturn(true);

        DuplicateEmailException exception =
                assertThrows(
                        DuplicateEmailException.class,
                        () -> studentService.createStudent(request)
                );

        assertNotNull(exception);

        verify(studentRepository)
                .existsByEmail("existing@test.com");

        verify(studentRepository, never())
                .save(any(Student.class));
    }

    @Test
    void getStudentById_shouldReturnStudentSuccessfully() {

        Student student = new Student();

        student.setId(1L);
        student.setFirstName("Ali");
        student.setLastName("Ahmad");
        student.setEmail("ali@test.com");
        student.setAge(20);

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        StudentResponseDTO result =
                studentService.getStudentById(1L);

        assertNotNull(result);

        assertEquals(1L, result.getId());
        assertEquals("Ali", result.getFirstName());
        assertEquals("Ahmad", result.getLastName());
        assertEquals("ali@test.com", result.getEmail());
        assertEquals(20, result.getAge());

        verify(studentRepository).findById(1L);
    }


    @Test
    void getStudentById_shouldThrowException_whenStudentNotFound() {

        // Arrange
        when(studentRepository.findById(1L))
                .thenReturn(Optional.empty());


        // Act + Assert
        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> studentService.getStudentById(1L)
                );


        // Assert exception message
        assertEquals(
                "Student with id : 1 was not found.",
                exception.getMessage()
        );


        // Verify
        verify(studentRepository).findById(1L);
    }

        @Test
        void updateStudent_shouldUpdateStudent(){

        }


        @Test
        void patchStudent_shouldUpdateOnlyProvidedFields(){

        }


        @Test
        void deleteStudent_shouldDeleteStudent(){

        }


}
