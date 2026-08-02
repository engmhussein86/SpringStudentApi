package com.student.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StudentResponseDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private Integer age;


}
