package com.fpt.ecoverse_backend.services.imp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.ecoverse_backend.dtos.requests.CompetitionLinkRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.CompetitionParticipantRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.CompetitionRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.*;
import com.fpt.ecoverse_backend.entities.*;
import com.fpt.ecoverse_backend.enums.CompetitionType;
import com.fpt.ecoverse_backend.exceptions.BadRequestException;
import com.fpt.ecoverse_backend.exceptions.NotFoundException;
import com.fpt.ecoverse_backend.mappers.*;
import com.fpt.ecoverse_backend.repositories.*;
import com.fpt.ecoverse_backend.services.CompetitionService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompetitionServiceImp implements CompetitionService {

    private final CompetitionRepository competitionRepository;
    private final PartnerRepository partnerRepository;
    private final CompetitionMapper competitionMapper;
    private final QuizTemplateRepository quizTemplateRepository;
    private final GameRoundRepository gameRoundRepository;
    private final CompetitionLinkRepository competitionLinkRepository;
    private final CompetitionLinkMapper competitionLinkMapper;
    private final StudentRepository studentRepository;
    private final CompetitionParticipantMapper competitionParticipantMapper;
    private final CompetitionParticipantRepository competitionParticipantRepository;
    private final StudentMapper studentMapper;
    private final GameRoundMapper gameRoundMapper;
    private final ObjectMapper objectMapper;

    public CompetitionServiceImp(CompetitionRepository competitionRepository, PartnerRepository partnerRepository, CompetitionMapper competitionMapper, QuizTemplateRepository quizTemplateRepository, GameRoundRepository gameRoundRepository, CompetitionLinkRepository competitionLinkRepository, CompetitionLinkMapper competitionLinkMapper, StudentRepository studentRepository, CompetitionParticipantMapper competitionParticipantMapper, CompetitionParticipantRepository competitionParticipantRepository, StudentMapper studentMapper, GameRoundMapper gameRoundMapper, ObjectMapper objectMapper) {
        this.competitionRepository = competitionRepository;
        this.partnerRepository = partnerRepository;
        this.competitionMapper = competitionMapper;
        this.quizTemplateRepository = quizTemplateRepository;
        this.gameRoundRepository = gameRoundRepository;
        this.competitionLinkRepository = competitionLinkRepository;
        this.competitionLinkMapper = competitionLinkMapper;
        this.studentRepository = studentRepository;
        this.competitionParticipantMapper = competitionParticipantMapper;
        this.competitionParticipantRepository = competitionParticipantRepository;
        this.studentMapper = studentMapper;
        this.gameRoundMapper = gameRoundMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public CompetitionResponseDto createCompetition(String partnerId, CompetitionRequestDto request) {

        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new NotFoundException("Not found partner"));

        Competition competition = competitionMapper.toCompetition(request, null);
        competition.setPartner(partner);
        competitionRepository.save(competition);

        CompetitionLinkRequestDto linkRequest = request.getLink();

        if (linkRequest != null) {

            if (linkRequest.getQuizTemplateId() == null && linkRequest.getGameRoundId() == null) {
                throw new BadRequestException("Must provide quizTemplateId or gameRoundId");
            }

            if (linkRequest.getQuizTemplateId() != null && linkRequest.getGameRoundId() != null) {
                throw new BadRequestException("Only one is allowed");
            }

            CompetitionLink competitionLink = new CompetitionLink();
            competitionLink.setCompetition(competition);
            competitionLink.setIsCustom(linkRequest.getIsCustom());
            competitionLink.setScore(linkRequest.getScore());

            if (linkRequest.getQuizTemplateId() != null) {
                QuizTemplate quizTemplate = quizTemplateRepository
                        .findByIdForCompetition(linkRequest.getQuizTemplateId())
                        .orElseThrow(() -> new NotFoundException("Not found quiz template"));

                competitionLink.setQuizTemplate(quizTemplate);
                competitionLink.setCompetitionType(CompetitionType.QUIZ);

            } else {
                GameRound gameRound = gameRoundRepository
                        .findByIdForCompetition(linkRequest.getGameRoundId())
                        .orElseThrow(() -> new NotFoundException("Not found game round"));

                competitionLink.setGameRound(gameRound);
                competitionLink.setCompetitionType(CompetitionType.GAME);
            }

            competitionLinkRepository.save(competitionLink);
        }
        CompetitionResponseDto responseDto = competitionMapper.toCompetitionResponse(competition);
        responseDto.setScore(request.getLink().getScore() != null ? request.getLink().getScore() : 0);
        return responseDto;
    }

    @Override
    public CompetitionLinkResponseDto linkCompetition(String competitionId, CompetitionLinkRequestDto request) {
        Optional<Competition> competition = competitionRepository.findById(competitionId);
        if (competition.isEmpty()) {
            throw new NotFoundException("Not found competition");
        }
        if (request.getQuizTemplateId() == null && request.getGameRoundId() == null) {
            throw new BadRequestException("Must provide quizTemplateId or gameRoundId");
        }
        CompetitionLink competitionLink = new CompetitionLink();
        Optional<GameRound> gameRound = Optional.empty();
        Optional<QuizTemplate> quizTemplate = Optional.empty();
        competitionLink.setCompetition(competition.get());
        if (request.getQuizTemplateId() != null) {
            quizTemplate = quizTemplateRepository.findByIdForCompetition(request.getQuizTemplateId());
            if (quizTemplate.isEmpty()) {
                throw new NotFoundException("Not found quiz template for competition");
            }
            competitionLink.setQuizTemplate(quizTemplate.get());
            competitionLink.setCompetitionType(CompetitionType.QUIZ);
        } else if (request.getGameRoundId() != null) {
            gameRound = gameRoundRepository.findByIdForCompetition(request.getGameRoundId());
            if (gameRound.isEmpty()) {
                throw new NotFoundException("Not found game round for competition");
            }
            competitionLink.setGameRound(gameRound.get());
            competitionLink.setCompetitionType(CompetitionType.GAME);
        }
        competitionLink.setIsCustom(request.getIsCustom());
        competitionLink.setScore(request.getScore());
        competitionLinkRepository.save(competitionLink);
        CompetitionLinkResponseDto response = competitionLinkMapper.toCompetitionLinkResponse(competitionLink);
        response.setCompetitionId(competitionId);
        if (quizTemplate.isPresent()) {
            response.setQuizTemplateId(quizTemplate.get().getId());
        } else gameRound.ifPresent(round -> response.setGameRoundId(round.getId()));
        return response;
    }

    @Override
    public CompetitionParticipantResponseDto createCompetitionParticipant(String competitionId, String studentId, CompetitionParticipantRequestDto request) {
        Optional<Competition> competition = competitionRepository.findById(competitionId);
        if (competition.isEmpty()) {
            throw new NotFoundException("Not found competition");
        }
        LocalDateTime nowInVietNam = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        if (competition.get().getStartTime() != null && nowInVietNam.isBefore(competition.get().getStartTime())) {
            throw new BadRequestException("Competition has not started yet");
        }
        if (competition.get().getEndTime() != null && nowInVietNam.isAfter(competition.get().getEndTime())) {
            throw new BadRequestException("Competition has ended");
        }
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isEmpty()) {
            throw new NotFoundException("Not found student");
        }
        student.get().setPoints(student.get().getPoints()+ request.getTotalScore());
        studentRepository.save(student.get());
        CompetitionParticipant competitionParticipant = competitionParticipantMapper.toCompetitionParticipant(request, null);
        competitionParticipant.setStudent(student.get());
        competitionParticipant.setCompetition(competition.get());
        competitionParticipantRepository.save(competitionParticipant);
        CompetitionParticipantResponseDto response = competitionParticipantMapper.toResponse(competitionParticipant);
        StudentResponseDto studentResponseDto = studentMapper.toStudentResponse(student.get());
        response.setStudent(studentResponseDto);
        return response;
    }

    @Override
    public List<CompetitionResponseDto> getListCompetition(String partnerId) {
        Optional<Partner> partner = partnerRepository.findById(partnerId);
        if (partner.isEmpty()) {
            throw new NotFoundException("Not found partner");
        }
        List<Competition> competitions = competitionRepository.findByPartnerId(partnerId);
        List<CompetitionResponseDto> responses = new ArrayList<>();
        competitions.forEach(competition -> {
            Optional<CompetitionLink> competitionLink = competitionLinkRepository.findByCompetitionId(competition.getId());
            if (competitionLink.isPresent()) {
                CompetitionResponseDto competitionResponseDto = competitionMapper.toCompetitionResponse(competition);
                competitionResponseDto.setScore(competitionLink.get().getScore());
                if (competitionLink.get().getGameRound() != null) {
                    GameRound gameRound = competitionLink.get().getGameRound();
                    GameRoundResponseDto gameRoundResponseDto = gameRoundMapper.toGameRoundResponse(gameRound);
                    gameRoundResponseDto.setPartnerId(partnerId);
                    competitionResponseDto.setGameRound(gameRoundResponseDto);
                    responses.add(competitionResponseDto);
                } else if (competitionLink.get().getQuizTemplate() != null) {
                    QuizTemplate quizTemplate = competitionLink.get().getQuizTemplate();
                    QuizTemplateResponseDto quizTemplateResponseDto = toDetailResponse(quizTemplate, true);
                    competitionResponseDto.setQuizTemplate(quizTemplateResponseDto);
                    responses.add(competitionResponseDto);
                }
            }
        });
        return responses;
    }

    @Override
    public List<CompetitionParticipantResponseDto> getListParticipant(String competitionId) {
        Optional<Competition> competition = competitionRepository.findById(competitionId);
        if (competition.isEmpty()) {
            throw new NotFoundException("Not found competition");
        }
        List<CompetitionParticipant> competitionParticipants = competitionParticipantRepository.findByCompetitionId(competitionId);
        List<CompetitionParticipantResponseDto> responses = new ArrayList<>();
        competitionParticipants.forEach(competitionParticipant -> {
            CompetitionParticipantResponseDto competitionParticipantResponseDto = competitionParticipantMapper.toResponse(competitionParticipant);
            Student student = competitionParticipant.getStudent();
            StudentResponseDto studentResponseDto = studentMapper.toStudentResponse(student);
            competitionParticipantResponseDto.setStudent(studentResponseDto);
            responses.add(competitionParticipantResponseDto);
        });
        return responses;
    }

    @Override
    public CompetitionResponseDto updateCompetition(String competitionId, CompetitionRequestDto request) {
        Optional<Competition> competition = competitionRepository.findById(competitionId);
        if (competition.isEmpty()) {
            throw new NotFoundException("Not found competition");
        }
        Competition updatedCompetition = competition.get();
        competitionMapper.updateCompetition(updatedCompetition, request);
        updatedCompetition.setStatus(request.getStatus());
        competitionRepository.save(updatedCompetition);
        Optional<CompetitionLink> competitionLink = competitionLinkRepository.findByCompetitionId(updatedCompetition.getId());
        if (competitionLink.isEmpty()) {
            throw new NotFoundException("Not found competition link");
        }
        GameRound gameRound = competitionLink.get().getGameRound();
        GameRoundResponseDto gameRoundResponseDto = gameRoundMapper.toGameRoundResponse(gameRound);
        CompetitionResponseDto response = competitionMapper.toCompetitionResponse(updatedCompetition);
        response.setGameRound(gameRoundResponseDto);
        response.setScore(competitionLink.get().getScore());
        return response;
    }

    @Override
    public CompetitionResponseDto deleteCompetition(String competitionId) {
        Optional<Competition> competition = competitionRepository.findById(competitionId);
        if (competition.isEmpty()) {
            throw new NotFoundException("Not found competition");
        }
        competitionRepository.delete(competition.get());
        return competitionMapper.toCompetitionResponse(competition.get());
    }

    @Override
    public CompetitionParticipantResponseDto checkParticipant(String studentId, String competitionId) {
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isEmpty()) {
            throw new NotFoundException("Not found student");
        }
        Optional<Competition> competition = competitionRepository.findById(competitionId);
        if (competition.isEmpty()) {
            throw new NotFoundException("Not found competition");
        }
        Optional<CompetitionParticipant> competitionParticipant = competitionParticipantRepository.findByCompetitionAndStudent(competitionId, studentId);
        return competitionParticipant.map(competitionParticipantMapper::toResponse).orElse(null);
    }

    private QuizTemplateResponseDto toDetailResponse(QuizTemplate t, boolean includeQuestions) {
        return QuizTemplateResponseDto.builder()
                .id(t.getId())
                .title(t.getTitle())
                .description(t.getDescription())
                .questionCount(t.getQuestions() != null ? t.getQuestions().size() : 0)
                .active(t.getActive())
                .partnerId(t.getPartner() != null ? t.getPartner().getId() : null)
                .partnerName(t.getPartner() != null ? t.getPartner().getOrganizationName() : null)
                .isCompetition(t.getIsCompetition())
                .questions(includeQuestions && t.getQuestions() != null ? t.getQuestions().stream()
                        .map(q -> QuizTemplateResponseDto.QuestionResponseDto.builder()
                                .id(q.getId())
                                .text(q.getText())
                                .options(parseOptions(q.getOptionsJson()))
                                .correctAnswer(q.getCorrectAnswer())
                                .explanation(q.getExplanation())
                                .build())
                        .collect(Collectors.toList()) : null)
                .createdAt(t.getCreatedAt())
                .updatedAt(t.getUpdatedAt())
                .build();
    }

    private List<String> parseOptions(String json) {
        if (json == null) return List.of();
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            return List.of();
        }
    }

}
