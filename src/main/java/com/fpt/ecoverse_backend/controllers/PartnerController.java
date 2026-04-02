package com.fpt.ecoverse_backend.controllers;

import com.fpt.ecoverse_backend.dtos.requests.PageFilterRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.PartnerRegisterRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.PartnerUpdateRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.StudentRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.BulkCreateReportResponseDto;
import com.fpt.ecoverse_backend.dtos.responses.PartnerResponseDto;
import com.fpt.ecoverse_backend.dtos.responses.StudentResponseDto;
import com.fpt.ecoverse_backend.dtos.responses.UserListResponseDto;
import com.fpt.ecoverse_backend.services.PartnerService;
import com.fpt.ecoverse_backend.utils.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/partnerships")
public class PartnerController {

    private final PartnerService partnerService;

    public PartnerController(PartnerService partnerService) {
        this.partnerService = partnerService;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<?> createPartner(@Valid PartnerRegisterRequestDto request) {
        PartnerResponseDto response = partnerService.createPartner(request);
        return ResponseUtil.success("Create partner successfully", response);
    }

    @GetMapping("/{partnership_id}")
    public ResponseEntity<?> getDetailPartner(@PathVariable("partnership_id") String partnerId) {
        PartnerResponseDto response = partnerService.getDetailPartner(partnerId);
        return ResponseUtil.success("Get partner detail successfully", response);
    }

    @PutMapping(value = "{partnership_id}/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updatePartner(@PathVariable("partnership_id") String partnerId, @ModelAttribute PartnerUpdateRequestDto request) {
        PartnerResponseDto response = partnerService.updatePartner(partnerId, request);
        return ResponseUtil.success("Update partner successfully", response);
    }

    @PostMapping(value = "/{partnership_id}/bulk-create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> bulkCreate(@PathVariable("partnership_id") String partnerId, @ModelAttribute @RequestParam("file") MultipartFile file) {
        BulkCreateReportResponseDto response = partnerService.bulkCreate(file, partnerId);
        return ResponseUtil.success("Bulk create students and parent successfully. Please check file report", response);
    }

    @GetMapping("/{partnership_id}/students/{student_id}")
    public ResponseEntity<?> getStudentDetail(@PathVariable("partnership_id") String partnerId, @PathVariable("student_id") String studentId) {
        StudentResponseDto response = partnerService.getStudentDetail(partnerId, studentId);
        return ResponseUtil.success("Get student detail successfully", response);
    }

    @PatchMapping("/{partnership_id}/students/{student_id}")
    public ResponseEntity<?> changeStatusStudent(@PathVariable("partnership_id") String partnerId, @PathVariable("student_id") String studentId, @RequestParam("status") String status) {
        StudentResponseDto response = partnerService.changeStatusStudent(partnerId, studentId, status);
        return ResponseUtil.success("Change status student successfully", response);
    }

    @GetMapping("/{partnership_id}/accounts")
    public ResponseEntity<?> getListUser(@PathVariable("partnership_id") String partnerId, @RequestBody PageFilterRequestDto pageFilterRequestDto) {
        UserListResponseDto<?> list = partnerService.getListUser(partnerId, pageFilterRequestDto);
        return ResponseUtil.success("Get list user successfully", list);
    }

    @PostMapping("/{partnership_id}/students")
    public ResponseEntity<?> createStudents(@PathVariable("partnership_id") String partnerId, @RequestBody List<StudentRequestDto> studentRequestDtos) {
        List<StudentResponseDto> list = partnerService.createStudents(partnerId, studentRequestDtos);
        return ResponseUtil.success("Create students successfully", list);
    }

    @DeleteMapping("/{partnership_id}")
    public ResponseEntity<?> deletePartner(@PathVariable("partnership_id") String partnerId) {
        PartnerResponseDto response = partnerService.deletePartner(partnerId);
        return ResponseUtil.success("Delete partner successfully", response);
    }
}

