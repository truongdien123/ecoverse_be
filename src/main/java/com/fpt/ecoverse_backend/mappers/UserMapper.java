package com.fpt.ecoverse_backend.mappers;

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
        @Mapping(target = "email", source = "email"),
        @Mapping(target = "phoneNumber", source = "phoneNumber"),
        @Mapping(target = "password", source = "password"),
        @Mapping(target = "address", source = "address"),
        @Mapping(target = "avatarUrl", source = "avatar", qualifiedByName = "convertImg")
    })
    User toUser(PartnerRegisterRequestDto request, @Context UploadFile uploadFile);

    @Mappings({
            @Mapping(target = "address", source = "address"),
            @Mapping(target = "avatarUrl", source = "avatar", qualifiedByName = "convertImg")
    })
    User toUser(PartnerUpdateRequestDto request, @Context UploadFile uploadFile);

    @Mappings({
            @Mapping(target = "fullName", source = "fullName"),
            @Mapping(target = "avatarUrl", source = "avatar", qualifiedByName = "convertImg")
    })
    User toUser(StudentRequestDto reuquest, @Context UploadFile uploadFile);

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
