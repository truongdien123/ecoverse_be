package com.fpt.ecoverse_backend.services.imp;

import com.fpt.ecoverse_backend.dtos.requests.WasteBinRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.WasteItemRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.WasteBinResponseDto;
import com.fpt.ecoverse_backend.dtos.responses.WasteItemResponseDto;
import com.fpt.ecoverse_backend.entities.Admin;
import com.fpt.ecoverse_backend.entities.WasteBin;
import com.fpt.ecoverse_backend.entities.WasteItem;
import com.fpt.ecoverse_backend.enums.CreatedBy;
import com.fpt.ecoverse_backend.exceptions.ForbiddenException;
import com.fpt.ecoverse_backend.exceptions.NotFoundException;
import com.fpt.ecoverse_backend.mappers.WasteBinMapper;
import com.fpt.ecoverse_backend.mappers.WasteItemMapper;
import com.fpt.ecoverse_backend.repositories.AdminRepository;
import com.fpt.ecoverse_backend.repositories.PartnerRepository;
import com.fpt.ecoverse_backend.repositories.WasteBinRepository;
import com.fpt.ecoverse_backend.repositories.WasteItemRepository;
import com.fpt.ecoverse_backend.services.WasteService;
import com.fpt.ecoverse_backend.utils.UploadFile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WasteServiceImp implements WasteService {

    private final WasteItemRepository wasteItemRepository;
    private final WasteItemMapper wasteItemMapper;
    private final AdminRepository adminRepository;
    private final PartnerRepository partnerRepository;
    private final UploadFile uploadFile;
    private final WasteBinRepository wasteBinRepository;
    private final WasteBinMapper wasteBinMapper;

    public WasteServiceImp(WasteItemRepository wasteItemRepository, WasteItemMapper wasteItemMapper, AdminRepository adminRepository, PartnerRepository partnerRepository, UploadFile uploadFile, WasteBinRepository wasteBinRepository, WasteBinMapper wasteBinMapper) {
        this.wasteItemRepository = wasteItemRepository;
        this.wasteItemMapper = wasteItemMapper;
        this.adminRepository = adminRepository;
        this.partnerRepository = partnerRepository;
        this.uploadFile = uploadFile;
        this.wasteBinRepository = wasteBinRepository;
        this.wasteBinMapper = wasteBinMapper;
    }

    @Override
    public WasteItemResponseDto createWasteItem(String userId, WasteItemRequestDto request) {
        CreatedBy userRole = getUserRole(userId);
        WasteItem wasteItem = wasteItemMapper.toWasteItem(request, null, uploadFile);
        wasteItem.setCreatedBy(userRole);
        Optional<WasteBin> wasteBin = wasteBinRepository.findByCode(request.getCorrectBinCode());
        if (wasteBin.isEmpty()) {
            throw new NotFoundException("Not found waste bin or waste bin is not active");
        }
        wasteItem.setWasteBin(wasteBin.get());
        wasteItemRepository.save(wasteItem);
        WasteItemResponseDto response = wasteItemMapper.toWasteItemResponse(wasteItem);
        response.setCorrectBinCode(wasteBin.get().getCode());
        response.setCreatedBy(userRole.toString());
        return wasteItemMapper.toWasteItemResponse(wasteItem);
    }

    @Override
    public WasteBinResponseDto createWasteBin(String adminId, WasteBinRequestDto request) {
        Optional<Admin> admin = adminRepository.findById(adminId);
        if (admin.isEmpty()) {
            throw new NotFoundException("Admin not found");
        }
        WasteBin wasteBin = wasteBinMapper.toWasteBin(request, null, uploadFile);
        wasteBinRepository.save(wasteBin);
        return wasteBinMapper.toWasteBinResponse(wasteBin);
    }

    @Override
    public List<WasteBinResponseDto> getWasteBins() {
        return wasteBinRepository.findAll().stream().map(wasteBinMapper::toWasteBinResponse).toList();
    }

    @Override
    public List<WasteItemResponseDto> getWasteItems(String userId) {
        CreatedBy userRole = getUserRole(userId);
        List<WasteItem> wasteItems = wasteItemRepository.findWasteItems(userRole, userId);
        return wasteItems.stream().map(wasteItemMapper::toWasteItemResponse).toList();
    }

    @Override
    public WasteItemResponseDto updateWasteItem(String userId, String wasteItemId, WasteItemRequestDto request) {
        CreatedBy userRole = getUserRole(userId);
        Optional<WasteItem> wasteItemOptional;
        if (userRole == CreatedBy.PARTNERSHIP) {
            wasteItemOptional = wasteItemRepository.findWasteItemByPartnerId(userId);
            if (wasteItemOptional.isEmpty()) {
                throw new NotFoundException("Waste item not found");
            }
            wasteItemMapper.toWasteItem(request, wasteItemOptional.get().getId(), uploadFile);
            wasteItemRepository.save(wasteItemOptional.get());
            return wasteItemMapper.toWasteItemResponse(wasteItemOptional.get());
        } else {
            wasteItemOptional = wasteItemRepository.findById(wasteItemId);
            if (wasteItemOptional.isEmpty()) {
                throw new NotFoundException("Waste item not found");
            }
            wasteItemMapper.toWasteItem(request, wasteItemOptional.get().getId(), uploadFile);
            wasteItemRepository.save(wasteItemOptional.get());
            return wasteItemMapper.toWasteItemResponse(wasteItemOptional.get());
        }
    }

    @Override
    public WasteBinResponseDto updateWasteBin(String adminId, String wasteBinId, WasteBinRequestDto request) {
        CreatedBy userRole = getUserRole(adminId);
        if (userRole != CreatedBy.ADMIN) {
            throw new ForbiddenException("Only admin can update waste bin");
        }
        Optional<WasteBin> wasteBinOptional = wasteBinRepository.findById(wasteBinId);
        if (wasteBinOptional.isEmpty()) {
            throw new NotFoundException("Waste bin not found");
        }
        wasteBinMapper.toWasteBin(request, wasteBinOptional.get().getId(), uploadFile);
        wasteBinRepository.save(wasteBinOptional.get());
        return wasteBinMapper.toWasteBinResponse(wasteBinOptional.get());
    }

    @Override
    public WasteItemResponseDto deleteWasteItem(String userId, String wasteItemId) {
        CreatedBy userRole = getUserRole(userId);
        Optional<WasteItem> wasteItemOptional;
        if (userRole == CreatedBy.PARTNERSHIP) {
            wasteItemOptional = wasteItemRepository.findWasteItemByPartnerId(userId);
            if (wasteItemOptional.isEmpty()) {
                throw new NotFoundException("Waste item not found");
            }
            wasteItemRepository.delete(wasteItemOptional.get());
            return wasteItemMapper.toWasteItemResponse(wasteItemOptional.get());
        } else {
            wasteItemOptional = wasteItemRepository.findById(wasteItemId);
            if (wasteItemOptional.isEmpty()) {
                throw new NotFoundException("Waste item not found");
            }
            wasteItemRepository.delete(wasteItemOptional.get());
            return wasteItemMapper.toWasteItemResponse(wasteItemOptional.get());
        }
    }

    private CreatedBy getUserRole(String userId) {
        if (adminRepository.existsById(userId)) {
            return CreatedBy.ADMIN;
        } else if (partnerRepository.existsById(userId)) {
            return CreatedBy.PARTNERSHIP;
        } else {
            throw new NotFoundException("User not found");
        }
    }
}

