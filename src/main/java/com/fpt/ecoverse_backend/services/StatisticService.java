package com.fpt.ecoverse_backend.services;

import com.fpt.ecoverse_backend.dtos.StatisticPartner;
import com.fpt.ecoverse_backend.dtos.StatisticStudent;

public interface StatisticService {
    StatisticStudent getStudentStatistic(String studentId);
    StatisticPartner getPartnerStatistic(String partnerId);
}
