package com.fpt.ecoverse_backend.mappers;

import com.fpt.ecoverse_backend.dtos.requests.CompetitionRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.CompetitionResponseDto;
import com.fpt.ecoverse_backend.entities.Competition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface CompetitionMapper {

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "targetScope", source = "request.scope"),
            @Mapping(target = "targetClassCode", source = "request.targetClass"),
            @Mapping(target = "partner", ignore = true),
            @Mapping(target = "competitionLinks", ignore = true),
            @Mapping(target = "competitionParticipants", ignore = true)
    })
    Competition toCompetition(CompetitionRequestDto request, String id);

    @Mappings({
            @Mapping(target = "competitionId", source = "id"),
            @Mapping(target = "scope", source = "targetScope"),
            @Mapping(target = "targetClass", source = "targetClassCode")
    })
    CompetitionResponseDto toCompetitionResponse(Competition competition);
}
