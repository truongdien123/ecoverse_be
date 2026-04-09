package com.fpt.ecoverse_backend.services.imp;

import com.fpt.ecoverse_backend.dtos.requests.PageFilterRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.WasteBinRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.WasteItemRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.WasteBinResponseDto;
import com.fpt.ecoverse_backend.dtos.responses.WasteItemResponseDto;
import com.fpt.ecoverse_backend.entities.*;
import com.fpt.ecoverse_backend.enums.CreatedBy;
import com.fpt.ecoverse_backend.enums.UserType;
import com.fpt.ecoverse_backend.enums.WasteBinCode;
import com.fpt.ecoverse_backend.exceptions.ForbiddenException;
import com.fpt.ecoverse_backend.exceptions.NotFoundException;
import com.fpt.ecoverse_backend.mappers.WasteBinMapper;
import com.fpt.ecoverse_backend.mappers.WasteItemMapper;
import com.fpt.ecoverse_backend.projections.WasteItemWithOrderProjection;
import com.fpt.ecoverse_backend.repositories.*;
import com.fpt.ecoverse_backend.services.WasteService;
import com.fpt.ecoverse_backend.utils.UploadFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WasteServiceImp implements WasteService {

    private final WasteItemRepository wasteItemRepository;
    private final WasteItemMapper wasteItemMapper;
    private final UserRepository userRepository;
    private final PartnerRepository partnerRepository;
    private final UploadFile uploadFile;
    private final WasteBinRepository wasteBinRepository;
    private final WasteBinMapper wasteBinMapper;
    private final GameAttemptRepository gameAttemptRepository;
    private final GameRoundRepository gameRoundRepository;
    private final StudentRepository studentRepository;
    private final GameRoundItemRepository gameRoundItemRepository;

    public WasteServiceImp(WasteItemRepository wasteItemRepository, WasteItemMapper wasteItemMapper, UserRepository userRepository, PartnerRepository partnerRepository, UploadFile uploadFile, WasteBinRepository wasteBinRepository, WasteBinMapper wasteBinMapper, GameAttemptRepository gameAttemptRepository, GameRoundRepository gameRoundRepository, StudentRepository studentRepository, GameRoundItemRepository gameRoundItemRepository) {
        this.wasteItemRepository = wasteItemRepository;
        this.wasteItemMapper = wasteItemMapper;
        this.userRepository = userRepository;
        this.partnerRepository = partnerRepository;
        this.uploadFile = uploadFile;
        this.wasteBinRepository = wasteBinRepository;
        this.wasteBinMapper = wasteBinMapper;
        this.gameAttemptRepository = gameAttemptRepository;
        this.gameRoundRepository = gameRoundRepository;
        this.studentRepository = studentRepository;
        this.gameRoundItemRepository = gameRoundItemRepository;
    }

    @Override
    public WasteItemResponseDto createWasteItem(String userId, WasteItemRequestDto request) {
        CreatedBy userRole = getUserRole(userId);

        WasteItem wasteItem = wasteItemMapper.toWasteItem(request, null);
        wasteItem.setCreatedBy(userRole);
        Optional<WasteBin> wasteBin = wasteBinRepository.findByCode(request.getCorrectBinCode());
        if (wasteBin.isEmpty()) {
            throw new NotFoundException("Not found waste bin or waste bin is not active");
        }
        wasteItem.setWasteBin(wasteBin.get());
        if (userRole == CreatedBy.PARTNERSHIP) {
            Optional<Partner> partnerOpt = partnerRepository.findById(userId);
            if (partnerOpt.isEmpty()) {
                throw new NotFoundException("Partner not found");
            }
            wasteItem.setPartner(partnerOpt.get());
        }
        if (request.getImage() != null) {
            String imageUrl = uploadFile.imageToUrl(request.getImage());
            wasteItem.setImageUrl(imageUrl);
        }
        wasteItemRepository.save(wasteItem);
        WasteItemResponseDto response = wasteItemMapper.toWasteItemResponse(wasteItem, null);
        response.setCorrectBinCode(wasteBin.get().getCode());
        response.setCreatedBy(userRole.toString());
        return response;
    }

    @Override
    public WasteBinResponseDto createWasteBin(String adminId, WasteBinRequestDto request) {
        Optional<User> admin = userRepository.findById(adminId);
        if (admin.isEmpty()) {
            throw new NotFoundException("Admin not found");
        }
        if (admin.get().getRole() != UserType.ADMIN) {
            throw new ForbiddenException("Only admin can create waste bin");
        }
        Optional<WasteBin> wasteBinExisting = wasteBinRepository.findByCode(request.getCode());
        if (wasteBinExisting.isPresent()) {
            throw new ForbiddenException("Waste bin code already exist");
        }
        WasteBin wasteBin = wasteBinMapper.toWasteBin(request, null);
        if (request.getIcon() != null) {
            String iconUrl = uploadFile.imageToUrl(request.getIcon());
            wasteBin.setIconUrl(iconUrl);
        }
        wasteBinRepository.save(wasteBin);
        return wasteBinMapper.toWasteBinResponse(wasteBin);
    }

    @Override
    public List<WasteBinResponseDto> getWasteBins() {
        return wasteBinRepository.findAll().stream().map(wasteBinMapper::toWasteBinResponse).toList();
    }

    @Override
    public List<WasteItemResponseDto> getWasteItems(String userId, String gameRoundId) {
        CreatedBy userRole = getUserRole(userId);
        List<WasteItemWithOrderProjection> wasteItemWithOrder = wasteItemRepository.findByUserIdAndGameRoundId(userId, gameRoundId);
        if (wasteItemWithOrder.isEmpty()) {
            throw new NotFoundException("Waste items not found");
        }
        GameAttempt gameAttempt = new GameAttempt();
        Optional<GameRound> gameRoundOptional = gameRoundRepository.findById(gameRoundId);
        if (gameRoundOptional.isEmpty()) {
            throw new NotFoundException("Game round not found");
        }
        gameAttempt.setGameRound(gameRoundOptional.get());
        gameAttempt.setStudent(gameAttempt.getStudent());
        gameAttempt.setTotalItems(wasteItemWithOrder.size());
        gameAttempt.setAttemptNumber(1);
        gameAttemptRepository.save(gameAttempt);
        return wasteItemWithOrder.stream().map(projection ->
                wasteItemMapper.toWasteItemResponse(projection.getWasteItem(), projection.getOrderIndex())).toList();
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
        } else {
            wasteItemOptional = wasteItemRepository.findById(wasteItemId);
            if (wasteItemOptional.isEmpty()) {
                throw new NotFoundException("Waste item not found");
            }
        }
        wasteItemMapper.toWasteItem(request, wasteItemOptional.get().getId());
        if (request.getImage() != null) {
            String imageUrl = uploadFile.imageToUrl(request.getImage());
            wasteItemOptional.get().setImageUrl(imageUrl);
        }
        wasteItemRepository.save(wasteItemOptional.get());
        return wasteItemMapper.toWasteItemResponse(wasteItemOptional.get(), null);
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
        WasteBin wasteBin = wasteBinMapper.toWasteBin(request, wasteBinOptional.get().getId());
        if (request.getIcon() != null) {
            String iconUrl = uploadFile.imageToUrl(request.getIcon());
            wasteBin.setIconUrl(iconUrl);
        }
        wasteBinRepository.save(wasteBin);
        return wasteBinMapper.toWasteBinResponse(wasteBin);
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
            return wasteItemMapper.toWasteItemResponse(wasteItemOptional.get(), null);
        } else {
            wasteItemOptional = wasteItemRepository.findById(wasteItemId);
            if (wasteItemOptional.isEmpty()) {
                throw new NotFoundException("Waste item not found");
            }
            wasteItemRepository.delete(wasteItemOptional.get());
            return wasteItemMapper.toWasteItemResponse(wasteItemOptional.get(), null);
        }
    }

    @Override
    public List<WasteItemResponseDto> getWasteItemsByFilter(String userId, PageFilterRequestDto pageFilterRequestDto) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        Pageable pageable = PageRequest.of(
                pageFilterRequestDto.getPageNo()-1,
                pageFilterRequestDto.getPageSize());
        WasteBinCode type = null;
        if (pageFilterRequestDto.getType() != null) {
            type = WasteBinCode.valueOf(pageFilterRequestDto.getType().toUpperCase());
        }
        Page<WasteItem> wasteItems = wasteItemRepository.findWasteItemsByUserId(userId, type, pageFilterRequestDto.getSearching(), pageable);

        return wasteItems.stream().map(wasteItem -> {
            WasteItemResponseDto response = wasteItemMapper.toWasteItemResponse(wasteItem, null);
            response.setCorrectBinCode(wasteItem.getWasteBin().getCode());
            response.setCreatedBy(wasteItem.getCreatedBy().toString());
            return response;
        }).toList();
    }

    private CreatedBy getUserRole(String userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        if (userOpt.get().getRole() == UserType.ADMIN) {
            return CreatedBy.ADMIN;
        } else if (userOpt.get().getRole() == UserType.PARTNERSHIP) {
            return CreatedBy.PARTNERSHIP;
        }
        return CreatedBy.ADMIN;
    }
}

