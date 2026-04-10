package com.fpt.ecoverse_backend.services;

import com.fpt.ecoverse_backend.dtos.requests.GameAttemptRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.GamePlacementRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.GameRoundRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.PageFilterRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.GameAttemptResponseDto;
import com.fpt.ecoverse_backend.dtos.responses.GamePlacementResponseDto;
import com.fpt.ecoverse_backend.dtos.responses.GameRoundResponseDto;
import com.fpt.ecoverse_backend.enums.WasteBinCode;

import java.util.List;

public interface GameService {
    GameRoundResponseDto createGameRound(String userId, GameRoundRequestDto request);
    List<GameRoundResponseDto> getGameRounds(String userId, PageFilterRequestDto pageFilterRequestDto);
    GameRoundResponseDto updateGameRound(String userId, String gameRoundId, GameRoundRequestDto request);
    GameRoundResponseDto deleteGameRound(String userId, String gameRoundId);
    GameAttemptResponseDto createGameAttempt(String gameRoundId, String studentId, GameAttemptRequestDto request);
    GameAttemptResponseDto updateGameAttempt(String gameAttemptId, GameAttemptRequestDto request);
    List<GameAttemptResponseDto> getGameAttempts(String studentId, PageFilterRequestDto pageFilterRequestDto);
    List<GamePlacementResponseDto> createGamePlacements(String gameRoundId, String gameAttemptId, List<GamePlacementRequestDto> requests);
    List<GamePlacementResponseDto> getGamePlacements(String gameAttemptId);
    GameAttemptResponseDto replayGameRoundThroughGameAttempt(String gameAttemptId, GameAttemptRequestDto request);
    GamePlacementResponseDto updateGamePlacement(String gamePlacementId, Boolean correct, WasteBinCode code);
}
