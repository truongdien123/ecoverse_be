package com.fpt.ecoverse_backend.services.imp;

import com.fpt.ecoverse_backend.dtos.requests.GameAttemptRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.GameRoundRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.PageFilterRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.GameAttemptResponseDto;
import com.fpt.ecoverse_backend.dtos.responses.GameRoundItemResponseDto;
import com.fpt.ecoverse_backend.dtos.responses.GameRoundResponseDto;
import com.fpt.ecoverse_backend.entities.*;
import com.fpt.ecoverse_backend.enums.CreatedBy;
import com.fpt.ecoverse_backend.enums.UserType;
import com.fpt.ecoverse_backend.exceptions.NotFoundException;
import com.fpt.ecoverse_backend.mappers.GameAttemptMapper;
import com.fpt.ecoverse_backend.mappers.GameRoundMapper;
import com.fpt.ecoverse_backend.repositories.*;
import com.fpt.ecoverse_backend.services.GameService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final StudentRepository studentRepository;
    private final GameAttemptRepository gameAttemptRepository;
    private final GameAttemptMapper gameAttemptMapper;

    public GameServiceImp(UserRepository userRepository, PartnerRepository partnerRepository, GameRoundRepository gameRoundRepository, GameRoundMapper gameRoundMapper, WasteItemRepository wasteItemRepository, StudentRepository studentRepository, GameAttemptRepository gameAttemptRepository, GameAttemptMapper gameAttemptMapper) {
        this.userRepository = userRepository;
        this.partnerRepository = partnerRepository;
        this.gameRoundRepository = gameRoundRepository;
        this.gameRoundMapper = gameRoundMapper;
        this.wasteItemRepository = wasteItemRepository;
        this.studentRepository = studentRepository;
        this.gameAttemptRepository = gameAttemptRepository;
        this.gameAttemptMapper = gameAttemptMapper;
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

    @Override
    public List<GameRoundResponseDto> getGameRounds(String userId, PageFilterRequestDto pageFilterRequestDto) {
        Pageable pageable = PageRequest.of(
                pageFilterRequestDto.getPageNo()-1,
                pageFilterRequestDto.getPageSize(),
                Sort.by(pageFilterRequestDto.getSorting()).descending());
        Page<GameRound> gameRoundPage = gameRoundRepository.findGameRounds(
                userId, pageFilterRequestDto.getSearching(), getUserRole(userId).name(), pageable);
        List<GameRoundResponseDto> response = new ArrayList<>();
        for (GameRound gameRound : gameRoundPage.getContent()) {
            GameRoundResponseDto gameRoundResponseDto = gameRoundMapper.toGameRoundResponse(gameRound);
            gameRoundResponseDto.setPartnerId(gameRound.getPartner() != null ? gameRound.getPartner().getId() : null);
            response.add(gameRoundResponseDto);
        }
        return response;
    }

    @Override
    public GameRoundResponseDto updateGameRound(String userId, String gameRoundId, GameRoundRequestDto request) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        Optional<GameRound> gameRoundOpt = gameRoundRepository.findById(gameRoundId);
        if (gameRoundOpt.isEmpty()) {
            throw new NotFoundException("Game round not found");
        }
        GameRound gameRound = getGameRound(userId, gameRoundOpt, userOpt);
        GameRound updatedGameRound = gameRoundMapper.toGameRound(request, gameRound.getId());
        return gameRoundMapper.toGameRoundResponse(gameRoundRepository.save(updatedGameRound));
    }

    @Override
    public GameRoundResponseDto deleteGameRound(String userId, String gameRoundId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        Optional<GameRound> gameRoundOpt = gameRoundRepository.findById(gameRoundId);
        if (gameRoundOpt.isEmpty()) {
            throw new NotFoundException("Game round not found");
        }
        GameRound gameRound = getGameRound(userId, gameRoundOpt, userOpt);
        gameRoundRepository.delete(gameRound);
        return gameRoundMapper.toGameRoundResponse(gameRound);
    }

    @Override
    public GameAttemptResponseDto createGameAttempt(String gameRoundId, String studentId, GameAttemptRequestDto request) {
        Optional<GameRound> gameRound = gameRoundRepository.findById(gameRoundId);
        if (gameRound.isEmpty()) {
            throw new NotFoundException("Game round not found");
        }
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isEmpty()) {
            throw new NotFoundException("Student not found");
        }
        GameAttempt gameAttempt = gameAttemptMapper.toGameAttempt(request, null);
        gameAttempt.setGameRound(gameRound.get());
        gameAttempt.setStudent(student.get());
        Optional<GameAttempt> gameAttemptOptional = gameAttemptRepository.findByGameGroundAndStudent(gameRoundId, studentId);
        if (gameAttemptOptional.isEmpty()) {
            gameAttempt.setAttemptNumber(1);
        } else {
            gameAttempt.setAttemptNumber(gameAttempt.getAttemptNumber()+1);
        }
        gameAttemptRepository.save(gameAttempt);
        return gameAttemptMapper.toGameAttemptResponse(gameAttempt);
    }

    @Override
    public List<GameAttemptResponseDto> getGameAttempts(String gameRoundId, String studentId, PageFilterRequestDto pageFilterRequestDto) {
        Optional<GameRound> gameRound = gameRoundRepository.findById(gameRoundId);
        if (gameRound.isEmpty()) {
            throw new NotFoundException("Game round not found");
        }
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isEmpty()) {
            throw new NotFoundException("Student not found");
        }
        Pageable pageable = PageRequest.of(
                pageFilterRequestDto.getPageNo()-1,
                pageFilterRequestDto.getPageSize(),
                Sort.by(pageFilterRequestDto.getSorting()).descending());
        Page<GameAttempt> gameAttemptPage = gameAttemptRepository.findGameAttempts(gameRoundId, studentId, pageable);
        List<GameAttemptResponseDto> response = new ArrayList<>();
        for (GameAttempt gameAttempt : gameAttemptPage.getContent()) {
            response.add(gameAttemptMapper.toGameAttemptResponse(gameAttempt));
        }
        return response;
    }

    private static GameRound getGameRound(String userId, Optional<GameRound> gameRoundOpt, Optional<User> userOpt) {
        GameRound gameRound = gameRoundOpt.get();
        if (gameRound.getCreatedBy() == CreatedBy.ADMIN &&
                userOpt.get().getRole() != UserType.ADMIN) {
            throw new NotFoundException("Game round not found");
        }
        if (gameRound.getCreatedBy() == CreatedBy.PARTNERSHIP &&
                (userOpt.get().getRole() != UserType.PARTNERSHIP
                        || !gameRound.getPartner().getId().equals(userId))) {
            throw new NotFoundException("Game round not found");
        }
        return gameRound;
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
