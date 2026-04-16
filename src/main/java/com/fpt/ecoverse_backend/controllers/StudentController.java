package com.fpt.ecoverse_backend.controllers;

import com.fpt.ecoverse_backend.dtos.requests.StudentRequestDto;
import com.fpt.ecoverse_backend.services.StudentService;
import com.fpt.ecoverse_backend.utils.ResponseUtil;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{student_id}")
    @PreAuthorize("hasAnyRole('PARTNERSHIP', 'STUDENT', 'PARENT')")
    public ResponseEntity<?> getStudentDetail(@PathVariable("student_id") String studentId) {
        return ResponseUtil.success("Get student details successfully", studentService.getStudentDetails(studentId));
    }

    @PutMapping(value = "/{student_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('PARTNERSHIP', 'STUDENT')")
    public ResponseEntity<?> updateStudent(@PathVariable("student_id") String studentId, @ModelAttribute StudentRequestDto request) {
        return ResponseUtil.success("Update student details successfully", studentService.updateStudentDetails(studentId, request));
    }

    @DeleteMapping("/{student_id}")
    @PreAuthorize("hasAnyRole('PARTNERSHIP', 'STUDENT')")
    public ResponseEntity<?> deleteStudent(@PathVariable("student_id") String studentId) {
        return ResponseUtil.success("Delete student successfully", studentService.deleteStudent(studentId));
    }

    @GetMapping("/{student_id}/waste-items/AI")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getWasteItemByAI(@PathVariable("student_id") String studentId) {
        return ResponseUtil.success("Get waste items by AI successfully", studentService.getWasteItemByAI(studentId));
    }

    @PatchMapping(value = "/{student_id}/update-avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> updateAvatarStudent(@PathVariable("student_id") String studentId, @ModelAttribute MultipartFile file) {
        return ResponseUtil.success("Update avatar student successfully", studentService.updateAvatar(studentId, file));
    }
}
