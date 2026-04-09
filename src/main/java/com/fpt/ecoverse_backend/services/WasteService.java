package com.fpt.ecoverse_backend.services;

import com.fpt.ecoverse_backend.dtos.requests.PageFilterRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.WasteBinRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.WasteItemRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.WasteBinResponseDto;
import com.fpt.ecoverse_backend.dtos.responses.WasteItemResponseDto;

import java.util.List;

public interface WasteService {

    WasteItemResponseDto createWasteItem(String userId, WasteItemRequestDto request);
    WasteBinResponseDto createWasteBin(String adminId, WasteBinRequestDto request);
    List<WasteBinResponseDto> getWasteBins();
    List<WasteItemResponseDto> getWasteItems(String userId, String gameRoundId);
    WasteItemResponseDto updateWasteItem(String userId, String wasteItemId, WasteItemRequestDto request);
    WasteBinResponseDto updateWasteBin(String adminId, String wasteBinId, WasteBinRequestDto request);
    WasteItemResponseDto deleteWasteItem(String userId, String wasteItemId);
    List<WasteItemResponseDto> getWasteItemsByFilter(String userId, PageFilterRequestDto pageFilterRequestDto);
}
