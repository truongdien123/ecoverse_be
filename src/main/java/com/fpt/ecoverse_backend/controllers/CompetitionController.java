package com.fpt.ecoverse_backend.controllers;

import com.fpt.ecoverse_backend.dtos.requests.CompetitionLinkRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.CompetitionParticipantRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.CompetitionRequestDto;
import com.fpt.ecoverse_backend.services.CompetitionService;
import com.fpt.ecoverse_backend.utils.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/competitions")
public class CompetitionController {

    private final CompetitionService competitionService;

    public CompetitionController(CompetitionService competitionService) {
        this.competitionService = competitionService;
    }

    @PostMapping("/{partner_id}")
    @PreAuthorize("hasRole('PARTNERSHIP')")
    public ResponseEntity<?> createCompetition(@PathVariable("partner_id") String partnerId, @RequestBody CompetitionRequestDto request) {
        return ResponseUtil.success("Create competition successfully", competitionService.createCompetition(partnerId, request));
    }

    @PostMapping("/{competition_id}/link")
    @PreAuthorize("hasRole('PARTNERSHIP')")
    public ResponseEntity<?> linkCompetition(@PathVariable("competition_id") String competitionId, @RequestBody CompetitionLinkRequestDto request) {
        return ResponseUtil.success("Link competition successfully", competitionService.linkCompetition(competitionId, request));
    }

    @PostMapping("/{competition_id}/students/{student_id}/participant")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> createParticipant(@PathVariable("competition_id") String competitionId, @PathVariable("student_id") String studentId, @RequestBody CompetitionParticipantRequestDto request) {
        return ResponseUtil.success("Create competition participant successfully", competitionService.createCompetitionParticipant(competitionId, studentId, request));
    }

    @GetMapping("/{partner_id}")
    @PreAuthorize("hasAnyRole('STUDENT', 'PARTNERSHIP', 'PARENT')")
    public ResponseEntity<?> getListCompetition(@PathVariable("partner_id") String partnerId) {
        return ResponseUtil.success("Get list competition successfully", competitionService.getListCompetition(partnerId));
    }

    @GetMapping("/{competition_id}/participants")
    @PreAuthorize("hasAnyRole('STUDENT', 'PARTNERSHIP', 'PARENT')")
    public ResponseEntity<?> getListParticipant(@PathVariable("competition_id") String competitionId) {
        return ResponseUtil.success("Get list participant successfully", competitionService.getListParticipant(competitionId));
    }

    @PutMapping("/{competition_id}")
    @PreAuthorize("hasRole('PARTNERSHIP')")
    public ResponseEntity<?> updateCompetition(@PathVariable("competition_id") String competitionId, @RequestBody CompetitionRequestDto request) {
        return ResponseUtil.success("Update competition successfully", competitionService.updateCompetition(competitionId, request));
    }

    @DeleteMapping("/{competition_id}")
    @PreAuthorize("hasRole('PARTNERSHIP')")
    public ResponseEntity<?> deleteCompetition(@PathVariable("competition_id") String competitionId) {
        return ResponseUtil.success("Delete competition successfully", competitionService.deleteCompetition(competitionId));
    }
}
