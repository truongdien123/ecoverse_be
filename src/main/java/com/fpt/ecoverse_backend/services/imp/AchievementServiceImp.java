package com.fpt.ecoverse_backend.services.imp;

import com.fpt.ecoverse_backend.dtos.requests.AchievementRequestDto;
import com.fpt.ecoverse_backend.utils.UploadFile;
import com.fpt.ecoverse_backend.dtos.responses.AchievementResponseDto;
import com.fpt.ecoverse_backend.entities.Achievement;
import com.fpt.ecoverse_backend.exceptions.NotFoundException;
import com.fpt.ecoverse_backend.repositories.AchievementRepository;
import com.fpt.ecoverse_backend.services.AchievementService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AchievementServiceImp implements AchievementService {

    private final AchievementRepository achievementRepository;
    private final UploadFile uploadFile;

    public AchievementServiceImp(AchievementRepository achievementRepository, UploadFile uploadFile) {
        this.achievementRepository = achievementRepository;
        this.uploadFile = uploadFile;
    }

    @Override
    public AchievementResponseDto createAchievement(AchievementRequestDto requestDto) {
        Achievement achievement = new Achievement();
        achievement.setName(requestDto.getName());
        achievement.setDescription(requestDto.getDescription());
        achievement.setPointsRequired(requestDto.getPointsRequired());
        if (requestDto.getBadgeImage() != null && !requestDto.getBadgeImage().isEmpty()) {
            achievement.setBadgeImageUrl(uploadFile.imageToUrl(requestDto.getBadgeImage()));
        }

        Achievement saved = achievementRepository.save(achievement);
        return mapToResponseDto(saved);
    }

    @Override
    public AchievementResponseDto getAchievementById(String id) {
        Achievement achievement = achievementRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy thành tựu với ID: " + id));
        return mapToResponseDto(achievement);
    }

    @Override
    public Page<AchievementResponseDto> getAllAchievements(Pageable pageable) {
        return achievementRepository.findAll(pageable)
                .map(this::mapToResponseDto);
    }

    @Override
    public AchievementResponseDto updateAchievement(String id, AchievementRequestDto requestDto) {
        Achievement achievement = achievementRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy thành tựu với ID: " + id));

        achievement.setName(requestDto.getName());
        achievement.setDescription(requestDto.getDescription());
        achievement.setPointsRequired(requestDto.getPointsRequired());
        if (requestDto.getBadgeImage() != null && !requestDto.getBadgeImage().isEmpty()) {
            achievement.setBadgeImageUrl(uploadFile.imageToUrl(requestDto.getBadgeImage()));
        }

        Achievement updated = achievementRepository.save(achievement);
        return mapToResponseDto(updated);
    }

    @Override
    public void deleteAchievement(String id) {
        Achievement achievement = achievementRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy thành tựu với ID: " + id));
        achievementRepository.delete(achievement);
    }

    private AchievementResponseDto mapToResponseDto(Achievement achievement) {
        return new AchievementResponseDto(
                achievement.getId(),
                achievement.getName(),
                achievement.getDescription(),
                achievement.getPointsRequired(),
                achievement.getBadgeImageUrl(),
                achievement.getCreatedAt(),
                achievement.getUpdatedAt()
        );
    }
}
