package com.fpt.ecoverse_backend.services;

import com.fpt.ecoverse_backend.dtos.requests.GameRoundRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.GameRoundResponseDto;

public interface GameService {
    GameRoundResponseDto createGameRound(String userId, GameRoundRequestDto request);
}
