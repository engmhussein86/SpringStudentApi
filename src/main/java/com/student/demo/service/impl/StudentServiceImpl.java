package com.student.demo.service.impl;

import com.student.demo.dto.StudentPatchRequestDTO;
import com.student.demo.dto.StudentRequestDTO;
import com.student.demo.dto.StudentResponseDTO;
import com.student.demo.entity.Student;
import com.student.demo.exception.DuplicateEmailException;
import com.student.demo.exception.ResourceNotFoundException;
import com.student.demo.mapper.StudentMapper;
import com.student.demo.repository.StudentRepository;
import com.student.demo.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class StudentServiceImpl implements StudentService {
    private  final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private static final Set<String> ALLOWED_SORT_FIELDS =
            Set.of("id", "firstName", "lastName", "email", "age");

    public Page<StudentResponseDTO> getAllStudents(Pageable pageable){

//        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
//            throw new InvalidSortFieldException(sortBy);
//        }

        return studentRepository.findAll(pageable)
                .map(studentMapper::toDTO);

//        return studentRepository.findAll().stream()
//                .map(studentMapper::toDTO)
//                .toList();
    }

    @Override
    public StudentResponseDTO getStudentById(Long id) {
        return studentMapper.toDTO(findStudentById(id));
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
        Student existingStudent = findStudentById(id);

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
        Student existingStudent = findStudentById(id);

        studentRepository.delete(existingStudent);

    }

    @Override
    @Transactional
    public StudentResponseDTO patchStudent(
            Long id,
            StudentPatchRequestDTO patchRequestDTO) {

        Student existingStudent = findStudentById(id);


        if (patchRequestDTO.getEmail() != null
                && !patchRequestDTO.getEmail().isBlank() && !existingStudent.getEmail()
                .equals(patchRequestDTO.getEmail())
                && studentRepository.existsByEmail(
                patchRequestDTO.getEmail())) {

            throw new DuplicateEmailException(
                    patchRequestDTO.getEmail());
        }


        studentMapper.updateEntity(
                patchRequestDTO,
                existingStudent
        );


        Student updatedStudent =
                studentRepository.save(existingStudent);


        return studentMapper.toDTO(updatedStudent);
    }

    private Student findStudentById(Long id) {

        return studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student", id));
    }
}
