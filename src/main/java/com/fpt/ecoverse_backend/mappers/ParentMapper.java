package com.fpt.ecoverse_backend.mappers;

import com.fpt.ecoverse_backend.dtos.responses.ParentResponseDto;
import com.fpt.ecoverse_backend.entities.Parent;
import com.fpt.ecoverse_backend.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ParentMapper {
    @Mappings({
            @Mapping(target = "fullName", source = "user.fullName"),
            @Mapping(target = "phoneNumber", source = "user.phoneNumber"),
            @Mapping(target = "address", source = "user.address"),
            @Mapping(target = "email", source = "user.email"),
            @Mapping(target = "avatarUrl", source = "user.avatarUrl"),
            @Mapping(target = "active", source = "user.active"),
            @Mapping(target = "parentId", source = "id")
    })
    ParentResponseDto toParentResponse(Parent parent);


}
