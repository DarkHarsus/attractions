package com.harsiyanin.intensive.test.attractions.repositories;

import com.harsiyanin.intensive.test.attractions.entities.Assistance;
import com.harsiyanin.intensive.test.attractions.entities.AssistanceType;
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
public class AssistanceRepositoryIntegrationTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Autowired
    private AssistanceRepository assistanceRepository;

    @DynamicPropertySource
    static void registerPostgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Test
    public void testSaveAndFindAssistance() {
        Assistance assistance = new Assistance();
        assistance.setType(AssistanceType.GUIDE);
        assistance.setDescription("Guided tour assistance");
        assistance.setProvider("TravelCo");

        Assistance savedAssistance = assistanceRepository.save(assistance);

        assertThat(savedAssistance.getId()).isNotNull();

        Optional<Assistance> retrievedAssistance = assistanceRepository.findById(savedAssistance.getId());

        assertThat(retrievedAssistance).isPresent();
        assertThat(retrievedAssistance.get().getType()).isEqualTo(AssistanceType.GUIDE);
        assertThat(retrievedAssistance.get().getDescription()).isEqualTo("Guided tour assistance");
        assertThat(retrievedAssistance.get().getProvider()).isEqualTo("TravelCo");
    }

    @Test
    public void testDeleteAssistance() {
        Assistance assistance = new Assistance();
        assistance.setType(AssistanceType.GUIDE);
        assistance.setDescription("Translation assistance");
        assistance.setProvider("LangServ");

        Assistance savedAssistance = assistanceRepository.save(assistance);

        assistanceRepository.delete(savedAssistance);

        Optional<Assistance> deletedAssistance = assistanceRepository.findById(savedAssistance.getId());
        assertThat(deletedAssistance).isNotPresent();
    }
}
