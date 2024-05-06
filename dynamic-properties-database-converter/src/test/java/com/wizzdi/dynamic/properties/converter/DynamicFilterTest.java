package com.wizzdi.dynamic.properties.converter;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = App.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // deactivate the default behaviour

public class DynamicFilterTest {
    private final static PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer("postgres:15")

			.withDatabaseName("flexicore-test")
			.withUsername("flexicore")
			.withPassword("flexicore");
	
	static{
		postgresqlContainer.start();
	}
@DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresqlContainer::getUsername);
        registry.add("spring.datasource.password", postgresqlContainer::getPassword);
    }

	@Autowired
	private AuthorService authorService;


	@Test
	@Order(1)
	public void testCreate() {
		for (int i = 0; i < 10; i++) {
			Map<String, Object> dynamic = Map.of("surName", "van " + i, "location", Map.of("cityName", "city " + i, "capital", i % 2 == 0), "age", i * 10, "familiy", "kuku", "books", List.of("first book " + i, "second book " + i), "birthDate", OffsetDateTime.now().minusYears(i * 10));
			authorService.createAuthor("van " + i, dynamic);
		}

		for (int i = 0; i < 10; i++) {
			Map<String, Object> dynamic = Map.of("surName", "kuku "+i,"location",Map.of("cityName","city "+i,"capital",i%2==0),"age",i*10,"books",List.of("first book "+i,"second book "+i,"birthDate", OffsetDateTime.now().minusYears(i*10)));
			authorService.createAuthor("kuku "+i, dynamic);
		}


	}


}
