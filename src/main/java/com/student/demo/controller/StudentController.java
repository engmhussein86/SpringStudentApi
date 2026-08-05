package com.student.demo.controller;


import com.student.demo.dto.StudentRequestDTO;
import com.student.demo.dto.StudentResponseDTO;
import com.student.demo.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/students")
@Validated
public class StudentController {
    private final StudentService studentService;

    @Operation(
            summary = "Get students with pagination and sorting",
            description = "Returns students page by page with sorting options"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Students retrieved successfully")
    })
     @GetMapping
    public ResponseEntity<Page<StudentResponseDTO>> getAllStudents(
            @PageableDefault(
            page = 0,
            size = 10,
            sort = "id",
            direction = Sort.Direction.ASC
    )Pageable pageable) {
        Page<StudentResponseDTO> students = studentService.getAllStudents(pageable);

        return ResponseEntity.ok(students);
    }

    @Operation(
            summary = "Get a student by ID",
            description = "Retrieves a single student using the provided student ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Student retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getStudentById( @Parameter(description = "Student ID", example = "1") @PathVariable Long id) {

        StudentResponseDTO student = studentService.getStudentById(id);

        return ResponseEntity.ok(student);
    }

    @Operation(
            summary = "Create a new student",
            description = "Creates a new student and stores it in the database."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Student created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    @PostMapping
    public ResponseEntity<StudentResponseDTO> createStudent(
            @Valid @RequestBody StudentRequestDTO requestDTO) {

        StudentResponseDTO responseDTO = studentService.createStudent(requestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDTO);
    }

    @Operation(
            summary = "Update a student",
            description = "Updates all student information for the specified student ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Student updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Student not found"),
            @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> updateStudent(
            @Parameter(description = "Student ID", example = "1") @PathVariable Long id,
            @Valid @RequestBody StudentRequestDTO student) {

        StudentResponseDTO updatedStudent =
                studentService.updateStudent(id, student);

        return ResponseEntity.ok(updatedStudent);
    }

    @Operation(
            summary = "Delete a student",
            description = "Deletes a student from the database using the student ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Student deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent( @Parameter(description = "Student ID", example = "1") @PathVariable Long id) {

        studentService.deleteStudent(id);

        return ResponseEntity.noContent().build();
    }




}
