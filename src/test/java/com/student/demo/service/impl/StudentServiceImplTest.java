package com.student.demo.service.impl;

import com.student.demo.dto.StudentPatchRequestDTO;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;


    @Mock
    private StudentMapper studentMapper;

@InjectMocks
    private StudentServiceImpl studentService;

    @Test
    void createStudent_shouldCreateStudentSuccessfully() {

        StudentRequestDTO request = new StudentRequestDTO();

        request.setFirstName("Ali");
        request.setLastName("Ahmad");
        request.setEmail("ali@test.com");
        request.setAge(20);

        Student student = new Student();

        student.setFirstName("Ali");
        student.setLastName("Ahmad");
        student.setEmail("ali@test.com");
        student.setAge(20);

        Student savedStudent = new Student();

        savedStudent.setId(1L);
        savedStudent.setFirstName("Ali");
        savedStudent.setLastName("Ahmad");
        savedStudent.setEmail("ali@test.com");
        savedStudent.setAge(20);

        StudentResponseDTO response = new StudentResponseDTO();

        response.setId(1L);
        response.setFirstName("Ali");
        response.setLastName("Ahmad");
        response.setEmail("ali@test.com");
        response.setAge(20);

        when(studentRepository.existsByEmail("ali@test.com"))
                .thenReturn(false);

        when(studentMapper.toEntity(request))
                .thenReturn(student);

        when(studentRepository.save(any(Student.class)))
                .thenReturn(savedStudent);

        when(studentMapper.toDTO(savedStudent))
                .thenReturn(response);

        StudentResponseDTO result =
                studentService.createStudent(request);

        assertNotNull(result);

        assertEquals(savedStudent.getId(), result.getId());
        assertEquals(savedStudent.getFirstName(), result.getFirstName());
        assertEquals(savedStudent.getLastName(), result.getLastName());
        assertEquals(savedStudent.getEmail(), result.getEmail());
        assertEquals(savedStudent.getAge(), result.getAge());

        verify(studentRepository).existsByEmail("ali@test.com");

        verify(studentRepository)
                .existsByEmail("ali@test.com");

        verify(studentMapper)
                .toEntity(request);

        verify(studentRepository)
                .save(student);

        verify(studentMapper)
                .toDTO(savedStudent);
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

        // Arrange

        Student student = new Student();

        student.setId(1L);
        student.setFirstName("Ali");
        student.setLastName("Ahmad");
        student.setEmail("ali@test.com");
        student.setAge(20);


        StudentResponseDTO response = new StudentResponseDTO();

        response.setId(1L);
        response.setFirstName("Ali");
        response.setLastName("Ahmad");
        response.setEmail("ali@test.com");
        response.setAge(20);


        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        when(studentMapper.toDTO(student))
                .thenReturn(response);


        // Act

        StudentResponseDTO result =
                studentService.getStudentById(1L);


        // Assert

        assertNotNull(result);

        assertEquals(1L, result.getId());
        assertEquals("Ali", result.getFirstName());
        assertEquals("Ahmad", result.getLastName());
        assertEquals("ali@test.com", result.getEmail());
        assertEquals(20, result.getAge());


        // Verify

        verify(studentRepository)
                .findById(1L);

        verify(studentMapper)
                .toDTO(student);
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
    void updateStudent_shouldUpdateStudentSuccessfully() {

        // Arrange
        Student existingStudent = new Student();
        existingStudent.setId(1L);
        existingStudent.setFirstName("Ali");
        existingStudent.setLastName("Ahmad");
        existingStudent.setEmail("old@test.com");
        existingStudent.setAge(20);

        StudentRequestDTO request = new StudentRequestDTO();
        request.setFirstName("Omar");
        request.setLastName("Hassan");
        request.setEmail("new@test.com");
        request.setAge(25);

        StudentResponseDTO response = new StudentResponseDTO();
        response.setId(1L);
        response.setFirstName("Omar");
        response.setLastName("Hassan");
        response.setEmail("new@test.com");
        response.setAge(25);

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(existingStudent));

        when(studentRepository.existsByEmail("new@test.com"))
                .thenReturn(false);

        when(studentRepository.save(existingStudent))
                .thenReturn(existingStudent);

        when(studentMapper.toDTO(existingStudent))
                .thenReturn(response);


        // Act
        StudentResponseDTO result =
                studentService.updateStudent(1L, request);


        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Omar", result.getFirstName());
        assertEquals("Hassan", result.getLastName());
        assertEquals("new@test.com", result.getEmail());
        assertEquals(25, result.getAge());


        // Verify
        verify(studentRepository).findById(1L);

        verify(studentRepository)
                .existsByEmail("new@test.com");

        verify(studentMapper)
                .updateStudentFromDto(request, existingStudent);

        verify(studentRepository)
                .save(existingStudent);

        verify(studentMapper)
                .toDTO(existingStudent);
    }

    @Test
    void updateStudent_shouldThrowException_whenStudentNotFound() {

        // Arrange

        StudentRequestDTO request = new StudentRequestDTO();

        request.setFirstName("Omar");
        request.setLastName("Hassan");
        request.setEmail("new@test.com");
        request.setAge(25);

        when(studentRepository.findById(99L))
                .thenReturn(Optional.empty());


        // Act + Assert

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> studentService.updateStudent(99L, request)
                );


        // Assert exception message

        assertEquals(
                "Student with id : 99 was not found.",
                exception.getMessage()
        );


        // Verify

        verify(studentRepository)
                .findById(99L);

        verify(studentRepository, never())
                .existsByEmail(anyString());

        verify(studentMapper, never())
                .updateStudentFromDto(any(StudentRequestDTO.class), any(Student.class));

        verify(studentRepository, never())
                .save(any(Student.class));

        verify(studentMapper, never())
                .toDTO(any(Student.class));
    }

    @Test
    void updateStudent_shouldThrowException_whenNewEmailAlreadyExists() {

        // Arrange

        Student existingStudent = new Student();

        existingStudent.setId(1L);
        existingStudent.setFirstName("Ali");
        existingStudent.setLastName("Ahmad");
        existingStudent.setEmail("old@test.com");
        existingStudent.setAge(20);


        StudentRequestDTO request = new StudentRequestDTO();

        request.setFirstName("Omar");
        request.setLastName("Hassan");
        request.setEmail("existing@test.com");
        request.setAge(25);


        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(existingStudent));

        when(studentRepository.existsByEmail("existing@test.com"))
                .thenReturn(true);


        // Act + Assert

        DuplicateEmailException exception =
                assertThrows(
                        DuplicateEmailException.class,
                        () -> studentService.updateStudent(1L, request)
                );


        // Assert exception message

        assertEquals(
                "Email 'existing@test.com' already exists.",
                exception.getMessage()
        );


        // Verify

        verify(studentRepository)
                .findById(1L);

        verify(studentRepository)
                .existsByEmail("existing@test.com");


        verify(studentMapper, never())
                .updateStudentFromDto(
                        any(StudentRequestDTO.class),
                        any(Student.class)
                );

        verify(studentRepository, never())
                .save(any(Student.class));

        verify(studentMapper, never())
                .toDTO(any(Student.class));
    }

    @Test
    void updateStudent_shouldUpdateSuccessfully_whenEmailRemainsSame() {

        // Arrange

        Student existingStudent = new Student();

        existingStudent.setId(1L);
        existingStudent.setFirstName("Ali");
        existingStudent.setLastName("Ahmad");
        existingStudent.setEmail("old@test.com");
        existingStudent.setAge(20);


        StudentRequestDTO request = new StudentRequestDTO();

        request.setFirstName("Omar");
        request.setLastName("Hassan");
        request.setEmail("old@test.com");
        request.setAge(25);


        StudentResponseDTO response = new StudentResponseDTO();

        response.setId(1L);
        response.setFirstName("Omar");
        response.setLastName("Hassan");
        response.setEmail("old@test.com");
        response.setAge(25);


        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(existingStudent));

        when(studentRepository.save(existingStudent))
                .thenReturn(existingStudent);

        when(studentMapper.toDTO(existingStudent))
                .thenReturn(response);


        // Act

        StudentResponseDTO result =
                studentService.updateStudent(1L, request);


        // Assert

        assertNotNull(result);

        assertEquals(1L, result.getId());
        assertEquals("Omar", result.getFirstName());
        assertEquals("Hassan", result.getLastName());
        assertEquals("old@test.com", result.getEmail());
        assertEquals(25, result.getAge());


        // Verify

        verify(studentRepository)
                .findById(1L);


        verify(studentRepository, never())
                .existsByEmail(anyString());


        verify(studentMapper)
                .updateStudentFromDto(request, existingStudent);


        verify(studentRepository)
                .save(existingStudent);


        verify(studentMapper)
                .toDTO(existingStudent);
    }


        @Test
        void patchStudent_shouldUpdateOnlyProvidedFields(){

        }


    @Test
    void deleteStudent_shouldDeleteStudentSuccessfully() {

        // Arrange

        Student student = new Student();

        student.setId(1L);
        student.setFirstName("Ali");
        student.setLastName("Ahmad");
        student.setEmail("ali@test.com");
        student.setAge(20);

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));


        // Act

        studentService.deleteStudent(1L);


        // Assert / Verify

        verify(studentRepository)
                .findById(1L);

        verify(studentRepository)
                .delete(student);
    }

    @Test
    void deleteStudent_shouldThrowException_whenStudentNotFound() {

        // Arrange

        when(studentRepository.findById(99L))
                .thenReturn(Optional.empty());


        // Act + Assert

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> studentService.deleteStudent(99L)
                );


        // Assert exception message

        assertEquals(
                "Student with id : 99 was not found.",
                exception.getMessage()
        );


        // Verify

        verify(studentRepository)
                .findById(99L);

        verify(studentRepository, never())
                .delete(any(Student.class));
    }

    @Test
    void getAllStudents_shouldReturnPagedStudents() {

        // Arrange

        Pageable pageable = PageRequest.of(0, 10);

        Student student = new Student();
        student.setId(1L);

        StudentResponseDTO response = new StudentResponseDTO();
        response.setId(1L);

        Page<Student> studentPage =
                new PageImpl<>(List.of(student), pageable, 1);

        when(studentRepository.findAll(pageable))
                .thenReturn(studentPage);

        when(studentMapper.toDTO(student))
                .thenReturn(response);


        // Act

        Page<StudentResponseDTO> result =
                studentService.getAllStudents(pageable);


        // Assert

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals(1L, result.getContent().get(0).getId());


        // Verify

        verify(studentRepository)
                .findAll(pageable);

        verify(studentMapper)
                .toDTO(student);
    }

    @Test
    void getAllStudents_shouldReturnEmptyPage_whenNoStudentsExist() {

        // Arrange

        Pageable pageable = PageRequest.of(0, 10);

        when(studentRepository.findAll(pageable))
                .thenReturn(Page.empty(pageable));


        // Act

        Page<StudentResponseDTO> result =
                studentService.getAllStudents(pageable);


        // Assert

        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.getTotalElements());


        // Verify

        verify(studentRepository)
                .findAll(pageable);

        verifyNoInteractions(studentMapper);
    }

    @Test
    void patchStudent_shouldUpdateOnlyProvidedField() {

        // Arrange

        Student existingStudent = new Student();

        existingStudent.setId(1L);
        existingStudent.setFirstName("Ali");
        existingStudent.setLastName("Ahmad");
        existingStudent.setEmail("ali@test.com");
        existingStudent.setAge(20);


        StudentPatchRequestDTO request =
                new StudentPatchRequestDTO();

        request.setAge(25);


        StudentResponseDTO response =
                new StudentResponseDTO();

        response.setId(1L);
        response.setFirstName("Ali");
        response.setLastName("Ahmad");
        response.setEmail("ali@test.com");
        response.setAge(25);


        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(existingStudent));

        when(studentRepository.save(existingStudent))
                .thenReturn(existingStudent);

        when(studentMapper.toDTO(existingStudent))
                .thenReturn(response);


        // Act

        StudentResponseDTO result =
                studentService.patchStudent(1L, request);


        // Assert

        assertNotNull(result);

        assertEquals(1L, result.getId());
        assertEquals("Ali", result.getFirstName());
        assertEquals("Ahmad", result.getLastName());
        assertEquals("ali@test.com", result.getEmail());
        assertEquals(25, result.getAge());


        // Verify

        verify(studentRepository)
                .findById(1L);

        verify(studentMapper)
                .updateEntity(request, existingStudent);

        verify(studentRepository)
                .save(existingStudent);

        verify(studentMapper)
                .toDTO(existingStudent);
    }


    @Test
    void patchStudent_shouldThrowException_whenStudentNotFound() {

        // Arrange

        StudentPatchRequestDTO request =
                new StudentPatchRequestDTO();

        request.setAge(25);


        when(studentRepository.findById(99L))
                .thenReturn(Optional.empty());


        // Act + Assert

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> studentService.patchStudent(99L, request)
                );


        // Assert exception message

        assertEquals(
                "Student with id : 99 was not found.",
                exception.getMessage()
        );


        // Verify

        verify(studentRepository)
                .findById(99L);

        verify(studentRepository, never())
                .existsByEmail(anyString());

        verify(studentMapper, never())
                .updateEntity(
                        any(StudentPatchRequestDTO.class),
                        any(Student.class)
                );

        verify(studentRepository, never())
                .save(any(Student.class));

        verify(studentMapper, never())
                .toDTO(any(Student.class));
    }

    @Test
    void patchStudent_shouldThrowException_whenNewEmailAlreadyExists() {

        // Arrange

        Student existingStudent = new Student();

        existingStudent.setId(1L);
        existingStudent.setFirstName("Ali");
        existingStudent.setLastName("Ahmad");
        existingStudent.setEmail("old@test.com");
        existingStudent.setAge(20);


        StudentPatchRequestDTO request =
                new StudentPatchRequestDTO();

        request.setEmail("existing@test.com");


        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(existingStudent));

        when(studentRepository.existsByEmail("existing@test.com"))
                .thenReturn(true);


        // Act + Assert

        DuplicateEmailException exception =
                assertThrows(
                        DuplicateEmailException.class,
                        () -> studentService.patchStudent(1L, request)
                );


        // Assert exception message

        assertEquals(
                "Email 'existing@test.com' already exists.",
                exception.getMessage()
        );


        // Verify

        verify(studentRepository)
                .findById(1L);

        verify(studentRepository)
                .existsByEmail("existing@test.com");

        verify(studentMapper, never())
                .updateEntity(
                        any(StudentPatchRequestDTO.class),
                        any(Student.class)
                );

        verify(studentRepository, never())
                .save(any(Student.class));

        verify(studentMapper, never())
                .toDTO(any(Student.class));
    }

    @Test
    void patchStudent_shouldUpdateSuccessfully_whenEmailRemainsSame() {

        // Arrange

        Student existingStudent = new Student();

        existingStudent.setId(1L);
        existingStudent.setFirstName("Ali");
        existingStudent.setLastName("Ahmad");
        existingStudent.setEmail("old@test.com");
        existingStudent.setAge(20);


        StudentPatchRequestDTO request =
                new StudentPatchRequestDTO();

        request.setEmail("old@test.com");


        StudentResponseDTO response =
                new StudentResponseDTO();

        response.setId(1L);
        response.setFirstName("Ali");
        response.setLastName("Ahmad");
        response.setEmail("old@test.com");
        response.setAge(20);


        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(existingStudent));

        when(studentRepository.save(existingStudent))
                .thenReturn(existingStudent);

        when(studentMapper.toDTO(existingStudent))
                .thenReturn(response);


        // Act

        StudentResponseDTO result =
                studentService.patchStudent(1L, request);


        // Assert

        assertNotNull(result);

        assertEquals(1L, result.getId());
        assertEquals("Ali", result.getFirstName());
        assertEquals("Ahmad", result.getLastName());
        assertEquals("old@test.com", result.getEmail());
        assertEquals(20, result.getAge());


        // Verify

        verify(studentRepository)
                .findById(1L);

        verify(studentRepository, never())
                .existsByEmail(anyString());

        verify(studentMapper)
                .updateEntity(request, existingStudent);

        verify(studentRepository)
                .save(existingStudent);

        verify(studentMapper)
                .toDTO(existingStudent);
    }


}
