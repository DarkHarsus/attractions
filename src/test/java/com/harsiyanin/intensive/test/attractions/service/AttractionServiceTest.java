package com.harsiyanin.intensive.test.attractions.service;

import com.harsiyanin.intensive.test.attractions.dtos.AttractionCreateDto;
import com.harsiyanin.intensive.test.attractions.dtos.AttractionDto;
import com.harsiyanin.intensive.test.attractions.dtos.LocalityDto;
import com.harsiyanin.intensive.test.attractions.entities.Assistance;
import com.harsiyanin.intensive.test.attractions.entities.Attraction;
import com.harsiyanin.intensive.test.attractions.entities.AttractionType;
import com.harsiyanin.intensive.test.attractions.entities.Locality;
import com.harsiyanin.intensive.test.attractions.mappers.AttractionMapper;
import com.harsiyanin.intensive.test.attractions.repositories.AssistanceRepository;
import com.harsiyanin.intensive.test.attractions.repositories.AttractionRepository;
import com.harsiyanin.intensive.test.attractions.repositories.LocalityRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AttractionServiceTest {

    @Mock
    private AttractionRepository attractionRepository;

    @Mock
    private AssistanceRepository assistanceRepository;

    @Mock
    private LocalityRepository localityRepository;

    @Mock
    private AttractionMapper attractionMapper;

    @InjectMocks
    private AttractionServiceImpl attractionService;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddAttraction() {
        Long localityId = 1L;
        AttractionCreateDto attractionCreateDto = new AttractionCreateDto(
                "Museum", LocalDate.now(), "A historical museum", AttractionType.MUSEUM, 1L);
        AttractionDto expectedAttractionDto = new AttractionDto(
                1L, "Museum", LocalDate.now(), "A historical museum", AttractionType.MUSEUM, Collections.emptyList(), 1L);
        Locality locality = new Locality();
        locality.setId(localityId);
        Attraction attraction = new Attraction(1L, "Museum", LocalDate.now(), "A historical museum", AttractionType.MUSEUM, locality, List.of());

        when(localityRepository.findById(attractionCreateDto.getLocalityId())).thenReturn(Optional.of(locality));
        when(attractionMapper.toEntity(attractionCreateDto)).thenReturn(attraction);
        when(attractionRepository.save(any(Attraction.class))).thenReturn(attraction);
        when(attractionMapper.toDto(attraction)).thenReturn(expectedAttractionDto);

        AttractionDto result = attractionService.addAttraction(attractionCreateDto);

        assertNotNull(result);
        assertEquals(expectedAttractionDto.getName(), result.getName());
        verify(attractionRepository, times(1)).save(any(Attraction.class));
        verify(localityRepository, times(1)).findById(attractionCreateDto.getLocalityId());
    }


    @Test
    void testGetAllAttractions() {
        Pageable pageable = PageRequest.of(0, 5);
        Attraction attraction = new Attraction(1L, "Museum", LocalDate.now(), "A historical museum", AttractionType.MUSEUM, new Locality(), List.of());
        List<Attraction> attractions = List.of(attraction);

        when(attractionRepository.findByType(AttractionType.MUSEUM, pageable)).thenReturn(new PageImpl<>(attractions));

        List<AttractionDto> result = attractionService.getAllAttractions(AttractionType.MUSEUM, pageable);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(attraction.getName(), result.get(0).getName());
        verify(attractionRepository, times(1)).findByType(AttractionType.MUSEUM, pageable);
    }

    @Test
    void testGetAttractionsByLocality() {
        Long localityId = 1L;
        Attraction attraction = new Attraction(1L, "Museum", LocalDate.now(), "A historical museum", AttractionType.MUSEUM, new Locality(), List.of());
        when(attractionRepository.findByLocalityId(localityId)).thenReturn(List.of(attraction));

        List<AttractionDto> result = attractionService.getAttractionsByLocality(localityId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(attractionRepository, times(1)).findByLocalityId(localityId);
    }

    @Test
    void testUpdateAttractionDescription() {
        Long attractionId = 1L;
        String newDescription = "Updated description";
        Attraction attraction = new Attraction(1L, "Museum", LocalDate.now(), "A historical museum", AttractionType.MUSEUM, new Locality(), List.of());

        when(attractionRepository.findById(attractionId)).thenReturn(Optional.of(attraction));
        when(attractionRepository.save(any())).thenReturn(attraction);

        AttractionDto result = attractionService.updateAttractionDescription(attractionId, newDescription);

        assertNotNull(result);
        assertEquals(newDescription, result.getDescription());
        verify(attractionRepository, times(1)).findById(attractionId);
        verify(attractionRepository, times(1)).save(any());
    }


@Test
void testDeleteAttraction() {
    Long nonExistentAttractionId = 999L;

    when(attractionRepository.findById(nonExistentAttractionId)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> {
        attractionService.deleteAttraction(nonExistentAttractionId);
    });

    verify(attractionRepository, never()).delete(any(Attraction.class));
    verify(assistanceRepository, never()).save(any(Assistance.class));
}

    @Test
    void testAddLocality() {
        Locality locality = new Locality(1L, "CityName", "RegionName", Collections.emptyList(), 45.0, 75.0);
        LocalityDto localityDto = new LocalityDto(1L, "CityName", "RegionName", Collections.emptyList(), 45.0, 75.0);
        when(localityRepository.save(any())).thenReturn(locality);

        LocalityDto result = attractionService.addLocality(localityDto);

        assertNotNull(result);
        assertEquals(locality.getCity(), result.getCity());
        verify(localityRepository, times(1)).save(any());
    }
}
