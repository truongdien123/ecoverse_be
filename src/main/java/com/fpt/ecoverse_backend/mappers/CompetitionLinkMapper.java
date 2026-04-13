package com.fpt.ecoverse_backend.mappers;

import com.fpt.ecoverse_backend.dtos.responses.CompetitionLinkResponseDto;
import com.fpt.ecoverse_backend.entities.CompetitionLink;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface CompetitionLinkMapper {

    @Mappings({
            @Mapping(target = "competitionLinkId", source = "id"),
            @Mapping(target = "isCustom", source = "isCustom"),
            @Mapping(target = "score", source = "score")
    })
    CompetitionLinkResponseDto toCompetitionLinkResponse(CompetitionLink competitionLink);
}
