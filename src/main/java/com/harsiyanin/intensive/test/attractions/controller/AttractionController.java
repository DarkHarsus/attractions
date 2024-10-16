package com.harsiyanin.intensive.test.attractions.controller;

import com.harsiyanin.intensive.test.attractions.dtos.AttractionCreateDto;
import com.harsiyanin.intensive.test.attractions.dtos.AttractionDto;
import com.harsiyanin.intensive.test.attractions.dtos.LocalityDto;
import com.harsiyanin.intensive.test.attractions.entities.AttractionType;
import com.harsiyanin.intensive.test.attractions.service.AttractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/attractions")
public class AttractionController {

    private final AttractionService attractionService;

    @Autowired
    public AttractionController(AttractionService attractionService) {
        this.attractionService = attractionService;
    }

    @PostMapping("/attraction")
    public ResponseEntity<AttractionDto> addAttraction(@RequestBody AttractionCreateDto attractionCreateDto) {
        AttractionDto createdAttraction = attractionService.addAttraction(attractionCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAttraction);
    }

    @GetMapping("/page")
    public ResponseEntity<List<AttractionDto>> getAllAttractions(
            @RequestParam(value = "filterByType", required = false)AttractionType type, Pageable pageable) {
        List<AttractionDto> attractions = attractionService.getAllAttractions(type, pageable);
        return new ResponseEntity<>(attractions, HttpStatus.OK);
    }

    @GetMapping("/locality/{localityId}")
    public ResponseEntity<List<AttractionDto>> getAttractionsByLocality(@PathVariable Long localityId) {
        List<AttractionDto> attractions = attractionService.getAttractionsByLocality(localityId);
        return new ResponseEntity<>(attractions, HttpStatus.OK);
    }

    @PutMapping("/{attractionId}")
    public ResponseEntity<AttractionDto> updateAttractionDescription(
            @PathVariable Long attractionId,
            @RequestBody String newDescription) {
        AttractionDto updatedAttraction = attractionService.updateAttractionDescription(attractionId, newDescription);
        return new ResponseEntity<>(updatedAttraction, HttpStatus.OK);
    }

    @DeleteMapping("/{attractionId}")
    public ResponseEntity<Void> deleteAttraction(@PathVariable Long attractionId) {
        attractionService.deleteAttraction(attractionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/locality")
    public ResponseEntity<LocalityDto> addLocality(@RequestBody LocalityDto localityDto) {
        LocalityDto savedLocality = attractionService.addLocality(localityDto);
        return new ResponseEntity<>(savedLocality, HttpStatus.CREATED);
    }
}
