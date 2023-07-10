package com.test.rest;

import com.flexicore.request.ExportDynamicInvoker;
import com.flexicore.request.FieldProperties;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.impl.DynamicInvokersService;
import com.test.init.FlexiCoreApplication;
import com.wizzdi.flexicore.file.model.FileResource;
import com.wizzdi.flexicore.security.request.BasicPropertiesFilter;
import com.wizzdi.flexicore.security.request.RoleFilter;
import com.wizzdi.flexicore.security.request.SecurityUserFilter;
import com.wizzdi.flexicore.security.rest.RoleController;
import com.wizzdi.flexicore.security.rest.SecurityUserController;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.util.Map;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = FlexiCoreApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // deactivate the default behaviour


public class CSVExportTest {
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
    private DynamicInvokersService dynamicInvokersService;

    @Autowired
    @Lazy
    private SecurityContext adminSecurityContext;

    @Test
    @Order(1)
    public void testExportCsv() {
        ExportDynamicInvoker exportDynamicExecution = new ExportDynamicInvoker();
        exportDynamicExecution.setFieldProperties(Map.of("id", new FieldProperties("ID", 0), "name", new FieldProperties("Name", 1)))
                .setInvokerNames(Set.of(SecurityUserController.class.getCanonicalName()))
                .setInvokerMethodName("getAll")
                .setExecutionParametersHolder(new SecurityUserFilter().setBasicPropertiesFilter(new BasicPropertiesFilter().setNameLike("%a%")));
        dynamicInvokersService.validateExportDynamicInvoker(exportDynamicExecution, null);

        FileResource fileResource = dynamicInvokersService.exportDynamicInvokerToCSV(exportDynamicExecution, adminSecurityContext);
        Assertions.assertNotNull(fileResource);
        Assertions.assertTrue(new File(fileResource.getFullPath()).exists());
        System.out.println("csv export at " + fileResource.getFullPath());


    }

    @Test
    @Order(2)
    public void testExportCsvWithNestedFields() {
        ExportDynamicInvoker exportDynamicExecution = new ExportDynamicInvoker();
        exportDynamicExecution.setFieldProperties(Map.of(
                        "id", new FieldProperties("ID", 0),
                        "name", new FieldProperties("Name", 1),
                        "tenant.id", new FieldProperties("Tenant ID", 2),
                        "tenant.name", new FieldProperties("Tenant Name", 3)
                ))
                .setInvokerNames(Set.of(RoleController.class.getCanonicalName()))
                .setInvokerMethodName("getAll")
                .setExecutionParametersHolder(new RoleFilter().setBasicPropertiesFilter(new BasicPropertiesFilter().setNameLike("%a%")));
        dynamicInvokersService.validateExportDynamicInvoker(exportDynamicExecution, null);

        FileResource fileResource = dynamicInvokersService.exportDynamicInvokerToCSV(exportDynamicExecution, adminSecurityContext);
        Assertions.assertNotNull(fileResource);
        Assertions.assertTrue(new File(fileResource.getFullPath()).exists());
        System.out.println("csv export at " + fileResource.getFullPath());
    }


    @Test
    @Order(3)
    public void testExportCsvWithMissingFields() {
        ExportDynamicInvoker exportDynamicExecution = new ExportDynamicInvoker();
        exportDynamicExecution.setFieldProperties(Map.of(
                        "id", new FieldProperties("ID", 0),
                        "name", new FieldProperties("Name", 1),
                        "tenant.id", new FieldProperties("Tenant ID", 2),
                        "tenant.name", new FieldProperties("Tenant Name", 3),
                        "foo.name", new FieldProperties("Direct Foo", 4),
                        "tenant.bar", new FieldProperties("Indirect Bar", 5),
                        "tenant.creator.baz", new FieldProperties("Nested Baz", 6)
                ))
                .setInvokerNames(Set.of(RoleController.class.getCanonicalName()))
                .setInvokerMethodName("getAll")
                .setExecutionParametersHolder(new RoleFilter().setBasicPropertiesFilter(new BasicPropertiesFilter().setNameLike("%a%")));
        dynamicInvokersService.validateExportDynamicInvoker(exportDynamicExecution, null);

        FileResource fileResource = dynamicInvokersService.exportDynamicInvokerToCSV(exportDynamicExecution, adminSecurityContext);
        Assertions.assertNotNull(fileResource);
        Assertions.assertTrue(new File(fileResource.getFullPath()).exists());
        System.out.println("csv export at " + fileResource.getFullPath());
    }

}
