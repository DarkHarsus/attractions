package com.harsiyanin.intensive.test.attractions.controller;


import com.harsiyanin.intensive.test.attractions.dtos.AssistanceDto;
import com.harsiyanin.intensive.test.attractions.service.AssistanceService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/assistance")
public class AssistanceController {

    private final AssistanceService assistanceService;

    @Autowired
    public AssistanceController(AssistanceService assistanceService) {
        this.assistanceService = assistanceService;
    }

    @PostMapping
    public ResponseEntity<AssistanceDto> addAssistance(@RequestBody AssistanceDto assistanceDto){
        AssistanceDto createdAssistance = assistanceService.addAssistance(assistanceDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAssistance);
    }

    @GetMapping("/{assistanceId}")
    public ResponseEntity<AssistanceDto> getAssistance(@PathVariable Long assistanceId){
        AssistanceDto assistanceDto = assistanceService.getAssistance(assistanceId);
        return new ResponseEntity<>(assistanceDto, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AssistanceDto>> getAllAssistance(Pageable pageable) {
        List<AssistanceDto> assistanceDtos = assistanceService.getAllAssistance(pageable);
        return new ResponseEntity<>(assistanceDtos, HttpStatus.OK);
    }

    @GetMapping("/all/{attractionId}")
    public ResponseEntity<List<AssistanceDto>> getAssistanceByAttraction(@PathVariable Long attractionId) {  //постман
        List<AssistanceDto> assistanceDtos = assistanceService.getAssistanceByAttraction(attractionId);
        return new ResponseEntity<>(assistanceDtos, HttpStatus.OK);
    }

    @PostMapping("/{assistanceId}/add-to-attraction/{attractionId}")
    public ResponseEntity<Void> addAssistanceToAttraction(@PathVariable Long assistanceId, @PathVariable Long attractionId) {
        try {
            assistanceService.addAssistanceToAttraction(assistanceId, attractionId);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{assistanceId}")
    public ResponseEntity<Void> deleteAssistance(@PathVariable Long assistanceId) {
        assistanceService.deleteAssistance(assistanceId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
