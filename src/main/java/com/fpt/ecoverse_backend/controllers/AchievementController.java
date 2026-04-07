package com.fpt.ecoverse_backend.controllers;

import com.fpt.ecoverse_backend.dtos.requests.AchievementRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.AchievementResponseDto;
import com.fpt.ecoverse_backend.base.ApiResponse;
import com.fpt.ecoverse_backend.services.AchievementService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/achievements")
public class AchievementController {

    private final AchievementService achievementService;

    public AchievementController(AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AchievementResponseDto>> createAchievement(
            @Valid @ModelAttribute AchievementRequestDto requestDto) {
        AchievementResponseDto created = achievementService.createAchievement(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Thêm thành tựu thành công", created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AchievementResponseDto>> getAchievementById(@PathVariable String id) {
        AchievementResponseDto achievement = achievementService.getAchievementById(id);
        return ResponseEntity.ok(new ApiResponse<>("Lấy thông tin thành tựu thành công", achievement));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<AchievementResponseDto>>> getAllAchievements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AchievementResponseDto> achievements = achievementService.getAllAchievements(pageable);
        return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách thành tựu thành công", achievements));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AchievementResponseDto>> updateAchievement(
            @PathVariable String id,
            @Valid @ModelAttribute AchievementRequestDto requestDto) {
        AchievementResponseDto updated = achievementService.updateAchievement(id, requestDto);
        return ResponseEntity.ok(new ApiResponse<>("Cập nhật thành tựu thành công", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAchievement(@PathVariable String id) {
        achievementService.deleteAchievement(id);
        return ResponseEntity.ok(new ApiResponse<>("Xóa thành tựu thành công", null));
    }
}
