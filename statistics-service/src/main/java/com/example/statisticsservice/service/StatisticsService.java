package com.example.statisticsservice.service;

import com.example.response.CategoryCountProjection;
import com.example.response.DailyCountProjection;
import com.example.statisticsservice.repository.AuthClient;
import com.example.statisticsservice.repository.StudyClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final AuthClient authClient;
    private final StudyClient studyClient;

    @PreAuthorize("hasRole('ADMIN')")
    public List<DailyCountProjection> userLast7Days() {
        return authClient.userLast7Days().getResultList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<DailyCountProjection> documentLast7Days() {
        return studyClient.documentLast7Days().getResultList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<CategoryCountProjection> documentByCategory() {
        return studyClient.documentByCategory().getResultList();
    }

}
