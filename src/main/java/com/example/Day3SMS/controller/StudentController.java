package com.example.Day3SMS.controller;

import com.example.Day3SMS.dto.StudentRequestDto;
import com.example.Day3SMS.dto.StudentResponseDto;
import com.example.Day3SMS.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
public class StudentController {

    private final StudentService service;

    public  StudentController(StudentService service) {
        this.service = service;
    }

    @GetMapping("/students")
    public List<StudentResponseDto> getAllStudents() {
        return service.getAllStudents();
    }

    @PostMapping("/add-student")
    public StudentResponseDto addStudent(@Valid @RequestBody StudentRequestDto student) {
        return service.addStudent(student);
    }

    @PatchMapping("/update-student/{studentId}")
    public StudentResponseDto updateStudent(@Valid @PathVariable String studentId, @RequestBody StudentRequestDto updates) {
        return service.updateStudent(studentId, updates);
    }

    @DeleteMapping("/students/{studentId}")
    public String deleteStudent(@PathVariable String studentId) {
        return service.deleteStudent(studentId);
    }
}