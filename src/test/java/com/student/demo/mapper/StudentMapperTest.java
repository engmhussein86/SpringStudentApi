package com.student.demo.mapper;

import com.student.demo.dto.StudentPatchRequestDTO;
import com.student.demo.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StudentMapperTest {
    private StudentMapper studentMapper;

    @BeforeEach
    void setUp() {
        studentMapper = new StudentMapper();
    }

    @Test
    void updateEntity_shouldUpdateOnlyAge() {

        // Arrange

        Student student = new Student();

        student.setId(1L);
        student.setFirstName("Ali");
        student.setLastName("Ahmad");
        student.setEmail("ali@test.com");
        student.setAge(20);


        StudentPatchRequestDTO request =
                new StudentPatchRequestDTO();

        request.setAge(25);


        // Act

        studentMapper.updateEntity(request, student);


        // Assert

        assertEquals(25, student.getAge());

        assertEquals("Ali", student.getFirstName());
        assertEquals("Ahmad", student.getLastName());
        assertEquals("ali@test.com", student.getEmail());
    }

    @Test
    void updateEntity_shouldUpdateOnlyEmail() {

        // Arrange

        Student student = new Student();

        student.setId(1L);
        student.setFirstName("Ali");
        student.setLastName("Ahmad");
        student.setEmail("old@test.com");
        student.setAge(20);


        StudentPatchRequestDTO request =
                new StudentPatchRequestDTO();

        request.setEmail("new@test.com");


        // Act

        studentMapper.updateEntity(request, student);


        // Assert

        assertEquals("new@test.com", student.getEmail());

        assertEquals("Ali", student.getFirstName());
        assertEquals("Ahmad", student.getLastName());
        assertEquals(20, student.getAge());
    }

    @Test
    void updateEntity_shouldNotChangeFields_whenAllFieldsAreNull() {

        // Arrange

        Student student = new Student();

        student.setId(1L);
        student.setFirstName("Ali");
        student.setLastName("Ahmad");
        student.setEmail("ali@test.com");
        student.setAge(20);


        StudentPatchRequestDTO request =
                new StudentPatchRequestDTO();


        // Act

        studentMapper.updateEntity(request, student);


        // Assert

        assertEquals("Ali", student.getFirstName());
        assertEquals("Ahmad", student.getLastName());
        assertEquals("ali@test.com", student.getEmail());
        assertEquals(20, student.getAge());
    }

    @Test
    void updateEntity_shouldUpdateAllProvidedFields() {

        // Arrange

        Student student = new Student();

        student.setId(1L);
        student.setFirstName("Ali");
        student.setLastName("Ahmad");
        student.setEmail("old@test.com");
        student.setAge(20);


        StudentPatchRequestDTO request =
                new StudentPatchRequestDTO();

        request.setFirstName("Omar");
        request.setLastName("Hassan");
        request.setEmail("new@test.com");
        request.setAge(25);


        // Act

        studentMapper.updateEntity(request, student);


        // Assert

        assertEquals("Omar", student.getFirstName());
        assertEquals("Hassan", student.getLastName());
        assertEquals("new@test.com", student.getEmail());
        assertEquals(25, student.getAge());
    }


}
