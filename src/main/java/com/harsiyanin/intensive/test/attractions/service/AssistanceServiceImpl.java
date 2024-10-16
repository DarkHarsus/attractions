package com.harsiyanin.intensive.test.attractions.service;

import com.harsiyanin.intensive.test.attractions.dtos.AssistanceDto;
import com.harsiyanin.intensive.test.attractions.entities.Assistance;
import com.harsiyanin.intensive.test.attractions.entities.Attraction;
import com.harsiyanin.intensive.test.attractions.mappers.AssistanceMapper;
import com.harsiyanin.intensive.test.attractions.mappers.AttractionMapper;
import com.harsiyanin.intensive.test.attractions.repositories.AssistanceRepository;
import com.harsiyanin.intensive.test.attractions.repositories.AttractionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class AssistanceServiceImpl implements AssistanceService {

    private final AssistanceRepository assistanceRepository;
    private final AttractionRepository attractionRepository;
    private final AssistanceMapper assistanceMapper = AssistanceMapper.INSTANCE;
    private final AttractionMapper attractionMapper = AttractionMapper.INSTANCE;

    @Autowired
    public AssistanceServiceImpl(AssistanceRepository assistanceRepository, AttractionRepository attractionRepository) {
        this.assistanceRepository = assistanceRepository;
        this.attractionRepository = attractionRepository;
    }

    /**
     * Adds a new assistance entity to the database.
     *
     * @param assistanceDto the DTO containing details of the assistance to be added
     * @return the created AssistanceDto
     */
    @Override
    public AssistanceDto addAssistance(AssistanceDto assistanceDto) {
        log.info("Adding new assistance: {}", assistanceDto);
        Assistance assistance = assistanceMapper.toEntity(assistanceDto);
        Assistance savedAssistance = assistanceRepository.save(assistance);
        log.info("Assistance saved successfully with id: {}", savedAssistance.getId());
        return assistanceMapper.toDto(savedAssistance);
    }

    /**
     * Retrieves assistance details by its ID.
     *
     * @param assistanceId the ID of the assistance
     * @return the AssistanceDto for the specified ID
     * @throws IllegalArgumentException if the assistance is not found
     */
    @Override
    public AssistanceDto getAssistance(Long assistanceId) {
        log.info("Fetching assistance with id: {}", assistanceId);
        Assistance assistance = assistanceRepository.findById(assistanceId)
                .orElseThrow(() -> {
                    log.error("Assistance with id {} not found", assistanceId);
                    return new IllegalArgumentException("Assistance with id " + assistanceId + " not found");
                });
        return assistanceMapper.toDto(assistance);
    }

    /**
     * Retrieves a paginated list of all assistance entities.
     *
     * @param pageable pagination information
     * @return a list of AssistanceDto objects
     */
    @Override
    public List<AssistanceDto> getAllAssistance(Pageable pageable) {
        log.info("Fetching all assistance with pagination: {}", pageable);
        Page<Assistance> assistances = assistanceRepository.findAll(pageable);
        return assistances.stream()
                .map(assistanceMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of assistance entities associated with a specific attraction.
     *
     * @param attractionId the ID of the attraction
     * @return a list of AssistanceDto objects for the specified attraction
     * @throws EntityNotFoundException if the attraction is not found
     */
    @Override
    public List<AssistanceDto> getAssistanceByAttraction(Long attractionId) {
        log.info("Fetching assistance for attraction with id: {}", attractionId);
        List<Assistance> assistanceList = attractionRepository.findById(attractionId)
                .map(Attraction::getAssistanceList)
                .orElseThrow(() -> {
                    log.error("Attraction with id {} not found", attractionId);
                    return new EntityNotFoundException("Entity with id " + attractionId + " not found.");
                });
        return assistanceList.stream()
                .map(assistanceMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Adds assistance to a specific attraction.
     *
     * @param assistanceId the ID of the assistance to be added
     * @param attractionId the ID of the attraction
     * @throws IllegalArgumentException if either the assistance or the attraction is not found
     */
    @Override
    public void addAssistanceToAttraction(Long assistanceId, Long attractionId) {
        log.info("Adding assistance with id {} to attraction with id {}", assistanceId, attractionId);
        Optional<Assistance> assistanceOptional = assistanceRepository.findById(assistanceId);
        Optional<Attraction> attractionOptional = attractionRepository.findById(attractionId);

        if (assistanceOptional.isPresent() && attractionOptional.isPresent()) {
            Assistance assistance = assistanceOptional.get();
            Attraction attraction = attractionOptional.get();

            attraction.getAssistanceList().add(assistance);
            attractionRepository.save(attraction);
            log.info("Assistance with id {} added to attraction with id {}", assistanceId, attractionId);
        } else {
            log.error("Assistance or Attraction not found with provided IDs: assistanceId={}, attractionId={}", assistanceId, attractionId);
            throw new IllegalArgumentException("Assistance or Attraction not found with the provided IDs.");
        }
    }

    /**
     * Deletes an assistance entity by its ID.
     *
     * @param assistanceId the ID of the assistance to be deleted
     */
    @Override
    public void deleteAssistance(Long assistanceId) {
        log.info("Deleting assistance with id: {}", assistanceId);
        assistanceRepository.findById(assistanceId)
                .ifPresentOrElse(
                        assistance -> {
                            assistanceRepository.delete(assistance);
                            log.info("Assistance with id {} deleted successfully", assistanceId);
                        },
                        () -> log.error("Assistance with id {} not found for deletion", assistanceId)
                );
    }
}
