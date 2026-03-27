package com.fpt.ecoverse_backend.mappers;

import com.fpt.ecoverse_backend.dtos.requests.GameRoundRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.GameRoundResponseDto;
import com.fpt.ecoverse_backend.entities.GameRound;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface GameRoundMapper {

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "title", source = "request.title"),
            @Mapping(target = "description", source = "request.description"),
            @Mapping(target = "shared", source = "request.shared")
    })
    GameRound toGameRound(GameRoundRequestDto request, String id);

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "title", source = "title"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "shared", source = "shared"),
            @Mapping(target = "createdBy", source = "createdBy"),
            @Mapping(target = "itemCount", source = "itemCount"),
            @Mapping(target = "active", source = "active")
    })
    GameRoundResponseDto toGameRoundResponse(GameRound gameRound);
}
