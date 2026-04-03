package com.fpt.ecoverse_backend.controllers;

import com.fpt.ecoverse_backend.dtos.requests.StudentRequestDto;
import com.fpt.ecoverse_backend.services.StudentService;
import com.fpt.ecoverse_backend.utils.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{student_id}")
    public ResponseEntity<?> getStudentDetail(@PathVariable("student_id") String studentId) {
        return ResponseUtil.success("Get student details successfully", studentService.getStudentDetails(studentId));
    }

    @PutMapping("/{student_id}")
    public ResponseEntity<?> updateStudent(@PathVariable("student_id") String studentId, StudentRequestDto request) {
        return ResponseUtil.success("Update student details successfully", studentService.updateStudentDetails(studentId, request));
    }
}
