package com.wizzdi.flexicore.file;

import com.flexicore.model.Role;
import com.wizzdi.flexicore.security.configuration.SecurityContext;
import com.wizzdi.flexicore.file.app.App;
import com.wizzdi.flexicore.file.model.FileResource;
import com.wizzdi.flexicore.file.service.FileResourceService;
import com.wizzdi.flexicore.file.service.MD5Service;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.ResponseExtractor;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Random;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = App.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // deactivate the default behaviour


public class FileDownloadControllerServiceTest {
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

    private Role role;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private MD5Service md5Service;
    private static final int FILE_LENGTH = 3500000;
    private static final int CHUNK_SIZE = 2000000;
    @Autowired
    private FileResourceService fileResourceService;
    @Autowired
    @Lazy
    private SecurityContext adminSecurityContext;
    private FileResource fileResource;
    private String md5;
    @BeforeAll
    public void init() throws IOException {

        restTemplate.getRestTemplate().setInterceptors(
                Collections.singletonList((request, body, execution) -> {
                    request.getHeaders()
                            .add("authenticationKey", "fake");
                    return execution.execute(request, body);
                }));
        Random rd = new Random();
        byte[] data = new byte[FILE_LENGTH];
        rd.nextBytes(data);
        md5=md5Service.generateMD5(data);
        try(ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(data)){
            fileResource = fileResourceService.uploadFileResource("test.jpg", adminSecurityContext, md5, md5, true, byteArrayInputStream);

        }
    }


    @Test
    @Order(0)
    public void testDownload() {
        File download= (File) this.restTemplate.execute("/downloadUnsecure/" + fileResource.getId(), HttpMethod.GET, null, (ResponseExtractor<Object>) clientHttpResponse -> {
            String filename = clientHttpResponse.getHeaders().getFirst("filename");
            File ret = File.createTempFile("download", filename);
            StreamUtils.copy(clientHttpResponse.getBody(), new FileOutputStream(ret));
            return ret;
        });
        String calculated = md5Service.generateMD5(download);
        Assertions.assertEquals(md5,calculated);


    }


}
