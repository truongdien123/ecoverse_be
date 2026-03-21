package com.fpt.ecoverse_backend.mappers;

import com.fpt.ecoverse_backend.dtos.requests.PartnerRegisterRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.PartnerUpdateRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.PartnerResponseDto;
import com.fpt.ecoverse_backend.entities.Partner;
import com.fpt.ecoverse_backend.utils.UploadFile;
import org.mapstruct.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PartnerMapper {

    @Mappings({
            @Mapping(target = "organizationName", source = "organizationName"),
            @Mapping(target = "email", source = "email"),
            @Mapping(target = "phoneNumber", source = "phoneNumber"),
            @Mapping(target = "avatarUrl", source = "avatar", qualifiedByName = "convertImg"),
            @Mapping(target = "password", source = "password"),
            @Mapping(target = "address", source = "address"),
            @Mapping(target = "contactPerson", source = "contactPerson")
    })

    Partner toPartner(PartnerRegisterRequestDto request, @Context UploadFile uploadFile);

    @Mappings({
            @Mapping(target = "partnershipId", source = "id"),
            @Mapping(target = "organizationName", source = "organizationName"),
            @Mapping(target = "email", source = "email"),
            @Mapping(target = "contactPerson", source = "contactPerson"),
            @Mapping(target = "phoneNumber", source = "phoneNumber"),
            @Mapping(target = "address", source = "address"),
            @Mapping(target = "status", source = "status"),
            @Mapping(target = "createdAt", source = "createdAt"),
            @Mapping(target = "updatedAt", source = "updatedAt"),
            @Mapping(target = "avatarUrl", source = "avatarUrl")
    })
    PartnerResponseDto toPartnerResponse(Partner partner);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "organizationName", source = "request.organizationName"),
            @Mapping(target = "contactPerson", source = "request.contactPerson"),
            @Mapping(target = "phoneNumber", source = "request.phoneNumber")
    })
    void toPartner(@MappingTarget Partner partner, PartnerUpdateRequestDto request, @Context UploadFile uploadFile);

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
