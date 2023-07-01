package com.test.rest;

import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.model.Role;
import com.flexicore.request.AuthenticationRequest;
import com.flexicore.request.ExportDynamicInvoker;
import com.flexicore.request.FieldProperties;
import com.flexicore.response.AuthenticationResponse;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.impl.DynamicInvokersService;
import com.test.init.FlexiCoreApplication;
import com.wizzdi.flexicore.boot.dynamic.invokers.request.DynamicInvokerFilter;
import com.wizzdi.flexicore.boot.dynamic.invokers.response.InvokerInfo;
import com.wizzdi.flexicore.file.model.FileResource;
import com.wizzdi.flexicore.security.request.BasicPropertiesFilter;
import com.wizzdi.flexicore.security.request.RoleFilter;
import com.wizzdi.flexicore.security.request.SecurityUserFilter;
import com.wizzdi.flexicore.security.rest.RoleController;
import com.wizzdi.flexicore.security.rest.SecurityUserController;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = FlexiCoreApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")

public class CSVExportTest {


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
