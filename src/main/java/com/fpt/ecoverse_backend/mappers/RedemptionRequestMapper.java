package com.fpt.ecoverse_backend.mappers;

import com.fpt.ecoverse_backend.dtos.responses.RedemptionResponseDto;
import com.fpt.ecoverse_backend.entities.RedemptionRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface RedemptionRequestMapper {

    @Mappings({
                @Mapping(target = "redemptionId", source = "id"),
                @Mapping(target = "pointsRequired", source = "redemptionRequest.rewardItem.pointsRequired"),
                @Mapping(target = "parentId", source = "redemptionRequest.parent.id"),
                @Mapping(target = "partnerId", source = "redemptionRequest.partner.id"),
                @Mapping(target = "reasonParent", source = "redemptionRequest.reasonParent"),
                @Mapping(target = "reasonPartner", source = "redemptionRequest.reasonPartner"),
                @Mapping(target = "redemptionDate", source = "redemptionRequest.createdAt"),
                @Mapping(target = "parentApproval", source = "redemptionRequest.parentApproval"),
                @Mapping(target = "partnerApproval", source = "redemptionRequest.partnerApproval"),
                @Mapping(target = "fulfilled", source = "redemptionRequest.fulfilled")
    })
    RedemptionResponseDto toRedemptionResponse(RedemptionRequest redemptionRequest, String id);
}
