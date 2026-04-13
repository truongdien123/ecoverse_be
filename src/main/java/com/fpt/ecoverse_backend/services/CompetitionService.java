package com.fpt.ecoverse_backend.services;

import com.fpt.ecoverse_backend.dtos.requests.CompetitionLinkRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.CompetitionParticipantRequestDto;
import com.fpt.ecoverse_backend.dtos.requests.CompetitionRequestDto;
import com.fpt.ecoverse_backend.dtos.responses.CompetitionLinkResponseDto;
import com.fpt.ecoverse_backend.dtos.responses.CompetitionParticipantResponseDto;
import com.fpt.ecoverse_backend.dtos.responses.CompetitionResponseDto;

import java.util.List;

public interface CompetitionService {
    CompetitionResponseDto createCompetition(String partnerId, CompetitionRequestDto request);
    CompetitionLinkResponseDto linkCompetition(String competitionId, CompetitionLinkRequestDto request);
    CompetitionParticipantResponseDto createCompetitionParticipant(String competitionId, String studentId, CompetitionParticipantRequestDto request);
    List<CompetitionResponseDto> getListCompetition(String partnerId);
    List<CompetitionParticipantResponseDto> getListParticipant(String competitionId);
}
