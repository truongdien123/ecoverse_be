package com.fpt.ecoverse_backend.mappers;

import com.fpt.ecoverse_backend.dtos.requests.RewardItemRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.RewardItemResponseDto;
import com.fpt.ecoverse_backend.entities.RewardItem;
import com.fpt.ecoverse_backend.utils.UploadFile;
import org.mapstruct.*;
import org.springframework.web.multipart.MultipartFile;

@Mapper(componentModel = "spring")
public interface RewardItemMapper {

    @Mappings({
        @Mapping(target = "name", source = "request.name"),
        @Mapping(target = "description", source = "request.description"),
        @Mapping(target = "pointsRequired", source = "request.pointRequired"),
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "imageUrl", source = "request.image", qualifiedByName = "convertImg")
    })
    RewardItem toRewardItem(RewardItemRequestDto request, String id, @Context UploadFile uploadFile);

    @Mappings({
        @Mapping(target = "id", source = "rewardItem.id"),
        @Mapping(target = "name", source = "rewardItem.name"),
        @Mapping(target = "description", source = "rewardItem.description"),
        @Mapping(target = "pointRequired", source = "rewardItem.pointsRequired"),
        @Mapping(target = "imageUrl", source = "rewardItem.imageUrl"),
        @Mapping(target = "available", source = "rewardItem.available")
    })
    RewardItemResponseDto toRewardItemResponse(RewardItem rewardItem);


    @Named("convertImg")
    static String convertImg(MultipartFile multipartFile, @Context UploadFile uploadFile) {
        if (multipartFile == null) {
            return "";
        }
        return uploadFile.imageToUrl(multipartFile);
    }
}
