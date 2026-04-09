package com.fpt.ecoverse_backend.mappers;

import com.fpt.ecoverse_backend.dtos.requests.GameAttemptRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.GameAttemptResponseDto;
import com.fpt.ecoverse_backend.entities.GameAttempt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface GameAttemptMapper {

    @Mappings({
            @Mapping(target = "duration", source = "request.duration"),
            @Mapping(target = "pointsEarned", source = "request.pointsEarned"),
            @Mapping(target = "totalItems", source = "request.totalItems"),
            @Mapping(target = "correctCount", source = "request.correctCount"),
            @Mapping(target = "completed", source = "request.completed"),
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "gamePlacements", ignore = true)
    })
    GameAttempt toGameAttempt(GameAttemptRequestDto request, String id);

    @Mappings({
            @Mapping(target = "duration", source = "request.duration"),
            @Mapping(target = "pointsEarned", source = "request.pointsEarned"),
            @Mapping(target = "totalItems", source = "request.totalItems"),
            @Mapping(target = "correctCount", source = "request.correctCount"),
            @Mapping(target = "completed", source = "request.completed"),
            @Mapping(target = "gamePlacements", ignore = true)
    })
    void updateGameAttempt(@MappingTarget GameAttempt entity, GameAttemptRequestDto request);

    @Mappings({
            @Mapping(target = "duration", source = "duration"),
            @Mapping(target = "pointsEarned", source = "pointsEarned"),
            @Mapping(target = "totalItems", source = "totalItems"),
            @Mapping(target = "correctCount", source = "correctCount"),
            @Mapping(target = "completed", source = "completed"),
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "attemptNumber", source = "attemptNumber")
    })
    GameAttemptResponseDto toGameAttemptResponse(GameAttempt gameAttempt);
}
