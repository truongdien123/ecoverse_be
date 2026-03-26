package com.fpt.ecoverse_backend.controllers;

import com.fpt.ecoverse_backend.dtos.requests.GameRoundRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.PageFilterRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.GameRoundResponseDto;
import com.fpt.ecoverse_backend.services.GameService;
import com.fpt.ecoverse_backend.utils.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/rounds/users/{user_id}")
    public ResponseEntity<?> createGameRound(@PathVariable("user_id") String userId, GameRoundRequestDto request) {
        GameRoundResponseDto response = gameService.createGameRound(userId, request);
        return ResponseUtil.success("Create game round successfully", response);
    }

    @GetMapping("/rounds/users/{user_id}")
    public ResponseEntity<?> getGameRounds(@PathVariable("user_id") String userId, PageFilterRequestDto pageFilterRequestDto) {
        return ResponseUtil.success("Get game rounds successfully", gameService.getGameRounds(userId, pageFilterRequestDto));
    }

    @PutMapping("/rounds/{game_round_id}/users/{user_id}")
    public ResponseEntity<?> updateGameRound(@PathVariable("user_id") String userId, @PathVariable("game_round_id") String gameRoundId, GameRoundRequestDto request) {
        GameRoundResponseDto response = gameService.updateGameRound(userId, gameRoundId, request);
        return ResponseUtil.success("Update game round successfully", response);
    }

    @DeleteMapping("/rounds/{game_round_id}/users/{user_id}")
    public ResponseEntity<?> deleteGameRound(@PathVariable("user_id") String userId, @PathVariable("game_round_id") String gameRoundId) {
        GameRoundResponseDto response = gameService.deleteGameRound(userId, gameRoundId);
        return ResponseUtil.success("Delete game round successfully", response);
    }
}
