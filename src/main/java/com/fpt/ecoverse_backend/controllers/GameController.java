package com.fpt.ecoverse_backend.controllers;

import com.fpt.ecoverse_backend.dtos.requests.GameAttemptRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.GamePlacementRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.GameRoundRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.PageFilterRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.GameRoundResponseDto;
import com.fpt.ecoverse_backend.enums.WasteBinCode;
import com.fpt.ecoverse_backend.services.GameService;
import com.fpt.ecoverse_backend.utils.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/rounds/users/{user_id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PARTNERSHIP')")
    public ResponseEntity<?> createGameRound(@PathVariable("user_id") String userId, @RequestBody GameRoundRequestDto request) {
        GameRoundResponseDto response = gameService.createGameRound(userId, request);
        return ResponseUtil.success("Create game round successfully", response);
    }

    @PostMapping("/rounds/users/{user_id}/get-list")
    @PreAuthorize("hasAnyRole('ADMIN', 'PARTNERSHIP', 'STUDENT')")
    public ResponseEntity<?> getGameRounds(@PathVariable("user_id") String userId, @RequestBody PageFilterRequestDto pageFilterRequestDto) {
        return ResponseUtil.success("Get game rounds successfully", gameService.getGameRounds(userId, pageFilterRequestDto));
    }

    @PutMapping("/rounds/{game_round_id}/users/{user_id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PARTNERSHIP')")
    public ResponseEntity<?> updateGameRound(@PathVariable("user_id") String userId, @PathVariable("game_round_id") String gameRoundId, @RequestBody GameRoundRequestDto request) {
        GameRoundResponseDto response = gameService.updateGameRound(userId, gameRoundId, request);
        return ResponseUtil.success("Update game round successfully", response);
    }

    @DeleteMapping("/rounds/{game_round_id}/users/{user_id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PARTNERSHIP')")
    public ResponseEntity<?> deleteGameRound(@PathVariable("user_id") String userId, @PathVariable("game_round_id") String gameRoundId) {
        GameRoundResponseDto response = gameService.deleteGameRound(userId, gameRoundId);
        return ResponseUtil.success("Delete game round successfully", response);
    }

    @PostMapping("/rounds/{game_round_id}/students/{student_id}/attempts")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> createGameAttempt(@PathVariable("game_round_id") String gameRoundId, @PathVariable("student_id") String studentId, @RequestBody GameAttemptRequestDto request) {
        return ResponseUtil.success("Create game attempt successfully", gameService.createGameAttempt(gameRoundId, studentId, request));
    }

    @PostMapping("/rounds/{game_round_id}/students/{student_id}/attempts/get-list")
    @PreAuthorize("hasAnyRole('ADMIN', 'PARTNERSHIP', 'STUDENT')")
    public ResponseEntity<?> getGameAttempts(@PathVariable("student_id") String studentId, @PathVariable("game_round_id") String gameRoundId, @RequestBody PageFilterRequestDto pageFilterRequestDto) {
        return ResponseUtil.success("Get game attempts successfully", gameService.getGameAttempts(studentId, gameRoundId, pageFilterRequestDto));
    }

    @PutMapping("/attempts/{game_attempt_id}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> updateGameAttempt(@PathVariable("game_attempt_id") String gameAttemptId, @RequestBody GameAttemptRequestDto request) {
        return ResponseUtil.success("Update game attempt successfully", gameService.updateGameAttempt(gameAttemptId, request));
    }

    @PostMapping("/rounds/{game_round_id}/attempts/{game_attempt_id}/placements")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> createGamePlacements(@PathVariable("game_round_id") String gameRoundId, @PathVariable("game_attempt_id") String gameAttemptId, @RequestBody List<GamePlacementRequestDto> requests) {
        return ResponseUtil.success("Create game placements successfully", gameService.createGamePlacements(gameRoundId, gameAttemptId, requests));
    }

    @GetMapping("/attempts/{game_attempt_id}/placements")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getGamePlacements(@PathVariable("game_attempt_id") String gameAttemptId) {
        return ResponseUtil.success("Get game placements successfully", gameService.getGamePlacements(gameAttemptId));
    }

    @PutMapping("/attempts/{game_attempt_id}/replay-game-round")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> replayGameRoundThroughGameAttempt(@PathVariable("game_attempt_id") String gameAttemptId, @RequestBody GameAttemptRequestDto request) {
        return ResponseUtil.success("Update game attempt through game attempt successfully", gameService.replayGameRoundThroughGameAttempt(gameAttemptId, request));
    }

    @PutMapping("/placements/{game_placement_id}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> updateGamePlacement(@PathVariable("game_placement_id") String gamePlacementId, Boolean correct, WasteBinCode code) {
        return ResponseUtil.success("Update game placement successfully", gameService.updateGamePlacement(gamePlacementId, correct, code));
    }

    @PutMapping("/attempts/{game_attempt_id}/placements")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> updateGamePlacements(@PathVariable("game_attempt_id") String gameAttemptId, @RequestBody List<GamePlacementRequestDto> requests) {
        return ResponseUtil.success("Update list game placement in game attempt successfully", gameService.updateGamePlacements(gameAttemptId, requests));
    }
}
