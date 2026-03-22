package com.fpt.ecoverse_backend.controllers;

import com.fpt.ecoverse_backend.dtos.requests.GameRoundRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.GameRoundResponseDto;
import com.fpt.ecoverse_backend.services.GameService;
import com.fpt.ecoverse_backend.utils.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
