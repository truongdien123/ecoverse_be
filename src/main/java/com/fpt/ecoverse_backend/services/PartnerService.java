package com.fpt.ecoverse_backend.services;

import com.fpt.ecoverse_backend.dtos.requests.PageFilterRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.PartnerRegisterRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.PartnerUpdateRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.StudentRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.BulkCreateReportResponseDto;
import com.fpt.ecoverse_backend.dtos.responses.PartnerResponseDto;
import com.fpt.ecoverse_backend.dtos.responses.StudentResponseDto;
import com.fpt.ecoverse_backend.dtos.responses.UserListResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PartnerService {
    PartnerResponseDto createPartner(PartnerRegisterRequestDto request);
    PartnerResponseDto getDetailPartner(String partnerId);
    PartnerResponseDto updatePartner(String partnerId, PartnerUpdateRequestDto request);
    BulkCreateReportResponseDto bulkCreate(MultipartFile file, String partnerId);
    StudentResponseDto getStudentDetail(String partnerId, String studentId);
    StudentResponseDto changeStatusStudent(String partnerId, String studentId, String status);
    UserListResponseDto<?> getListUser(String partnerId, PageFilterRequestDto pageFilterRequestDto);
    List<StudentResponseDto> createStudents(String partnerId, List<StudentRequestDto> studentRequestDtos);
}
