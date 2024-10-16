package com.harsiyanin.intensive.test.attractions.service;

import com.harsiyanin.intensive.test.attractions.dtos.AttractionCreateDto;
import com.harsiyanin.intensive.test.attractions.dtos.AttractionDto;
import com.harsiyanin.intensive.test.attractions.dtos.LocalityDto;
import com.harsiyanin.intensive.test.attractions.entities.Assistance;
import com.harsiyanin.intensive.test.attractions.entities.Attraction;
import com.harsiyanin.intensive.test.attractions.entities.AttractionType;
import com.harsiyanin.intensive.test.attractions.entities.Locality;
import com.harsiyanin.intensive.test.attractions.mappers.AttractionMapper;
import com.harsiyanin.intensive.test.attractions.mappers.LocalityMapper;
import com.harsiyanin.intensive.test.attractions.repositories.AssistanceRepository;
import com.harsiyanin.intensive.test.attractions.repositories.AttractionRepository;
import com.harsiyanin.intensive.test.attractions.repositories.LocalityRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class AttractionServiceImpl implements AttractionService {

    private final AttractionRepository attractionRepository;
    private final LocalityRepository localityRepository;
    private final AssistanceRepository assistanceRepository;
    private final AttractionMapper attractionMapper = AttractionMapper.INSTANCE;
    private final LocalityMapper localityMapper = LocalityMapper.INSTANCE;

    @Autowired
    public AttractionServiceImpl(AttractionRepository attractionRepository, LocalityRepository localityRepository, AssistanceRepository assistanceRepository) {
        this.attractionRepository = attractionRepository;
        this.localityRepository = localityRepository;
        this.assistanceRepository = assistanceRepository;
    }

    /**
     * Adds a new attraction to the database.
     *
     * @param attractionCreateDto the DTO containing details of the attraction to be added
     * @return the created AttractionDto
     * @throws EntityNotFoundException if the locality associated with the attraction is not found
     */
    @Override
    public AttractionDto addAttraction(AttractionCreateDto attractionCreateDto) {
        log.info("Adding attraction: {}", attractionCreateDto);
        Long localityId = attractionCreateDto.getLocalityId();
        Locality locality = localityRepository.findById(localityId)
                .orElseThrow(() -> {
                    log.error("Locality with id {} not found", localityId);
                    return new EntityNotFoundException("Locality with id " + localityId + " not found");
                });
        Attraction attraction = attractionMapper.toEntity(attractionCreateDto);
        attraction.setLocality(locality);
        Attraction savedAttraction = attractionRepository.save(attraction);
        log.info("Attraction saved successfully with id: {}", savedAttraction.getId());
        return attractionMapper.toDto(savedAttraction);
    }

    /**
     * Retrieves a list of attractions filtered by their type.
     *
     * @param filterByType the type of attractions to filter
     * @param pageable     pagination information
     * @return a list of filtered AttractionDto objects
     */
    @Override
    public List<AttractionDto> getAllAttractions(AttractionType filterByType, Pageable pageable) {
        log.info("Fetching attractions by type: {}", filterByType);
        Page<Attraction> attractions = attractionRepository.findByType(filterByType, pageable);
        return attractions.stream()
                .map(attractionMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves attractions by their associated locality ID.
     *
     * @param localityId the ID of the locality
     * @return a list of AttractionDto objects for the specified locality
     */
    @Override
    public List<AttractionDto> getAttractionsByLocality(Long localityId) {
        log.info("Fetching attractions for locality with id: {}", localityId);
        List<Attraction> attractions = attractionRepository.findByLocalityId(localityId);
        return attractions.stream()
                .map(attractionMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Updates the description of an existing attraction.
     *
     * @param attractionId   the ID of the attraction to be updated
     * @param newDescription the new description for the attraction
     * @return the updated AttractionDto
     * @throws EntityNotFoundException if the attraction is not found
     */
    @Override
    public AttractionDto updateAttractionDescription(Long attractionId, String newDescription) {
        log.info("Updating description for attraction with id: {}", attractionId);
        Attraction attraction = attractionRepository.findById(attractionId)
                .orElseThrow(() -> {
                    log.error("Attraction with id {} not found", attractionId);
                    return new EntityNotFoundException("Attraction with id " + attractionId + " not found.");
                });
        attraction.setDescription(newDescription);
        Attraction updatedAttraction = attractionRepository.save(attraction);
        log.info("Attraction description updated successfully for id: {}", attractionId);
        return attractionMapper.toDto(updatedAttraction);
    }

    /**
     * Deletes an attraction by its ID.
     *
     * @param attractionId the ID of the attraction to be deleted
     * @throws EntityNotFoundException if the attraction is not found
     */
    @Override
    public void deleteAttraction(Long attractionId) {
        log.info("Deleting attraction with id: {}", attractionId);
        Attraction attraction = attractionRepository.findById(attractionId)
                .orElseThrow(() -> {
                    log.error("Attraction with id {} not found", attractionId);
                    return new EntityNotFoundException("Attraction with id " + attractionId + " not found.");
                });

        log.info("Clearing assistance references for attraction with id: {}", attractionId);
        for (Assistance assistance : attraction.getAssistanceList()) {
            assistance.getAttractions().remove(attraction);
            assistanceRepository.save(assistance);
        }
        attraction.getAssistanceList().clear();
        attractionRepository.save(attraction);

        attractionRepository.delete(attraction);
        log.info("Attraction deleted successfully with id: {}", attractionId);
    }

    /**
     * Adds a new locality to the database.
     *
     * @param localityDto the DTO containing details of the locality to be added
     * @return the created LocalityDto
     */
    @Override
    public LocalityDto addLocality(LocalityDto localityDto) {
        log.info("Adding locality: {}", localityDto);
        Locality locality = localityMapper.toEntity(localityDto);
        Locality savedLocality = localityRepository.save(locality);
        log.info("Locality saved successfully with id: {}", savedLocality.getId());
        return localityMapper.toDto(savedLocality);
    }
}
