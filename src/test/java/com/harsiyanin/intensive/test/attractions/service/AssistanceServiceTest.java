package com.harsiyanin.intensive.test.attractions.service;

import com.harsiyanin.intensive.test.attractions.dtos.AssistanceDto;
import com.harsiyanin.intensive.test.attractions.entities.Assistance;
import com.harsiyanin.intensive.test.attractions.entities.Attraction;
import com.harsiyanin.intensive.test.attractions.mappers.AssistanceMapper;
import com.harsiyanin.intensive.test.attractions.repositories.AssistanceRepository;
import com.harsiyanin.intensive.test.attractions.repositories.AttractionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AssistanceServiceTest {
    @Mock
    private AssistanceRepository assistanceRepository;

    @Mock
    private AttractionRepository attractionRepository;

    @InjectMocks
    private AssistanceServiceImpl assistanceService;

    private AssistanceMapper assistanceMapper = AssistanceMapper.INSTANCE;

    private Assistance assistance;

    private AssistanceDto assistanceDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        assistance = new Assistance();
        assistance.setId(1L);
        assistanceDto = assistanceMapper.toDto(assistance);
    }

    @Test
    void testAddAssistance() {
        when(assistanceRepository.save(any(Assistance.class))).thenReturn(assistance);

        AssistanceDto result = assistanceService.addAssistance(assistanceDto);

        assertNotNull(result);
        assertEquals(assistance.getId(), result.getId());
        verify(assistanceRepository, times(1)).save(any(Assistance.class));
    }

    @Test
    void testGetAssistance() {
        when(assistanceRepository.findById(1L)).thenReturn(Optional.of(assistance));

        AssistanceDto result = assistanceService.getAssistance(1L);

        assertNotNull(result);
        assertEquals(assistance.getId(), result.getId());
        verify(assistanceRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllAssistance() {
        Page<Assistance> assistancePage = mock(Page.class);
        when(assistanceRepository.findAll(any(Pageable.class))).thenReturn(assistancePage);
        when(assistancePage.stream()).thenReturn(Collections.singletonList(assistance).stream());

        List<AssistanceDto> result = assistanceService.getAllAssistance(Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(assistanceRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testGetAssistanceByAttraction() {
        Attraction attraction = new Attraction();
        attraction.setId(1L);
        attraction.setAssistanceList(Collections.singletonList(assistance));

        when(attractionRepository.findById(1L)).thenReturn(Optional.of(attraction));

        List<AssistanceDto> result = assistanceService.getAssistanceByAttraction(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(assistance.getId(), result.get(0).getId());
        verify(attractionRepository, times(1)).findById(1L);
    }

    @Test
    void testAddAssistanceToAttraction() {
        Attraction attraction = new Attraction();
        attraction.setId(1L);
        when(assistanceRepository.findById(1L)).thenReturn(Optional.of(assistance));
        when(attractionRepository.findById(1L)).thenReturn(Optional.of(attraction));

        assistanceService.addAssistanceToAttraction(1L, 1L);

        assertTrue(attraction.getAssistanceList().contains(assistance));
        verify(attractionRepository, times(1)).save(attraction);
    }

    @Test
    void testDeleteAssistance() {
        when(assistanceRepository.findById(1L)).thenReturn(Optional.of(assistance));

        assistanceService.deleteAssistance(1L);

        verify(assistanceRepository, times(1)).findById(1L);
    }
}