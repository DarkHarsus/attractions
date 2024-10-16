package com.harsiyanin.intensive.test.attractions.repositories;

import com.harsiyanin.intensive.test.attractions.entities.Attraction;
import com.harsiyanin.intensive.test.attractions.entities.AttractionType;
import com.harsiyanin.intensive.test.attractions.entities.Locality;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:tc:postgresql:12.2:///attractionsdb",
        "spring.datasource.username=admin",
        "spring.datasource.password=admin"
})
@Transactional
public class AttractionRepositoryIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:12.2")
            .withDatabaseName("attractionsdb")
            .withUsername("admin")
            .withPassword("admin");

    @Autowired
    private AttractionRepository attractionRepository;

    @Autowired
    private LocalityRepository localityRepository;

    @Test
    public void testSaveAndFindAttraction() {
        Locality locality = new Locality();
        locality.setCity("Магас");
        locality.setRegion("Назрановский район");
        Locality savedLocality = localityRepository.save(locality);

        Attraction attraction = new Attraction();
        attraction.setName("Museum");
        attraction.setCreationDate(LocalDate.now());
        attraction.setDescription("A historical museum");
        attraction.setType(AttractionType.MUSEUM);
        attraction.setLocality(savedLocality);

        Attraction savedAttraction = attractionRepository.save(attraction);
        Attraction foundAttraction = attractionRepository.findById(savedAttraction.getId()).orElse(null);

        assertThat(foundAttraction).isNotNull();
        assertThat(foundAttraction.getName()).isEqualTo(savedAttraction.getName());
        assertThat(foundAttraction.getDescription()).isEqualTo(savedAttraction.getDescription());
        assertThat(foundAttraction.getLocality().getCity()).isEqualTo(savedLocality.getCity());
        assertThat(foundAttraction.getLocality().getRegion()).isEqualTo(savedLocality.getRegion());
    }

    @Test
    public void testFindAttractionsByType() {
        Locality locality1 = new Locality();
        locality1.setCity("Магас");
        locality1.setRegion("Назрановский район");
        Locality savedLocality1 = localityRepository.save(locality1);
        Attraction attraction1 = new Attraction();
        attraction1.setName("Museum A");
        attraction1.setCreationDate(LocalDate.now());
        attraction1.setDescription("A historical museum A");
        attraction1.setType(AttractionType.MUSEUM);
        attraction1.setLocality(savedLocality1);

        Locality locality2 = new Locality();
        locality2.setCity("Магас");
        locality2.setRegion("Назрановский район");
        Locality savedLocality2 = localityRepository.save(locality2);
        Attraction attraction2 = new Attraction();
        attraction2.setName("Park B");
        attraction2.setCreationDate(LocalDate.now());
        attraction2.setDescription("A beautiful park B");
        attraction2.setType(AttractionType.PARK);
        attraction2.setLocality(savedLocality2);

        attractionRepository.save(attraction1);
        attractionRepository.save(attraction2);

        List<Attraction> museumAttractions = attractionRepository.findByType(AttractionType.MUSEUM, Pageable.unpaged()).getContent();

        assertThat(museumAttractions).hasSize(1);
        assertThat(museumAttractions.get(0).getName()).isEqualTo(attraction1.getName());
    }
}

