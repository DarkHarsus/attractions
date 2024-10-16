package com.harsiyanin.intensive.test.attractions.repositories;

import com.harsiyanin.intensive.test.attractions.entities.Attraction;
import com.harsiyanin.intensive.test.attractions.entities.AttractionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttractionRepository extends JpaRepository<Attraction, Long> {
    Page<Attraction> findByType(AttractionType type, Pageable pageable);
    List<Attraction> findByLocalityId(Long localityId);
}
