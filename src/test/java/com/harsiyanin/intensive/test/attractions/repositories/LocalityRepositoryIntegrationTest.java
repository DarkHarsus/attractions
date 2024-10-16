package com.harsiyanin.intensive.test.attractions.repositories;

import com.harsiyanin.intensive.test.attractions.entities.Locality;
import com.harsiyanin.intensive.test.attractions.repositories.LocalityRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@Transactional
public class LocalityRepositoryIntegrationTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Autowired
    private LocalityRepository localityRepository;

    @DynamicPropertySource
    static void registerPostgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Test
    public void testSaveAndFindLocality() {
        Locality locality = new Locality();
        locality.setCity("Магас");
        locality.setRegion("Назрановский район");
        locality.setLatitude(43.1667);
        locality.setLongitude(44.8000);

        Locality savedLocality = localityRepository.save(locality);

        assertThat(savedLocality.getId()).isNotNull();

        Optional<Locality> retrievedLocality = localityRepository.findById(savedLocality.getId());

        assertThat(retrievedLocality).isPresent();
        assertThat(retrievedLocality.get().getCity()).isEqualTo("Магас");
        assertThat(retrievedLocality.get().getRegion()).isEqualTo("Назрановский район");
        assertThat(retrievedLocality.get().getLatitude()).isEqualTo(43.1667);
        assertThat(retrievedLocality.get().getLongitude()).isEqualTo(44.8000);
    }

    @Test
    public void testDeleteLocality() {
        Locality locality = new Locality();
        locality.setCity("Грозный");
        locality.setRegion("Грозненский район");

        Locality savedLocality = localityRepository.save(locality);

        localityRepository.delete(savedLocality);

        Optional<Locality> deletedLocality = localityRepository.findById(savedLocality.getId());
        assertThat(deletedLocality).isNotPresent();
    }
}
