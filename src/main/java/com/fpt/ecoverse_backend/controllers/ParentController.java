package com.fpt.ecoverse_backend.controllers;

import com.fpt.ecoverse_backend.dtos.requests.ParentRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.ParentResponseDto;
import com.fpt.ecoverse_backend.services.ParentService;
import com.fpt.ecoverse_backend.utils.ResponseUtil;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parents")
public class ParentController {

    private final ParentService parentService;

    public ParentController(ParentService parentService) {
        this.parentService = parentService;
    }

    @PostMapping("{parent_id}/students/{student_id}/link-student")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseEntity<?> linkParentToStudent(@PathVariable("parent_id") String parentId, @PathVariable("student_id") String studentId) {
        ParentResponseDto response = parentService.linkParentToStudent(parentId, studentId);
        return ResponseUtil.success("Link parent to student successfully", response);
    }

    @GetMapping("{parent_id}/students")
    @PreAuthorize("hasAnyRole('PARENT', 'PARTNERSHIP')")
    public ResponseEntity<?> getListStudent(@PathVariable("parent_id") String parentId) {
        return ResponseUtil.success("Get list student by parent id successfully", parentService.getListStudent(parentId));
    }

    @PutMapping(value = "{parent_id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('PARENT', 'PARTNERSHIP')")
    public ResponseEntity<?> updateParent(@PathVariable("parent_id") String parentId, @ModelAttribute ParentRequestDto request) {
        return ResponseUtil.success("Update parent successfully", parentService.updateParent(parentId, request));
    }

    @GetMapping("{parent_id}")
    @PreAuthorize("hasAnyRole('PARENT', 'PARTNERSHIP')")
    public ResponseEntity<?> getParentDetail(@PathVariable("parent_id") String parentId) {
        return ResponseUtil.success("Get parent by id successfully", parentService.getParentDetail(parentId));
    }

    @DeleteMapping("{parent_id}")
    @PreAuthorize("hasAnyRole('PARENT', 'PARTNERSHIP')")
    public ResponseEntity<?> deleteParent(@PathVariable("parent_id") String parentId) {
        return ResponseUtil.success("Delete parent successfully", parentService.deleteParent(parentId));
    }
}
