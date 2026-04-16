package com.fpt.ecoverse_backend.controllers;

import com.fpt.ecoverse_backend.dtos.requests.PageFilterRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.WasteBinRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.WasteItemRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.WasteBinResponseDto;
import com.fpt.ecoverse_backend.dtos.responses.WasteItemResponseDto;
import com.fpt.ecoverse_backend.services.WasteService;
import com.fpt.ecoverse_backend.utils.ResponseUtil;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wastes")
public class WasteController {

    private final WasteService wasteService;

    public WasteController(WasteService wasteService) {
        this.wasteService = wasteService;
    }

    @PostMapping(value = "/{user_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'PARTNERSHIP', 'STUDENT')")
    public ResponseEntity<?> createWasteItem(@PathVariable("user_id") String userId, @ModelAttribute WasteItemRequestDto request) {
        WasteItemResponseDto response = wasteService.createWasteItem(userId, request);
        return ResponseUtil.success("Create waste item successfully", response);
    }

    @PostMapping(value = "/bins/{admin_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createWasteBin(@PathVariable("admin_id") String adminId, @ModelAttribute WasteBinRequestDto request) {
        WasteBinResponseDto response = wasteService.createWasteBin(adminId, request);
        return ResponseUtil.success("Create waste bin successfully", response);
    }

    @GetMapping("/bins")
    @PreAuthorize("hasAnyRole('ADMIN', 'PARTNERSHIP', 'STUDENT')")
    public ResponseEntity<?> getWasteBins() {
        return ResponseUtil.success("Get waste bins successfully", wasteService.getWasteBins());
    }

    @GetMapping("/items/users/{user_id}/game-rounds/{game_round_id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PARTNERSHIP', 'STUDENT')")
    public ResponseEntity<?> getWasteItems(@PathVariable("user_id") String userId, @PathVariable("game_round_id") String gameRoundId) {
        return ResponseUtil.success("Get waste items successfully", wasteService.getWasteItems(userId, gameRoundId));
    }

    @PutMapping(value = "/items/{waste_item_id}/users/{user_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'PARTNERSHIP')")
    public ResponseEntity<?> updateWasteItem(@PathVariable("user_id") String userId, @PathVariable("waste_item_id") String wasteItemId, @ModelAttribute WasteItemRequestDto request) {
        WasteItemResponseDto response = wasteService.updateWasteItem(userId, wasteItemId, request);
        return ResponseUtil.success("Update waste item successfully", response);
    }

    @PutMapping(value = "/bins/{waste_bin_id}/admins/{admin_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateWasteBin(@PathVariable("admin_id") String adminId, @PathVariable("waste_bin_id") String wasteBinId, @ModelAttribute WasteBinRequestDto request) {
        WasteBinResponseDto response = wasteService.updateWasteBin(adminId, wasteBinId, request);
        return ResponseUtil.success("Update waste bin successfully", response);
    }

    @DeleteMapping("/items/{waste_item_id}/users/{user_id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PARTNERSHIP')")
    public ResponseEntity<?> deleteWasteItem(@PathVariable("user_id") String userId, @PathVariable("waste_item_id") String wasteItemId) {
        WasteItemResponseDto response = wasteService.deleteWasteItem(userId, wasteItemId);
        return ResponseUtil.success("Delete waste item successfully", response);
    }

    @PostMapping("/items/{user_id}/get-list")
    @PreAuthorize("hasAnyRole('ADMIN', 'PARTNERSHIP')")
    public ResponseEntity<?> getWasteItems(@PathVariable("user_id") String userId, @RequestBody PageFilterRequestDto pageFilterRequestDto) {
        List<WasteItemResponseDto> response = wasteService.getWasteItemsByFilter(userId, pageFilterRequestDto);
        return ResponseUtil.success("Get waste items by filter successfully", response);
    }

    @GetMapping("/items/{game_placement_id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PARTNERSHIP', 'STUDENT')")
    public ResponseEntity<?> getDetailWasteItem(@PathVariable("game_placement_id") String gamePlacementId) {
        return ResponseUtil.success("Get waste item detail successfully", wasteService.getDetailWasteItem(gamePlacementId));
    }
}
