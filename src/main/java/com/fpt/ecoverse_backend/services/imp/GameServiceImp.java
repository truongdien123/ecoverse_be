package com.fpt.ecoverse_backend.services.imp;

import com.fpt.ecoverse_backend.dtos.requests.GameRoundRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.GameRoundItemResponseDto;
import com.fpt.ecoverse_backend.dtos.responses.GameRoundResponseDto;
import com.fpt.ecoverse_backend.entities.*;
import com.fpt.ecoverse_backend.enums.CreatedBy;
import com.fpt.ecoverse_backend.enums.UserType;
import com.fpt.ecoverse_backend.exceptions.NotFoundException;
import com.fpt.ecoverse_backend.mappers.GameRoundMapper;
import com.fpt.ecoverse_backend.repositories.GameRoundRepository;
import com.fpt.ecoverse_backend.repositories.PartnerRepository;
import com.fpt.ecoverse_backend.repositories.UserRepository;
import com.fpt.ecoverse_backend.repositories.WasteItemRepository;
import com.fpt.ecoverse_backend.services.GameService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GameServiceImp implements GameService {

    private final UserRepository userRepository;
    private final PartnerRepository partnerRepository;
    private final GameRoundRepository gameRoundRepository;
    private final GameRoundMapper gameRoundMapper;
    private final WasteItemRepository wasteItemRepository;

    public GameServiceImp(UserRepository userRepository, PartnerRepository partnerRepository, GameRoundRepository gameRoundRepository, GameRoundMapper gameRoundMapper, WasteItemRepository wasteItemRepository) {
        this.userRepository = userRepository;
        this.partnerRepository = partnerRepository;
        this.gameRoundRepository = gameRoundRepository;
        this.gameRoundMapper = gameRoundMapper;
        this.wasteItemRepository = wasteItemRepository;
    }

    @Override
    public GameRoundResponseDto createGameRound(String userId, GameRoundRequestDto request) {
        CreatedBy userRole = getUserRole(userId);
        GameRound gameRound = gameRoundMapper.toGameRound(request, null);
        gameRound.setCreatedBy(userRole);
        Partner partner = new Partner();
        if (userRole == CreatedBy.PARTNERSHIP) {
            partner = partnerRepository.findById(userId).get();
            gameRound.setPartner(partner);
        }
        List<GameRoundItem> gameRoundItems = new ArrayList<>();
        List<WasteItem> wasteItems = wasteItemRepository.findAllById(request.getWasteItemIds());
        List<GameRoundItemResponseDto> gameRoundItemResponseDtos = new ArrayList<>();
        for (int i = 0; i < wasteItems.size(); i++) {
            GameRoundItem gameRoundItem = new GameRoundItem();
            gameRoundItem.setWasteItem(wasteItems.get(i));
            gameRoundItem.setGameRound(gameRound);
            gameRoundItem.setOrderIndex(i);
            gameRoundItems.add(gameRoundItem);
            GameRoundItemResponseDto gameRoundItemResponseDto = new GameRoundItemResponseDto();
            gameRoundItemResponseDto.setWasteItemId(wasteItems.get(i).getId());
            gameRoundItemResponseDto.setOrderIndex(i);
            gameRoundItemResponseDtos.add(gameRoundItemResponseDto);
        }
        gameRound.setGameRoundItems(gameRoundItems);
        gameRoundRepository.save(gameRound);
        GameRoundResponseDto response = gameRoundMapper.toGameRoundResponse(gameRound);
        response.setGameRoundItems(gameRoundItemResponseDtos);
        response.setPartnerId(partner.getId());
        return response;
    }


    private CreatedBy getUserRole(String userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        if (userOpt.get().getRole() == UserType.ADMIN) {
            return CreatedBy.ADMIN;
        } else if (userOpt.get().getRole() == UserType.PARENT) {
            return CreatedBy.PARTNERSHIP;
        } else {
            throw new NotFoundException("User not found");
        }
    }
}
