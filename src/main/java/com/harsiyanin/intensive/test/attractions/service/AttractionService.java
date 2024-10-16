package com.harsiyanin.intensive.test.attractions.service;



import com.harsiyanin.intensive.test.attractions.dtos.AttractionCreateDto;
import com.harsiyanin.intensive.test.attractions.dtos.AttractionDto;
import com.harsiyanin.intensive.test.attractions.dtos.LocalityDto;
import com.harsiyanin.intensive.test.attractions.entities.AttractionType;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AttractionService {
    AttractionDto addAttraction(AttractionCreateDto attractionCreateDto);
    List<AttractionDto> getAllAttractions(AttractionType filterByType, Pageable pageable);
    List<AttractionDto> getAttractionsByLocality(Long localityId);
    AttractionDto updateAttractionDescription(Long attractionId, String newDescription);
    void deleteAttraction(Long attractionId);
    LocalityDto addLocality(LocalityDto localityDto);
}

