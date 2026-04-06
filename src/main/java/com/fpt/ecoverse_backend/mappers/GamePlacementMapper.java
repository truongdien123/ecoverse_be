package com.fpt.ecoverse_backend.mappers;

import com.fpt.ecoverse_backend.dtos.requests.GamePlacementRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.GamePlacementResponseDto;
import com.fpt.ecoverse_backend.entities.GamePlacement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface GamePlacementMapper {

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "isCorrect", source = "request.isCorrect"),
            @Mapping(target = "placedBin.code", source = "request.code")
    })
    GamePlacement toGamePlacement(GamePlacementRequestDto request, String id);

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "isCorrect", source = "isCorrect"),
        @Mapping(target = "code", source = "placedBin.code"),
        @Mapping(target = "wasteItem", source = "wasteItem")
    })
    GamePlacementResponseDto toGamePlacementResponse(GamePlacement gamePlacement);
}
