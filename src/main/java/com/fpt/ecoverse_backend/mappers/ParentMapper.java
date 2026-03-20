package com.fpt.ecoverse_backend.mappers;

import com.fpt.ecoverse_backend.dtos.responses.ParentResponseDto;
import com.fpt.ecoverse_backend.entities.Parent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ParentMapper {

    @Mappings({
            @Mapping(target = "fullName", source = "fullName"),
            @Mapping(target = "phoneNumber", source = "phoneNumber"),
            @Mapping(target = "address", source = "address"),
            @Mapping(target = "email", source = "email")
    })
    ParentResponseDto toParentResponse(Parent parent);
}
