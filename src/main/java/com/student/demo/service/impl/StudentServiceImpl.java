package com.student.demo.service.impl;

import com.student.demo.dto.StudentRequestDTO;
import com.student.demo.dto.StudentResponseDTO;
import com.student.demo.entity.Student;
import com.student.demo.exception.DuplicateEmailException;
import com.student.demo.exception.InvalidSortFieldException;
import com.student.demo.exception.ResourceNotFoundException;
import com.student.demo.mapper.StudentMapper;
import com.student.demo.repository.StudentRepository;
import com.student.demo.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class StudentServiceImpl implements StudentService {
    private  final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private static final Set<String> ALLOWED_SORT_FIELDS =
            Set.of("id", "firstName", "lastName", "email", "age");

    public Page<StudentResponseDTO> getAllStudents(int page,
                                                   int size,
                                                   String sortBy,
                                                   String direction){

        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            throw new InvalidSortFieldException(sortBy);
        }
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable =
                PageRequest.of(page, size, sort);

        return studentRepository.findAll(pageable)
                .map(studentMapper::toDTO);

//        return studentRepository.findAll().stream()
//                .map(studentMapper::toDTO)
//                .toList();
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

        if (!existingStudent.getEmail().equals(studentRequestDTO.getEmail())
                && studentRepository.existsByEmail(studentRequestDTO.getEmail())) {

            throw new DuplicateEmailException(studentRequestDTO.getEmail());
        }

        studentMapper.updateStudentFromDto(studentRequestDTO,existingStudent);
// move this code to mapper to make service focuse on busniess logic (Single Responsibility Principle (SRP))
//        existingStudent.setFirstName(studentRequestDTO.getFirstName());
//        existingStudent.setLastName(studentRequestDTO.getLastName());
//        existingStudent.setEmail(studentRequestDTO.getEmail());
//        existingStudent.setAge(studentRequestDTO.getAge());

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
