package com.fpt.ecoverse_backend.controllers;

import com.fpt.ecoverse_backend.services.AdminService;
import com.fpt.ecoverse_backend.utils.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
