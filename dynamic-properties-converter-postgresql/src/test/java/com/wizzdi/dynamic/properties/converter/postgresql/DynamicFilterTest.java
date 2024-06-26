package com.wizzdi.dynamic.properties.converter.postgresql;

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
import java.util.ArrayList;
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


	@BeforeAll
	public void init() {
		for (int i = 0; i < 10; i++) {
			Map<String, Object> dynamic = Map.of("surName", "van " + i, "location", Map.of("cityName", "city " + i, "capital", i % 2 == 0), "age", i * 10, "familiy", "kuku", "books", List.of("first book " + i, "second book " + i), "birthDate", OffsetDateTime.now().minusYears(i * 10));
			authorService.createAuthor("van " + i,OffsetDateTime.now().minusYears(i), dynamic);
		}

		for (int i = 0; i < 10; i++) {
			Map<String, Object> dynamic = Map.of("surName", "kuku "+i,"location",Map.of("cityName","city "+i,"capital",i%2==0),"age",i*10,"books",List.of("first book "+i,"second book "+i,"birthDate", OffsetDateTime.now().minusYears(i*10)));
			authorService.createAuthor("kuku "+i,OffsetDateTime.now().minusYears(i), dynamic);
		}


	}

	@AfterAll
	public void cleanup() {
		authorService.deleteAll();

	}

	@Test
	@Order(2)
	public void testEQ() {
		List<Author> surName = authorService.getAuthorsDynamicFilter(Map.of("surName", new DynamicPredicateItem().setValue("van 1").setFilterType(FilterType.EQUALS)));
		Assertions.assertEquals(1,surName.size());

	}

	@Test
	@Order(2)
	public void testEQBool() {
		List<Author> surName = authorService.getAuthorsDynamicFilter(Map.of("location", new DynamicNodeItem(Map.of("capital",new DynamicPredicateItem().setValue(true).setFilterType(FilterType.EQUALS)))));
		Assertions.assertEquals(10,surName.size());

	}

	@Test
	@Order(3)
	public void testNEQ() {
		List<Author> surName = authorService.getAuthorsDynamicFilter(Map.of("surName", new DynamicPredicateItem().setValue("van 1").setFilterType(FilterType.NOT_EQUALS)));
		Assertions.assertEquals(19,surName.size());

	}
	@Test
	@Order(3)
	public void testNEQNested() {
		List<Author> surName = authorService.getAuthorsDynamicFilter(Map.of("location", new DynamicNodeItem(Map.of("cityName",new DynamicPredicateItem().setValue("city 0").setFilterType(FilterType.NOT_EQUALS)))));
		Assertions.assertEquals(18,surName.size());

	}

	@Test
	@Order(4)
	public void testContains() {
		List<Author> surName = authorService.getAuthorsDynamicFilter(Map.of("surName", new DynamicPredicateItem().setValue("ku").setFilterType(FilterType.CONTAINS)));
		Assertions.assertEquals(10,surName.size());


	}

	@Test
	@Order(5)
	public void testIn() {
		List<Author> surName = authorService.getAuthorsDynamicFilter(Map.of("surName", new DynamicPredicateItem().setValue(new ArrayList<>(List.of("van 1","van 2"))).setFilterType(FilterType.IN)));
		Assertions.assertEquals(2,surName.size());


	}

	@Test
	@Order(6)
	public void testLT() {
		List<Author> surName = authorService.getAuthorsDynamicFilter(Map.of("age", new DynamicPredicateItem().setValue(30).setFilterType(FilterType.LESS_THAN)));
		Assertions.assertEquals(6,surName.size());


	}

	@Test
	@Order(7)
	public void testLTE() {
		List<Author> surName = authorService.getAuthorsDynamicFilter(Map.of("age", new DynamicPredicateItem().setValue(30).setFilterType(FilterType.LESS_THAN_OR_EQUAL)));
		Assertions.assertEquals(8,surName.size());


	}

	@Test
	@Order(8)
	public void testGT() {
		List<Author> surName = authorService.getAuthorsDynamicFilter(Map.of("age", new DynamicPredicateItem().setValue(30).setFilterType(FilterType.GREATER_THAN)));
		Assertions.assertEquals(12,surName.size());


	}
	@Test
	@Order(9)
	public void testGTE() {
		List<Author> surName = authorService.getAuthorsDynamicFilter(Map.of("age", new DynamicPredicateItem().setValue(30).setFilterType(FilterType.GREATER_THAN_OR_EQUAL)));
		Assertions.assertEquals(14,surName.size());


	}

	@Test
	@Order(10)
	public void testNull() {
		List<Author> surName = authorService.getAuthorsDynamicFilter(Map.of("familiy", new DynamicPredicateItem().setFilterType(FilterType.IS_NULL)));
		Assertions.assertEquals(10,surName.size());


	}

	@Test
	@Order(10)
	public void testNotNull() {
		List<Author> surName = authorService.getAuthorsDynamicFilter(Map.of("familiy", new DynamicPredicateItem().setFilterType(FilterType.IS_NOT_NULL)));
		Assertions.assertEquals(10,surName.size());


	}

}
