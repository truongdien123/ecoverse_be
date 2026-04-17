package com.fpt.ecoverse_backend.services;

import com.fpt.ecoverse_backend.dtos.requests.*;
import com.fpt.ecoverse_backend.dtos.responses.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PartnerService {
    PartnerResponseDto createPartner(PartnerRegisterRequestDto request);
    PartnerResponseDto getDetailPartner(String partnerId);
    PartnerResponseDto updatePartner(String partnerId, PartnerUpdateRequestDto request);
    BulkCreateReportResponseDto bulkCreate(byte[] bytes, String partnerId);
    StudentResponseDto getStudentDetail(String partnerId, String studentId);
    StudentResponseDto changeStatusStudent(String partnerId, String studentId, String status);
    UserListResponseDto<?> getListUser(String partnerId, PageFilterRequestDto pageFilterRequestDto);
    List<StudentResponseDto> createStudents(String partnerId, List<StudentRequestDto> studentRequestDtos);
    PartnerResponseDto deletePartner(String partnerId);
    List<ParentResponseDto> createParents(String partnerId, List<ParentRequestDto> request);
    StudentResponseDto deleteStudent(String partnerId, String studentId);
    ParentResponseDto deleteParent(String partnerId, String parentId);
}
