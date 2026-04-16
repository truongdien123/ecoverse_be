package com.fpt.ecoverse_backend.services.imp;

import com.fpt.ecoverse_backend.dtos.StatisticStudent;
import com.fpt.ecoverse_backend.dtos.requests.StudentRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.StudentResponseDto;
import com.fpt.ecoverse_backend.dtos.responses.WasteItemResponseDto;
import com.fpt.ecoverse_backend.entities.Student;
import com.fpt.ecoverse_backend.entities.User;
import com.fpt.ecoverse_backend.entities.WasteItem;
import com.fpt.ecoverse_backend.exceptions.NotFoundException;
import com.fpt.ecoverse_backend.mappers.StudentMapper;
import com.fpt.ecoverse_backend.mappers.UserMapper;
import com.fpt.ecoverse_backend.mappers.WasteItemMapper;
import com.fpt.ecoverse_backend.repositories.StudentRepository;
import com.fpt.ecoverse_backend.repositories.UserRepository;
import com.fpt.ecoverse_backend.repositories.WasteItemRepository;
import com.fpt.ecoverse_backend.services.StatisticService;
import com.fpt.ecoverse_backend.services.StudentService;
import com.fpt.ecoverse_backend.utils.UploadFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

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

    public StudentServiceImp(StudentRepository studentRepository, StudentMapper studentMapper, UserMapper userMapper, UserRepository userRepository, UploadFile uploadFile, StatisticService statisticService, WasteItemRepository wasteItemRepository, WasteItemMapper wasteItemMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.uploadFile = uploadFile;
        this.statisticService = statisticService;
        this.wasteItemRepository = wasteItemRepository;
        this.wasteItemMapper = wasteItemMapper;
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
        Student student = studentMapper.toStudent(request, studentOpt.get().getId());
        User user = userMapper.toUser(request, studentOpt.get().getId());
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
}
