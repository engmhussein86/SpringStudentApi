package com.student.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class StudentRequestDTO {

    @Schema(description = "Student first name", example = "Ali")
    @NotBlank(message = "First name is required")
    private String firstName;

    @Schema(description = "Student last name", example = "Ahmad")
    @NotBlank(message = "Last name is required")
    private String lastName;

    @Schema(description = "Student email address", example = "ali@example.com")
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Schema(description = "Student age", example = "20")
    @Min(value = 16, message = "Age must be at least 16")
    @Max(value = 100, message = "Age must not exceed 100")
    private Integer age;

}