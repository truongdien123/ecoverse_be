package com.fpt.ecoverse_backend.mappers;

import com.fpt.ecoverse_backend.dtos.requests.WasteBinRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.WasteBinResponseDto;
import com.fpt.ecoverse_backend.entities.WasteBin;
import com.fpt.ecoverse_backend.utils.UploadFile;
import org.mapstruct.*;
import org.springframework.web.multipart.MultipartFile;

@Mapper(componentModel = "spring")
public interface WasteBinMapper {

    @Mappings({
            @Mapping(target = "code", source = "request.code"),
            @Mapping(target = "displayName", source = "request.displayName"),
            @Mapping(target = "colorHex", source = "request.colorHex"),
            @Mapping(target = "description", source = "request.description"),
            @Mapping(target = "id", source = "id")
    })
    WasteBin toWasteBin(WasteBinRequestDto request, String id);

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "code", source = "code"),
            @Mapping(target = "displayName", source = "displayName"),
            @Mapping(target = "colorHex", source = "colorHex"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "iconUrl", source = "iconUrl"),
            @Mapping(target = "active", source = "active")
    })
    WasteBinResponseDto toWasteBinResponse(WasteBin wasteBin);

    @Named("convertImg")
    static String convertImage(MultipartFile multipartFile, @Context UploadFile uploadFile) {
        if (multipartFile == null) {
            return "";
        }
        return uploadFile.imageToUrl(multipartFile);
    }
}
