package com.fpt.ecoverse_backend.services.imp;

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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public CompetitionServiceImp(CompetitionRepository competitionRepository, PartnerRepository partnerRepository, CompetitionMapper competitionMapper, QuizTemplateRepository quizTemplateRepository, GameRoundRepository gameRoundRepository, CompetitionLinkRepository competitionLinkRepository, CompetitionLinkMapper competitionLinkMapper, StudentRepository studentRepository, CompetitionParticipantMapper competitionParticipantMapper, CompetitionParticipantRepository competitionParticipantRepository, StudentMapper studentMapper, GameRoundMapper gameRoundMapper) {
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
    }

    @Override
    public CompetitionResponseDto createCompetition(String partnerId, CompetitionRequestDto request) {
        Optional<Partner> partner = partnerRepository.findById(partnerId);
        if (partner.isEmpty()) {
            throw new NotFoundException("Not found partner");
        }
        Competition competition = competitionMapper.toCompetition(request, null);
        competition.setPartner(partner.get());
        return competitionMapper.toCompetitionResponse(competition);
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
        if (competition.get().getEndTime() != null && LocalDateTime.now().isAfter(competition.get().getEndTime())) {
            throw new BadRequestException("Competition has ended");
        }
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isEmpty()) {
            throw new NotFoundException("Not found student");
        }
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
            if (competitionLink.isEmpty()) {
                throw new NotFoundException("Not found competition link");
            }
            GameRound gameRound = competitionLink.get().getGameRound();
            GameRoundResponseDto gameRoundResponseDto = gameRoundMapper.toGameRoundResponse(gameRound);
            CompetitionResponseDto competitionResponseDto = competitionMapper.toCompetitionResponse(competition);
            competitionResponseDto.setGameRound(gameRoundResponseDto);
            responses.add(competitionResponseDto);
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

}
