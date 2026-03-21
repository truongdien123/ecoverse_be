package com.fpt.ecoverse_backend.mappers;

import com.fpt.ecoverse_backend.dtos.requests.WasteItemRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.WasteItemResponseDto;
import com.fpt.ecoverse_backend.entities.WasteItem;
import com.fpt.ecoverse_backend.utils.UploadFile;
import org.mapstruct.*;
import org.springframework.web.multipart.MultipartFile;

@Mapper(componentModel = "spring")
public interface WasteItemMapper {

    @Mappings({
            @Mapping(target = "name", source = "request.name"),
            @Mapping(target = "description", source = "request.description"),
            @Mapping(target = "imageUrl", source = "request.image", qualifiedByName = "convertImg"),
            @Mapping(target = "id", source = "id")
    })
    WasteItem toWasteItem(WasteItemRequestDto request, String id, @Context UploadFile uploadFile);

    @Mappings({
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "imageUrl", source = "imageUrl"),
            @Mapping(target = "createdBy", source = "createdBy", ignore = true)
    })
    WasteItemResponseDto toWasteItemResponse(WasteItem item);

    @Named("convertImg")
    static String convertImage(MultipartFile multipartFile, @Context UploadFile uploadFile) {
        if (multipartFile == null) {
            return "";
        }
        return uploadFile.imageToUrl(multipartFile);
    }
}

