package com.example.Day3SMS.service;

import com.example.Day3SMS.dto.StudentRequestDto;
import com.example.Day3SMS.dto.StudentResponseDto;
import com.example.Day3SMS.exception.StudentNotFoundException;
import com.example.Day3SMS.model.StudentModel;
import com.example.Day3SMS.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    // Fixed: Corrected the stream mapping and removed the <S> syntax error
    public List<StudentResponseDto> getAllStudents() {
        return repository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    // Fixed: Updated to use DTOs for input and output
    public StudentResponseDto updateStudent(String id, StudentRequestDto dto) {
        StudentModel existingStudent = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("No student found with id: " + id));

        existingStudent.setName(dto.getName());
        existingStudent.setAge(dto.getAge());
        existingStudent.setEmail(dto.getEmail());

        StudentModel saved = repository.save(existingStudent);
        return mapToResponseDto(saved);
    }

    public StudentResponseDto addStudent(StudentRequestDto dto) {
        StudentModel student = new StudentModel();
        student.setName(dto.getName());
        student.setAge(dto.getAge());
        student.setEmail(dto.getEmail());

        StudentModel saved = repository.save(student);
        return mapToResponseDto(saved);
    }

    public String deleteStudent(String id) {
        StudentModel student = repository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student Not Found"));
        repository.deleteById(id);
        return "Student Deleted Successfully";
    }

    // Helper method to reduce code duplication
    private StudentResponseDto mapToResponseDto(StudentModel student) {
        return new StudentResponseDto(
                student.getId(),
                student.getName(),
                student.getAge(),
                student.getEmail()
        );
    }
}