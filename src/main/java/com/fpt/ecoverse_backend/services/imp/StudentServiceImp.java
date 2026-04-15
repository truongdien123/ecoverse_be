package com.fpt.ecoverse_backend.services.imp;

import com.fpt.ecoverse_backend.dtos.StatisticStudent;
import com.fpt.ecoverse_backend.dtos.requests.StudentRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.StudentResponseDto;
import com.fpt.ecoverse_backend.entities.Student;
import com.fpt.ecoverse_backend.entities.User;
import com.fpt.ecoverse_backend.exceptions.NotFoundException;
import com.fpt.ecoverse_backend.mappers.StudentMapper;
import com.fpt.ecoverse_backend.mappers.UserMapper;
import com.fpt.ecoverse_backend.repositories.StudentRepository;
import com.fpt.ecoverse_backend.repositories.UserRepository;
import com.fpt.ecoverse_backend.services.StatisticService;
import com.fpt.ecoverse_backend.services.StudentService;
import com.fpt.ecoverse_backend.utils.UploadFile;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentServiceImp implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final UploadFile uploadFile;
    private final StatisticService statisticService;

    public StudentServiceImp(StudentRepository studentRepository, StudentMapper studentMapper, UserMapper userMapper, UserRepository userRepository, UploadFile uploadFile, StatisticService statisticService) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.uploadFile = uploadFile;
        this.statisticService = statisticService;
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
}
