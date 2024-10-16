package com.harsiyanin.intensive.test.attractions.service;

import com.harsiyanin.intensive.test.attractions.dtos.AssistanceDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AssistanceService {
    AssistanceDto addAssistance(AssistanceDto assistanceDto);
    AssistanceDto getAssistance(Long assistanceId);
    List<AssistanceDto> getAllAssistance(Pageable pageable);
    List<AssistanceDto> getAssistanceByAttraction(Long attractionId);
    void addAssistanceToAttraction(Long assistanceId, Long attractionId);
    void deleteAssistance(Long assistanceId);
}
