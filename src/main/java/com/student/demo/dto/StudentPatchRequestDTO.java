package com.student.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StudentPatchRequestDTO {

    @Email(message = "Email should be valid")
    private String email;

    @Min(value = 16, message = "Age must be at least 16")
    @Max(value = 100, message = "Age must not exceed 100")
    private Integer age;

    @Pattern(regexp = ".*\\S.*", message = "First name must not be blank")
    private String firstName;

    @Pattern(regexp = ".*\\S.*", message = "Last name must not be blank")
    private String lastName;
}
