package com.fpt.ecoverse_backend.services.imp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.ecoverse_backend.dtos.StatisticStudent;
import com.fpt.ecoverse_backend.dtos.requests.StudentRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.*;
import com.fpt.ecoverse_backend.entities.*;
import com.fpt.ecoverse_backend.exceptions.NotFoundException;
import com.fpt.ecoverse_backend.mappers.*;
import com.fpt.ecoverse_backend.repositories.*;
import com.fpt.ecoverse_backend.services.StatisticService;
import com.fpt.ecoverse_backend.services.StudentService;
import com.fpt.ecoverse_backend.utils.UploadFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentServiceImp implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final UploadFile uploadFile;
    private final StatisticService statisticService;
    private final WasteItemRepository wasteItemRepository;
    private final WasteItemMapper wasteItemMapper;
    private final CompetitionRepository competitionRepository;
    private final CompetitionLinkRepository competitionLinkRepository;
    private final GameRoundMapper gameRoundMapper;
    private final CompetitionMapper competitionMapper;
    private final ObjectMapper objectMapper;

    public StudentServiceImp(StudentRepository studentRepository, StudentMapper studentMapper, UserMapper userMapper, UserRepository userRepository, UploadFile uploadFile, StatisticService statisticService, WasteItemRepository wasteItemRepository, WasteItemMapper wasteItemMapper, CompetitionRepository competitionRepository, CompetitionLinkRepository competitionLinkRepository, GameRoundMapper gameRoundMapper, CompetitionMapper competitionMapper, ObjectMapper objectMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.uploadFile = uploadFile;
        this.statisticService = statisticService;
        this.wasteItemRepository = wasteItemRepository;
        this.wasteItemMapper = wasteItemMapper;
        this.competitionRepository = competitionRepository;
        this.competitionLinkRepository = competitionLinkRepository;
        this.gameRoundMapper = gameRoundMapper;
        this.competitionMapper = competitionMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public StudentResponseDto getStudentDetails(String studentId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isEmpty()) {
            throw new NotFoundException("Student not found");
        }
        StatisticStudent statisticStudent = statisticService.getStudentStatistic(studentId);
        StudentResponseDto response = studentMapper.toStudentResponse(studentOpt.get());
        response.setStatistics(statisticStudent);
        return response;
    }

    @Override
    public StudentResponseDto updateStudentDetails(String studentId, StudentRequestDto request) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isEmpty()) {
            throw new NotFoundException("Student not found");
        }
        Student student = studentOpt.get();
        studentMapper.updateStudent(student, request);
        User user = student.getUser();
        userMapper.updateStudent(user, request);
        userRepository.save(user);
        studentRepository.save(student);
        return studentMapper.toStudentResponse(student);
    }

    @Override
    public StudentResponseDto deleteStudent(String studentId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isEmpty()) {
            throw new NotFoundException("Student not found");
        }
        Student student = studentOpt.get();
        studentRepository.delete(student);
        userRepository.deleteById(studentId);
        return studentMapper.toStudentResponse(student);
    }

    @Override
    public List<WasteItemResponseDto> getWasteItemByAI(String studentId) {
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isEmpty()) {
            throw new NotFoundException("Not found student");
        }
        List<WasteItem> wasteItems = wasteItemRepository.findWasteItemByAI(studentId);
        return wasteItems.stream().map(wasteItem -> wasteItemMapper.toWasteItemResponse(wasteItem, null)).toList();
    }

    @Override
    public StudentResponseDto updateAvatar(String studentId, MultipartFile file) {
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isEmpty()) {
            throw new NotFoundException("Not found student");
        }
        if (file != null) {
            String avatar = uploadFile.imageToUrl(file);
            student.get().getUser().setAvatarUrl(avatar);
            studentRepository.save(student.get());
            userRepository.save(student.get().getUser());
        }
        return studentMapper.toStudentResponse(student.get());
    }

    @Override
    public List<CompetitionResponseDto> getCompetitionsForStudent(String studentId) {
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isEmpty()) {
            throw new NotFoundException("Not found student");
        }
        Partner partner = student.get().getPartner();
        String targetScopeClass = student.get().getGrade();
        List<Competition> competitions = competitionRepository.findCompetitionsForStudent(partner.getId(), targetScopeClass);
        List<CompetitionResponseDto> responses = new ArrayList<>();
        competitions.forEach(competition -> {
            Optional<CompetitionLink> competitionLink = competitionLinkRepository.findByCompetitionId(competition.getId());
            if (competitionLink.isPresent()) {
                CompetitionResponseDto competitionResponseDto = competitionMapper.toCompetitionResponse(competition);
                competitionResponseDto.setScore(competitionLink.get().getScore());
                if (competitionLink.get().getGameRound() != null) {
                    GameRound gameRound = competitionLink.get().getGameRound();
                    GameRoundResponseDto gameRoundResponseDto = gameRoundMapper.toGameRoundResponse(gameRound);
                    gameRoundResponseDto.setPartnerId(partner.getId());
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
