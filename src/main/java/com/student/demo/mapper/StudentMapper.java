package com.student.demo.mapper;

import com.student.demo.dto.StudentPatchRequestDTO;
import com.student.demo.dto.StudentRequestDTO;
import com.student.demo.dto.StudentResponseDTO;
import com.student.demo.entity.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    public Student toEntity(StudentRequestDTO dto) {

        Student student = new Student();

        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setEmail(dto.getEmail());
        student.setAge(dto.getAge());

        return student;
    }


    public StudentResponseDTO toDTO(Student student) {

        StudentResponseDTO dto = new StudentResponseDTO();

        dto.setId(student.getId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setEmail(student.getEmail());
        dto.setAge(student.getAge());

        return dto;
    }

    public void updateStudentFromDto(StudentRequestDTO dto, Student student) {

        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setEmail(dto.getEmail());
        student.setAge(dto.getAge());
    }

    public void updateEntity(StudentPatchRequestDTO dto,
                             Student student) {

        if (dto.getFirstName() != null) {
            student.setFirstName(dto.getFirstName());
        }

        if (dto.getLastName() != null) {
            student.setLastName(dto.getLastName());
        }

        if (dto.getEmail() != null) {
            student.setEmail(dto.getEmail());
        }

        if (dto.getAge() != null) {
            student.setAge(dto.getAge());
        }
    }
}
