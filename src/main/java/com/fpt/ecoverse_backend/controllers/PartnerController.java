package com.fpt.ecoverse_backend.controllers;

import com.fpt.ecoverse_backend.base.ApiResponse;
import com.fpt.ecoverse_backend.dtos.requests.PageFilterRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.PartnerRegisterRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.PartnerUpdateRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.StudentRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.BulkCreateReportResponseDto;
import com.fpt.ecoverse_backend.dtos.responses.PartnerResponseDto;
import com.fpt.ecoverse_backend.dtos.responses.StudentResponseDto;
import com.fpt.ecoverse_backend.dtos.responses.UserListResponseDto;
import com.fpt.ecoverse_backend.services.PartnerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>> createPartner(@Valid @ModelAttribute PartnerRegisterRequestDto request) {
        PartnerResponseDto response = partnerService.createPartner(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Create partner successfully", response));
    }

    @GetMapping("/{partnership_id}")
    public ResponseEntity<ApiResponse<?>> getDetailPartner(@PathVariable("partnership_id") String partnerId) {
        PartnerResponseDto response = partnerService.getDetailPartner(partnerId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Get detail partner successfully", response));
    }

    @PutMapping(value = "{partnership_id}/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>> updatePartner(@PathVariable("partnership_id") String partnerId, @ModelAttribute PartnerUpdateRequestDto request) {
        PartnerResponseDto response = partnerService.updatePartner(partnerId, request);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Update partner successfully", response));
    }

    @PostMapping("/{partnership_id}/bulk-create")
    public ResponseEntity<ApiResponse<?>> bulkCreate(@PathVariable("partnership_id") String partnerId, @RequestParam("file") MultipartFile file) {
        BulkCreateReportResponseDto response = partnerService.bulkCreate(file, partnerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Bulk create partner and student successfully", response));
    }

    @GetMapping("/{partnership_id}/students/{student_id}")
    public ResponseEntity<ApiResponse<?>> getStudentDetail(@PathVariable("partnership_id") String partnerId, @PathVariable("student_id") String studentId) {
        StudentResponseDto response = partnerService.getStudentDetail(partnerId, studentId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Get student detail successfully", response));
    }

    @PatchMapping("/{partnership_id}/students/{student_id}")
    public ResponseEntity<ApiResponse<?>> changeStatusStudent(@PathVariable("partnership_id") String partnerId, @PathVariable("student_id") String studentId, @RequestParam("status") String status) {
        StudentResponseDto response = partnerService.changeStatusStudent(partnerId, studentId, status);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Change status student successfully", response));
    }

    @GetMapping("/{partnership_id}/accounts")
    public ResponseEntity<ApiResponse<?>> getListUser(@PathVariable("partnership_id") String partnerId, @RequestBody PageFilterRequestDto pageFilterRequestDto) {
        UserListResponseDto<?> list = partnerService.getListUser(partnerId, pageFilterRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Get list user by type "+pageFilterRequestDto.getType()+" successfully", list));
    }

    @PostMapping("/{partnership_id}/students")
    public ResponseEntity<ApiResponse<?>> createStudents(@PathVariable("partnership_id") String partnerId, @RequestBody List<StudentRequestDto> studentRequestDtos) {
        List<StudentResponseDto> list = partnerService.createStudents(partnerId, studentRequestDtos);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Create students successfully", list));
    }
}

