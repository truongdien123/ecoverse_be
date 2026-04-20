package com.fpt.ecoverse_backend.controllers;

import com.fpt.ecoverse_backend.enums.LeaderboardScope;
import com.fpt.ecoverse_backend.services.LeaderboardService;
import com.fpt.ecoverse_backend.utils.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/leaderboards")
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    public LeaderboardController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    @GetMapping("/{partner_id}")
    @PreAuthorize("hasAnyRole('STUDENT', 'PARTNERSHIP', 'PARENT')")
    public ResponseEntity<?> getListLeaderboard(@PathVariable("partner_id") String partnerId, LeaderboardScope scope, @RequestParam(required = false)  String grade, int page, int size) {
        return ResponseUtil.success("Get list leaderboard successfully", leaderboardService.getListLeaderboard(partnerId, scope, grade, page, size));
    }

    @GetMapping("/partners/{partner_id}/students/{student_id}")
    @PreAuthorize("hasAnyRole('STUDENT', 'PARTNERSHIP', 'PARENT')")
    public ResponseEntity<?> getStudentRank(@PathVariable("partner_id") String partnerId, @PathVariable("student_id") String studentId, LeaderboardScope scope, @RequestParam(required = false) String grade) {
        return ResponseUtil.success("Get student rank successfully", leaderboardService.getStudentRank(studentId, partnerId, scope, grade));
    }
}
