package com.fpt.ecoverse_backend.mappers;

import com.fpt.ecoverse_backend.dtos.requests.CompetitionParticipantRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.CompetitionParticipantResponseDto;
import com.fpt.ecoverse_backend.entities.CompetitionParticipant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface CompetitionParticipantMapper {

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "joinedAt", source = "request.joinedAt"),
            @Mapping(target = "totalScore", source = "request.totalScore")
    })
    CompetitionParticipant toCompetitionParticipant(CompetitionParticipantRequestDto request, String id);

    @Mappings({
            @Mapping(target = "competitionParticipantId", source = "id"),
            @Mapping(target = "joinedAt", source = "joinedAt"),
            @Mapping(target = "totalScore", source = "totalScore")
    })
    CompetitionParticipantResponseDto toResponse(CompetitionParticipant competitionParticipant);
}
