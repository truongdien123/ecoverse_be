package com.fpt.ecoverse_backend.controllers;

import com.fpt.ecoverse_backend.dtos.requests.WasteBinRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.WasteItemRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.WasteBinResponseDto;
import com.fpt.ecoverse_backend.dtos.responses.WasteItemResponseDto;
import com.fpt.ecoverse_backend.services.WasteService;
import com.fpt.ecoverse_backend.utils.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wastes")
public class WasteController {

    private final WasteService wasteService;

    public WasteController(WasteService wasteService) {
        this.wasteService = wasteService;
    }

    @PostMapping("/{user_id}")
    public ResponseEntity<?> createWasteItem(@PathVariable("user_id") String userId, WasteItemRequestDto request) {
        WasteItemResponseDto response = wasteService.createWasteItem(userId, request);
        return ResponseUtil.success("Create waste item successfully", response);
    }

    @PostMapping("/bins/{admin_id}")
    public ResponseEntity<?> createWasteBin(@PathVariable("admin_id") String adminId, WasteBinRequestDto request) {
        WasteBinResponseDto response = wasteService.createWasteBin(adminId, request);
        return ResponseUtil.success("Create waste bin successfully", response);
    }

    @GetMapping("/bins")
    public ResponseEntity<?> getWasteBins() {
        return ResponseUtil.success("Get waste bins successfully", wasteService.getWasteBins());
    }

    @GetMapping("/items/users/{user_id}/game-rounds/{game_round_id}")
    public ResponseEntity<?> getWasteItems(@PathVariable("user_id") String userId, @PathVariable("game_round_id") String gameRoundId) {
        return ResponseUtil.success("Get waste items successfully", wasteService.getWasteItems(userId, gameRoundId));
    }

    @PutMapping("/items/{waste_item_id}/users/{user_id}")
    public ResponseEntity<?> updateWasteItem(@PathVariable("user_id") String userId, @PathVariable("waste_item_id") String wasteItemId, WasteItemRequestDto request) {
        WasteItemResponseDto response = wasteService.updateWasteItem(userId, wasteItemId, request);
        return ResponseUtil.success("Update waste item successfully", response);
    }

    @PutMapping("/bins/{waste_bin_id}/admins/{admin_id}")
    public ResponseEntity<?> updateWasteBin(@PathVariable("admin_id") String adminId, @PathVariable("waste_bin_id") String wasteBinId, WasteBinRequestDto request) {
        WasteBinResponseDto response = wasteService.updateWasteBin(adminId, wasteBinId, request);
        return ResponseUtil.success("Update waste bin successfully", response);
    }

    @DeleteMapping("/items/{waste_item_id}/users/{user_id}")
    public ResponseEntity<?> deleteWasteItem(@PathVariable("user_id") String userId, @PathVariable("waste_item_id") String wasteItemId) {
        WasteItemResponseDto response = wasteService.deleteWasteItem(userId, wasteItemId);
        return ResponseUtil.success("Delete waste item successfully", response);
    }
}
