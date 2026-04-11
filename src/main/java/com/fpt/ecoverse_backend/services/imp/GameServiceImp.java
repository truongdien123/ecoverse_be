package com.fpt.ecoverse_backend.services.imp;

import com.fpt.ecoverse_backend.dtos.requests.GameAttemptRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.GamePlacementRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.GameRoundRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.PageFilterRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.*;
import com.fpt.ecoverse_backend.entities.*;
import com.fpt.ecoverse_backend.enums.CreatedBy;
import com.fpt.ecoverse_backend.enums.LeaderboardScope;
import com.fpt.ecoverse_backend.enums.UserType;
import com.fpt.ecoverse_backend.enums.WasteBinCode;
import com.fpt.ecoverse_backend.exceptions.BadRequestException;
import com.fpt.ecoverse_backend.exceptions.NotFoundException;
import com.fpt.ecoverse_backend.mappers.GameAttemptMapper;
import com.fpt.ecoverse_backend.mappers.GamePlacementMapper;
import com.fpt.ecoverse_backend.mappers.GameRoundMapper;
import com.fpt.ecoverse_backend.mappers.WasteItemMapper;
import com.fpt.ecoverse_backend.repositories.*;
import com.fpt.ecoverse_backend.services.GameService;
import com.fpt.ecoverse_backend.services.LeaderboardService;
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
    private final GamePlacementMapper gamePlacementMapper;
    private final GamePlacementRepository gamePlacementRepository;
    private final WasteItemMapper wasteItemMapper;
    private final GameRoundItemRepository gameRoundItemRepository;
    private final WasteBinRepository wasteBinRepository;
    private final LeaderboardService leaderboardService;

    public GameServiceImp(UserRepository userRepository, PartnerRepository partnerRepository, GameRoundRepository gameRoundRepository, GameRoundMapper gameRoundMapper, WasteItemRepository wasteItemRepository, StudentRepository studentRepository, GameAttemptRepository gameAttemptRepository, GameAttemptMapper gameAttemptMapper, GamePlacementMapper gamePlacementMapper, GamePlacementRepository gamePlacementRepository, WasteItemMapper wasteItemMapper, GameRoundItemRepository gameRoundItemRepository, WasteBinRepository wasteBinRepository, LeaderboardService leaderboardService) {
        this.userRepository = userRepository;
        this.partnerRepository = partnerRepository;
        this.gameRoundRepository = gameRoundRepository;
        this.gameRoundMapper = gameRoundMapper;
        this.wasteItemRepository = wasteItemRepository;
        this.studentRepository = studentRepository;
        this.gameAttemptRepository = gameAttemptRepository;
        this.gameAttemptMapper = gameAttemptMapper;
        this.gamePlacementMapper = gamePlacementMapper;
        this.gamePlacementRepository = gamePlacementRepository;
        this.wasteItemMapper = wasteItemMapper;
        this.gameRoundItemRepository = gameRoundItemRepository;
        this.wasteBinRepository = wasteBinRepository;
        this.leaderboardService = leaderboardService;
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
        gameRound.setItemCount(wasteItems.size());
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
                pageFilterRequestDto.getPageSize());
        Page<GameRound> gameRoundPage = gameRoundRepository.findGameRounds(
                userId, pageFilterRequestDto.getSearching(), getUserRole(userId), pageable);
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
        gameAttempt.setAttemptNumber(1);
        gameAttempt.setPointsEarned(request.getPointsEarned());
        gameAttemptRepository.save(gameAttempt);
        GameAttemptResponseDto response = gameAttemptMapper.toGameAttemptResponse(gameAttempt);
        response.setGameRoundId(gameRound.get().getId());
        response.setTitleGameRound(gameRound.get().getTitle());
        response.setStudentId(student.get().getId());
        return response;
    }

    @Override
    public GameAttemptResponseDto updateGameAttempt(String gameAttemptId, GameAttemptRequestDto request) {
        Optional<GameAttempt> gameAttemptOpt = gameAttemptRepository.findById(gameAttemptId);
        if (gameAttemptOpt.isEmpty()) {
            throw new NotFoundException("Game attempt not found");
        }
        Optional<Student> student = studentRepository.findById(gameAttemptOpt.get().getStudent().getId());
        if (student.isEmpty()) {
            throw new NotFoundException("Student not found");
        }
        GameAttempt gameAttempt = gameAttemptOpt.get();
        Integer points = 0;
        if (gameAttempt.getPointsEarned() < request.getPointsEarned()) {
            points = request.getPointsEarned() - gameAttempt.getPointsEarned();
        }
        gameAttemptMapper.updateGameAttempt(gameAttempt, request);
        gameAttemptRepository.save(gameAttempt);
        student.get().setPoints(student.get().getPoints() + points);
        handleGameResult(gameAttempt, points);
        studentRepository.save(student.get());
        GameAttemptResponseDto response = gameAttemptMapper.toGameAttemptResponse(gameAttempt);
        response.setGameRoundId(gameAttempt.getGameRound().getId());
        response.setTitleGameRound(gameAttempt.getGameRound().getTitle());
        response.setStudentId(student.get().getId());
        return response;
    }

    @Override
    public List<GameAttemptResponseDto> getGameAttempts(String studentId, PageFilterRequestDto pageFilterRequestDto) {
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isEmpty()) {
            throw new NotFoundException("Student not found");
        }
        Pageable pageable = PageRequest.of(
                pageFilterRequestDto.getPageNo()-1,
                pageFilterRequestDto.getPageSize());
        Page<GameAttempt> gameAttemptPage = gameAttemptRepository.findGameAttempts(studentId, pageable);
        List<GameAttemptResponseDto> response = new ArrayList<>();
        for (GameAttempt gameAttempt : gameAttemptPage.getContent()) {
            GameAttemptResponseDto gameAttemptResponse = gameAttemptMapper.toGameAttemptResponse(gameAttempt);
            gameAttemptResponse.setGameRoundId(gameAttempt.getGameRound().getId());
            gameAttemptResponse.setTitleGameRound(gameAttempt.getGameRound().getTitle());
            gameAttemptResponse.setStudentId(gameAttempt.getStudent().getId());
            response.add(gameAttemptResponse);
        }
        return response;
    }

    @Override
    public List<GamePlacementResponseDto> createGamePlacements(String gameRoundId, String gameAttemptId, List<GamePlacementRequestDto> requests) {
        Optional<GameRound> gameRoundOpt = gameRoundRepository.findById(gameRoundId);
        if (gameRoundOpt.isEmpty()) {
            throw new NotFoundException("Game round not found");
        }
        Optional<GameAttempt> gameAttemptOpt = gameAttemptRepository.findById(gameAttemptId);
        if (gameAttemptOpt.isEmpty()) {
            throw new NotFoundException("Game attempt not found");
        }
        List<GamePlacementResponseDto> response = new ArrayList<>();
        List<GamePlacement> gamePlacements = new ArrayList<>();
        requests.forEach(request -> {
            GamePlacement gamePlacement = gamePlacementMapper.toGamePlacement(request, null);

            gamePlacement.setGameAttempt(gameAttemptOpt.get());

            WasteItem wasteItem = wasteItemRepository.findById(request.getWasteItemId())
                    .orElseThrow(() -> new NotFoundException("Waste item not found"));
            gamePlacement.setWasteItem(wasteItem);

            gamePlacement.setPlacedBin(
                    wasteBinRepository.findByCode(request.getCode())
                            .orElseThrow(() -> new NotFoundException("Waste bin not found for code: " + request.getCode()))
            );

            GamePlacement saved = gamePlacementRepository.save(gamePlacement);
            gamePlacements.add(saved);

            // build response
            GamePlacementResponseDto gamePlacementResponseDto = gamePlacementMapper.toGamePlacementResponse(saved);

            GameRoundItem gameRoundItem = gameRoundItemRepository
                    .findByGameRoundIdAndWasteItemId(gameRoundId, request.getWasteItemId())
                    .orElseThrow(() -> new NotFoundException("Game round item not found for waste item id: " + request.getWasteItemId()));

            WasteItemResponseDto wasteItemResponseDto =
                    wasteItemMapper.toWasteItemResponse(wasteItem, gameRoundItem.getOrderIndex());

            wasteItemResponseDto.setCorrectBinCode(wasteItem.getWasteBin().getCode());

            gamePlacementResponseDto.setWasteItem(wasteItemResponseDto);
            gamePlacementResponseDto.setCode(request.getCode());
            gamePlacementResponseDto.setIsCorrect(request.getIsCorrect());

            response.add(gamePlacementResponseDto);
        });
        return response;
    }

    @Override
    public List<GamePlacementResponseDto> getGamePlacements(String gameAttemptId) {
        GameAttempt gameAttempt = gameAttemptRepository.findById(gameAttemptId)
                .orElseThrow(() -> new NotFoundException("Game attempt not found"));
        List<GamePlacement> gamePlacements = gamePlacementRepository.findByGameAttemptId(gameAttemptId);
        List<GamePlacementResponseDto> response = new ArrayList<>();
        gamePlacements.forEach(gamePlacement -> {
            GamePlacementResponseDto dto = gamePlacementMapper.toGamePlacementResponse(gamePlacement);
            WasteItem wasteItem = gamePlacement.getWasteItem();
            String gameRoundId = gameAttempt.getGameRound().getId();
            GameRoundItem gameRoundItem = gameRoundItemRepository
                    .findByGameRoundIdAndWasteItemId(gameRoundId, wasteItem.getId())
                    .orElseThrow(() -> new NotFoundException(
                            "Game round item not found for waste item id: " + wasteItem.getId()
                    ));
            WasteItemResponseDto wasteItemDto =
                    wasteItemMapper.toWasteItemResponse(wasteItem, gameRoundItem.getOrderIndex());
            wasteItemDto.setCorrectBinCode(wasteItem.getWasteBin().getCode());
            dto.setWasteItem(wasteItemDto);
            dto.setCode(gamePlacement.getPlacedBin().getCode());
            dto.setIsCorrect(
                    gamePlacement.getIsCorrect()
            );
            response.add(dto);
        });

        return response;
    }

    @Override
    public GameAttemptResponseDto replayGameRoundThroughGameAttempt(String gameAttemptId, GameAttemptRequestDto request) {
        Optional<GameAttempt> gameAttemptOptional = gameAttemptRepository.findById(gameAttemptId);
        if (gameAttemptOptional.isEmpty()) {
            throw new NotFoundException("Not found game attempt");
        }
        GameAttempt gameAttempt = gameAttemptOptional.get();
        gameAttemptMapper.updateGameAttempt(gameAttempt, request);
        gameAttempt.setAttemptNumber(gameAttempt.getAttemptNumber()+1);
        gameAttemptRepository.save(gameAttempt);
        GameAttemptResponseDto response = gameAttemptMapper.toGameAttemptResponse(gameAttempt);
        response.setGameRoundId(gameAttempt.getGameRound().getId());
        response.setTitleGameRound(gameAttempt.getGameRound().getTitle());
        response.setStudentId(gameAttempt.getStudent().getId());
        return response;
    }

    @Override
    public GamePlacementResponseDto updateGamePlacement(String gamePlacementId, Boolean correct, WasteBinCode code) {
        Optional<GamePlacement> gamePlacement = gamePlacementRepository.findById(gamePlacementId);
        if (gamePlacement.isEmpty()) {
            throw new NotFoundException("Game placement not found");
        }
        gamePlacement.get().setIsCorrect(correct);
        WasteBin wasteBin = wasteBinRepository.findByCode(code).orElseThrow(() -> new NotFoundException("Waste bin not found"));
        gamePlacement.get().setPlacedBin(wasteBin);
        gamePlacementRepository.save(gamePlacement.get());
        GamePlacementResponseDto dto = gamePlacementMapper.toGamePlacementResponse(gamePlacement.get());
        WasteItem wasteItem = gamePlacement.get().getWasteItem();
        Optional<GameAttempt> gameAttempt = gameAttemptRepository.findById(gamePlacement.get().getGameAttempt().getId());
        if (gameAttempt.isEmpty()) {
            throw new NotFoundException("Not found game attempt");
        }
        gameAttempt.get().setAttemptNumber(gameAttempt.get().getAttemptNumber()+1);
        gameAttemptRepository.save(gameAttempt.get());
        String gameRoundId = gameAttempt.get().getGameRound().getId();
        GameRoundItem gameRoundItem = gameRoundItemRepository
                .findByGameRoundIdAndWasteItemId(gameRoundId, wasteItem.getId())
                .orElseThrow(() -> new NotFoundException(
                        "Game round item not found for waste item id: " + wasteItem.getId()
                ));
        WasteItemResponseDto wasteItemDto =
                wasteItemMapper.toWasteItemResponse(wasteItem, gameRoundItem.getOrderIndex());
        wasteItemDto.setCorrectBinCode(wasteItem.getWasteBin().getCode());
        dto.setWasteItem(wasteItemDto);
        dto.setCode(gamePlacement.get().getPlacedBin().getCode());
        dto.setIsCorrect(
                gamePlacement.get().getIsCorrect()
        );
        return dto;
    }

    @Override
    public List<GamePlacementResponseDto> updateGamePlacements(String gameAttemptId, List<GamePlacementRequestDto> requests) {
        GameAttempt gameAttempt = gameAttemptRepository.findById(gameAttemptId)
                .orElseThrow(() -> new NotFoundException("Game attempt not found"));

        List<GamePlacementResponseDto> response = new ArrayList<>();

        for (GamePlacementRequestDto request : requests) {

            GamePlacement gamePlacement = gamePlacementRepository
                    .findByGameAttemptIdAndWasteItemId(gameAttemptId, request.getWasteItemId())
                    .orElseThrow(() -> new NotFoundException(
                            "Game placement not found for waste item id: " + request.getWasteItemId()
                    ));

            WasteBin wasteBin = wasteBinRepository.findByCode(request.getCode())
                    .orElseThrow(() -> new NotFoundException("Waste bin not found"));
            gamePlacement.setPlacedBin(wasteBin);

            gamePlacement.setIsCorrect(request.getIsCorrect());

            gamePlacementRepository.save(gamePlacement);

            GamePlacementResponseDto dto = gamePlacementMapper.toGamePlacementResponse(gamePlacement);

            WasteItem wasteItem = gamePlacement.getWasteItem();

            String gameRoundId = gameAttempt.getGameRound().getId();

            GameRoundItem gameRoundItem = gameRoundItemRepository
                    .findByGameRoundIdAndWasteItemId(gameRoundId, wasteItem.getId())
                    .orElseThrow(() -> new NotFoundException(
                            "Game round item not found for waste item id: " + wasteItem.getId()
                    ));

            WasteItemResponseDto wasteItemDto =
                    wasteItemMapper.toWasteItemResponse(wasteItem, gameRoundItem.getOrderIndex());

            wasteItemDto.setCorrectBinCode(wasteItem.getWasteBin().getCode());

            dto.setWasteItem(wasteItemDto);
            dto.setCode(gamePlacement.getPlacedBin().getCode());
            dto.setIsCorrect(gamePlacement.getIsCorrect());

            response.add(dto);
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

    public void handleGameResult(GameAttempt attempt, int point) {
        if (point == 0) return;
        Student student = attempt.getStudent();

        // SCHOOL
        leaderboardService.upsertLeaderboardEntry(
                student.getId(),
                student.getPartner().getId(),
                LeaderboardScope.valueOf("SCHOOL"),
                student.getGrade(),
                point
        );

        // CLASS
        leaderboardService.upsertLeaderboardEntry(
                student.getId(),
                student.getPartner().getId(),
                LeaderboardScope.valueOf("CLASS"),
                student.getGrade(),
                point
        );
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
        } else {
            throw new BadRequestException("User role not supported for game round creation");
        }
    }
}
