package com.fpt.ecoverse_backend.mappers;

import com.fpt.ecoverse_backend.dtos.requests.PartnerRegisterRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.PartnerUpdateRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.PartnerResponseDto;
import com.fpt.ecoverse_backend.entities.Partner;
import com.fpt.ecoverse_backend.entities.User;
import com.fpt.ecoverse_backend.utils.UploadFile;
import org.mapstruct.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PartnerMapper {

    @Mappings({
            @Mapping(target = "organizationName", source = "organizationName"),
            @Mapping(target = "contactPerson", source = "contactPerson")
    })

    Partner toPartner(PartnerRegisterRequestDto request);

    @Mappings({
            @Mapping(target = "partnershipId", source = "partner.id"),
            @Mapping(target = "organizationName", source = "partner.organizationName"),
            @Mapping(target = "email", source = "user.email"),
            @Mapping(target = "contactPerson", source = "partner.contactPerson"),
            @Mapping(target = "phoneNumber", source = "user.phoneNumber"),
            @Mapping(target = "address", source = "user.address"),
            @Mapping(target = "status", source = "partner.status"),
            @Mapping(target = "createdAt", source = "user.createdAt"),
            @Mapping(target = "updatedAt", source = "user.updatedAt"),
            @Mapping(target = "avatarUrl", source = "user.avatarUrl")
    })
    PartnerResponseDto toPartnerResponse(Partner partner, User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "organizationName", source = "request.organizationName"),
            @Mapping(target = "contactPerson", source = "request.contactPerson")
    })
    void toPartner(@MappingTarget Partner partner, PartnerUpdateRequestDto request);

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
