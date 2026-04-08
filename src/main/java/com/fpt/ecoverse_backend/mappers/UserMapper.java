package com.fpt.ecoverse_backend.mappers;

import com.fpt.ecoverse_backend.dtos.requests.ParentRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.PartnerRegisterRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.PartnerUpdateRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.StudentRequestDto;
import com.fpt.ecoverse_backend.entities.User;
import com.fpt.ecoverse_backend.utils.UploadFile;
import org.mapstruct.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mappings({
        @Mapping(target = "email", source = "request.email"),
        @Mapping(target = "phoneNumber", source = "request.phoneNumber"),
        @Mapping(target = "password", source = "request.password"),
        @Mapping(target = "address", source = "request.address"),
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "avatarUrl", source = "request.avatar", qualifiedByName = "convertImg"),
    })
    User toUser(PartnerRegisterRequestDto request, String id, @Context UploadFile uploadFile);

    @Mappings({
            @Mapping(target = "address", source = "request.address"),
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "avatarUrl", source = "request.avatar", qualifiedByName = "convertImg")
    })
    User toUser(PartnerUpdateRequestDto request, String id, @Context UploadFile uploadFile);

    @Mappings({
            @Mapping(target = "fullName", source = "request.fullName"),
            @Mapping(target = "id", source = "id")
    })
    User toUser(StudentRequestDto request, String id);

    @Mappings({
            @Mapping(target = "fullName", source = "request.fullName"),
            @Mapping(target = "address", source = "request.address"),
            @Mapping(target = "phoneNumber", source = "request.phoneNumber"),
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "email", source = "request.email")
    })
    User toUser(ParentRequestDto request, String id);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "fullName", source = "fullName"),
            @Mapping(target = "address", source = "address"),
            @Mapping(target = "phoneNumber", source = "phoneNumber"),
            @Mapping(target = "email", source = "email"),
            @Mapping(target = "notifications", ignore = true),
            @Mapping(target = "password", ignore = true),
            @Mapping(target = "role", ignore = true),
            @Mapping(target = "active", ignore = true),
            @Mapping(target = "avatarUrl", ignore = true)
    })
    void updateUserFromDto(ParentRequestDto dto, @MappingTarget User user);

    @Named("convertImg")
    static String convertImg(MultipartFile multipartFile, @Context UploadFile uploadFile) {
        if (multipartFile == null) {
            return "";
        }
        return uploadFile.imageToUrl(multipartFile);
    }

    @Named("convertImages")
    static List<String> convertImages(List<MultipartFile> multipartFiles, @Context UploadFile uploadFile) {
        return uploadFile.imagesToUrl(multipartFiles);
    }
}
