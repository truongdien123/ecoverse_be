package com.fpt.ecoverse_backend.services.imp;

import com.fpt.ecoverse_backend.dtos.requests.ParentRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.ParentResponseDto;
import com.fpt.ecoverse_backend.dtos.responses.StudentResponseDto;
import com.fpt.ecoverse_backend.entities.Parent;
import com.fpt.ecoverse_backend.entities.Student;
import com.fpt.ecoverse_backend.entities.User;
import com.fpt.ecoverse_backend.exceptions.BadRequestException;
import com.fpt.ecoverse_backend.exceptions.NotFoundException;
import com.fpt.ecoverse_backend.mappers.ParentMapper;
import com.fpt.ecoverse_backend.mappers.StudentMapper;
import com.fpt.ecoverse_backend.mappers.UserMapper;
import com.fpt.ecoverse_backend.repositories.ParentRepository;
import com.fpt.ecoverse_backend.repositories.StudentRepository;
import com.fpt.ecoverse_backend.repositories.UserRepository;
import com.fpt.ecoverse_backend.services.ParentService;
import com.fpt.ecoverse_backend.utils.UploadFile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParentServiceImp implements ParentService {

    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;
    private final ParentMapper parentMapper;
    private final StudentMapper studentMapper;
    private final UserMapper userMapper;
    private final UploadFile uploadFile;
    private final UserRepository userRepository;

    public ParentServiceImp(ParentRepository parentRepository, StudentRepository studentRepository, ParentMapper parentMapper, StudentMapper studentMapper, UserMapper userMapper, UploadFile uploadFile, UserRepository userRepository) {
        this.parentRepository = parentRepository;
        this.studentRepository = studentRepository;
        this.parentMapper = parentMapper;
        this.studentMapper = studentMapper;
        this.userMapper = userMapper;
        this.uploadFile = uploadFile;
        this.userRepository = userRepository;
    }

    @Override
    public ParentResponseDto linkParentToStudent(String parentId, String studentId) {
        Optional<Parent> parentOpt = parentRepository.findById(parentId);
        if (parentOpt.isEmpty()) {
            throw new NotFoundException("Parent not found");
        }
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isEmpty()) {
            throw new NotFoundException("Student not found");
        }
        Student student = studentOpt.get();
        if (student.getParent() != null) {
            throw new BadRequestException("Student already has a parent");
        }
        student.setParent(parentOpt.get());
        studentRepository.save(student);
        List<Student> students = studentRepository.findByParentId(parentId);
        ParentResponseDto response = parentMapper.toParentResponse(parentOpt.get(), parentOpt.get().getUser());
        List<StudentResponseDto> studentResponseDtos = students.stream()
                .map(s -> studentMapper.toStudentResponse(s, s.getUser()))
                .toList();
        response.setStudents(studentResponseDtos);
        return response;
    }

    @Override
    public List<StudentResponseDto> getListStudent(String parentId) {
        Optional<Parent> parent = parentRepository.findById(parentId);
        if (parent.isEmpty()) {
            throw new NotFoundException("Parent not found");
        }
        List<Student> students = studentRepository.findByParentId(parentId);
        return students.stream().map(st -> studentMapper.toStudentResponse(st, st.getUser())).toList();
    }

    @Override
    public ParentResponseDto getParentDetail(String parentId) {
        Optional<Parent> parent = parentRepository.findById(parentId);
        if (parent.isEmpty()) {
            throw new NotFoundException("Parent not found");
        }
        return parentMapper.toParentResponse(parent.get(), parent.get().getUser());
    }

    @Override
    public ParentResponseDto updateParent(String parentId, ParentRequestDto request) {
        Optional<Parent> parent = parentRepository.findById(parentId);
        if (parent.isEmpty()) {
            throw new NotFoundException("Parent not found");
        }
        User user = userMapper.toUser(request, parentId, uploadFile);
        userRepository.save(user);
        return parentMapper.toParentResponse(parent.get(), user);
    }

    @Override
    public ParentResponseDto deleteParent(String parentId) {
        Optional<Parent> parentOpt = parentRepository.findById(parentId);
        if (parentOpt.isEmpty()) {
            throw new NotFoundException("Parent not found");
        }
        parentRepository.delete(parentOpt.get());
        userRepository.delete(parentOpt.get().getUser());
        return parentMapper.toParentResponse(parentOpt.get(), parentOpt.get().getUser());
    }


}
