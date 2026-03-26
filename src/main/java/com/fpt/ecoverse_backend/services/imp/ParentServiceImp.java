package com.fpt.ecoverse_backend.services.imp;

import com.fpt.ecoverse_backend.dtos.responses.ParentResponseDto;
import com.fpt.ecoverse_backend.mappers.ParentMapper;
import com.fpt.ecoverse_backend.repositories.ParentRepository;
import com.fpt.ecoverse_backend.repositories.StudentRepository;
import com.fpt.ecoverse_backend.services.ParentService;
import org.springframework.stereotype.Service;

@Service
public class ParentServiceImp implements ParentService {

    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;
    private final ParentMapper parentMapper;

    public ParentServiceImp(ParentRepository parentRepository, StudentRepository studentRepository, ParentMapper parentMapper) {
        this.parentRepository = parentRepository;
        this.studentRepository = studentRepository;
        this.parentMapper = parentMapper;
    }

    @Override
    public ParentResponseDto linkParentToStudent(String parentId, String studentId) {
        
        return null;
    }
}
