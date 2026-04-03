package com.fpt.ecoverse_backend.controllers;

import com.fpt.ecoverse_backend.dtos.requests.PageFilterRequestDto;
import com.fpt.ecoverse_backend.services.AdminService;
import com.fpt.ecoverse_backend.utils.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admins")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PatchMapping("/{admin_id}/partners/{partner_id}/verify")
    public ResponseEntity<?> verifyPartner(@PathVariable("admin_id") String adminId, @PathVariable("partner_id") String partnerId, Boolean isApproved) {
        return ResponseUtil.success("Partner verification successful", adminService.verifyPartner(adminId, partnerId, isApproved));
    }

    @GetMapping("/{admin_id}/partners")
    public ResponseEntity<?> getListPartner(@PathVariable("admin_id") String adminId, String status, PageFilterRequestDto pageFilterRequestDto) {
        return ResponseUtil.success("Get list partner successfully", adminService.getListPartner(adminId, status, pageFilterRequestDto));
    }
}
