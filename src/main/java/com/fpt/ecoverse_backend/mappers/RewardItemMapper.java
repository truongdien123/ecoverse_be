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
        @Mapping(target = "available", source = "request.available"),
        @Mapping(target = "redemptionRequests", ignore = true),
        @Mapping(target = "partner", ignore = true)
    })
    RewardItem toRewardItem(RewardItemRequestDto request, String id);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "name", source = "request.name"),
            @Mapping(target = "description", source = "request.description"),
            @Mapping(target = "pointsRequired", source = "request.pointRequired"),
            @Mapping(target = "available", source = "request.available"),
            @Mapping(target = "redemptionRequests", ignore = true),
            @Mapping(target = "partner", ignore = true),
            @Mapping(target = "id", ignore = true)
    })
    void updateRewardItem(@MappingTarget RewardItem entity, RewardItemRequestDto request);

    @Mappings({
        @Mapping(target = "id", source = "rewardItem.id"),
        @Mapping(target = "name", source = "rewardItem.name"),
        @Mapping(target = "description", source = "rewardItem.description"),
        @Mapping(target = "pointRequired", source = "rewardItem.pointsRequired"),
        @Mapping(target = "imageUrl", source = "rewardItem.imageUrl"),
        @Mapping(target = "available", source = "rewardItem.available"),
        @Mapping(target = "partnerId", source = "rewardItem.partner.id")
    })
    RewardItemResponseDto toRewardItemResponse(RewardItem rewardItem);

}
