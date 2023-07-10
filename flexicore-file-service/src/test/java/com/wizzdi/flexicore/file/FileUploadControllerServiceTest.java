package com.wizzdi.flexicore.file;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flexicore.model.Role;
import com.wizzdi.flexicore.file.app.App;
import com.wizzdi.flexicore.file.model.FileResource;
import com.wizzdi.flexicore.file.service.MD5Service;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.ResponseExtractor;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = App.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // deactivate the default behaviour


public class FileUploadControllerServiceTest {
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
    @Autowired
    private ObjectMapper objectMapper;
    private static final int FILE_LENGTH = 3500000;
    private static final int CHUNK_SIZE = 2000000;

    @BeforeAll
    public void init() {

        restTemplate.getRestTemplate().setInterceptors(
                Collections.singletonList((request, body, execution) -> {
                    request.getHeaders()
                            .add("authenticationKey", "fake");
                    return execution.execute(request, body);
                }));
    }


    @Test
    @Order(0)
    public void testUploadFileFullMd5() {
        Random rd = new Random();
        byte[] data = new byte[FILE_LENGTH];
        rd.nextBytes(data);
        String md5=md5Service.generateMD5(data);
        String name="test-"+System.currentTimeMillis()+".js";
        String id=null;
        //just for sake of the example , frResponse will always be 204 here since we are generating a random file content
        ResponseEntity<FileResource> frResponse=this.restTemplate.getForEntity("/fileResource/"+md5, FileResource.class);
        FileResource body = frResponse.getBody();
        long offset=body!=null?body.getOffset():0L;

        // chunk size to divide
        for(long i=offset;i<data.length;i+=CHUNK_SIZE){
            byte[] chunk=Arrays.copyOfRange(data, (int)i, Math.min(data.length,(int)(i+CHUNK_SIZE)));
            String chunkMD5= md5Service.generateMD5(chunk);
            boolean lastChunk=i+CHUNK_SIZE >=data.length;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.add("md5",md5);
            headers.add("chunkMd5",chunkMD5);
            headers.add("lastChunk",lastChunk+"");
            headers.add("name",name);



            HttpEntity<byte[]> requestEntity = new HttpEntity<>(chunk, headers);

            ResponseEntity<FileResource> response=this.restTemplate.exchange("/upload", HttpMethod.POST, requestEntity, FileResource.class);
            Assertions.assertEquals(200, response.getStatusCodeValue());
            FileResource fileResource=response.getBody();
            Assertions.assertNotNull(fileResource);
            Assertions.assertEquals(fileResource.isDone(),lastChunk);
            id=fileResource.getId();

        }

        this.restTemplate.execute("/downloadUnsecure/" + id, HttpMethod.GET, null, (ResponseExtractor<Object>) clientHttpResponse -> {
            File ret = File.createTempFile("download", "tmp");
            StreamUtils.copy(clientHttpResponse.getBody(), new FileOutputStream(ret));
            return ret;
        });

    }

    @Test
    @Order(1)
    public void testUploadFileChunkErrorRecovery() throws JsonProcessingException {
        Random rd = new Random();
        byte[] data = new byte[FILE_LENGTH];
        rd.nextBytes(data);
        String md5=md5Service.generateMD5(data);
        String name="test-"+System.currentTimeMillis();
        // chunk size to divide
        boolean error=true;
        for(int i=0;i<data.length;i+=CHUNK_SIZE){
            byte[] chunk=Arrays.copyOfRange(data, i, Math.min(data.length,i+CHUNK_SIZE));
            String chunkMD5= md5Service.generateMD5(chunk);
            if(error){
                chunkMD5="fake";
            }
            boolean lastChunk=i+CHUNK_SIZE >=data.length;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.add("md5",md5);
            headers.add("chunkMd5",chunkMD5);
            headers.add("lastChunk",lastChunk+"");
            headers.add("name",name);



            HttpEntity<byte[]> requestEntity = new HttpEntity<>(chunk, headers);

            ResponseEntity<String> response=this.restTemplate.exchange("/upload", HttpMethod.POST, requestEntity, String.class);
            if(error){
                Assertions.assertEquals(HttpStatus.PRECONDITION_FAILED.value(),response.getStatusCodeValue());
                i-=chunk.length;
                error=false;
                continue;
            }
            Assertions.assertEquals(200, response.getStatusCodeValue());
            FileResource fileResource=objectMapper.readValue(response.getBody(),FileResource.class);
            Assertions.assertNotNull(fileResource);
            Assertions.assertEquals(fileResource.isDone(),lastChunk);
        }

    }

    @Test
    @Order(1)
    public void testUploadFileFileErrorRecovery() throws JsonProcessingException {
        Random rd = new Random();
        byte[] data = new byte[FILE_LENGTH];
        rd.nextBytes(data);
        byte[] real=data;
        String md5=md5Service.generateMD5(data);
        String name="test-"+System.currentTimeMillis();
        data= new byte[FILE_LENGTH];
        rd.nextBytes(data);


        // chunk size to divide
        for(int i=0;i<data.length;i+=CHUNK_SIZE){
            byte[] chunk=Arrays.copyOfRange(data, i, Math.min(data.length,i+CHUNK_SIZE));
            String chunkMD5= md5Service.generateMD5(chunk);

            boolean lastChunk=i+CHUNK_SIZE >=data.length;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.add("md5",md5);
            headers.add("chunkMd5",chunkMD5);
            headers.add("lastChunk",lastChunk+"");
            headers.add("name",name);



            HttpEntity<byte[]> requestEntity = new HttpEntity<>(chunk, headers);

            ResponseEntity<String> response=this.restTemplate.exchange("/upload", HttpMethod.POST, requestEntity, String.class);
            if(lastChunk){
                Assertions.assertEquals(HttpStatus.EXPECTATION_FAILED.value(), response.getStatusCodeValue());
                break;

            }
            Assertions.assertEquals(200, response.getStatusCodeValue());
            FileResource fileResource=objectMapper.readValue(response.getBody(),FileResource.class);
            Assertions.assertNotNull(fileResource);
            Assertions.assertEquals(fileResource.isDone(),lastChunk);
        }
        data=real;
        // chunk size to divide
        for(int i=0;i<data.length;i+=CHUNK_SIZE){
            byte[] chunk=Arrays.copyOfRange(data, i, Math.min(data.length,i+CHUNK_SIZE));
            String chunkMD5= md5Service.generateMD5(chunk);

            boolean lastChunk=i+CHUNK_SIZE >=data.length;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.add("md5",md5);
            headers.add("chunkMd5",chunkMD5);
            headers.add("lastChunk",lastChunk+"");
            headers.add("name",name);



            HttpEntity<byte[]> requestEntity = new HttpEntity<>(chunk, headers);

            ResponseEntity<FileResource> response=this.restTemplate.exchange("/upload", HttpMethod.POST, requestEntity, FileResource.class);
            Assertions.assertEquals(200, response.getStatusCodeValue());
            FileResource fileResource=response.getBody();
            Assertions.assertNotNull(fileResource);
            Assertions.assertEquals(fileResource.isDone(),lastChunk);
        }

    }


    @Test
    @Order(2)
    public void testUploadFileNoMd5() {
        Random rd = new Random();
        byte[] data = new byte[FILE_LENGTH];
        rd.nextBytes(data);
        String md5=md5Service.generateMD5(data);
        String name="test-"+System.currentTimeMillis();
        // chunk size to divide
        for(int i=0;i<data.length;i+=CHUNK_SIZE){
            byte[] chunk=Arrays.copyOfRange(data, i, Math.min(data.length,i+CHUNK_SIZE));
            boolean lastChunk=false;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.add("md5",md5);
            headers.add("name",name);



            HttpEntity<byte[]> requestEntity = new HttpEntity<>(chunk, headers);

            ResponseEntity<FileResource> response=this.restTemplate.exchange("/upload", HttpMethod.POST, requestEntity, FileResource.class);
            Assertions.assertEquals(200, response.getStatusCodeValue());
            FileResource fileResource=response.getBody();
            Assertions.assertNotNull(fileResource);
            Assertions.assertEquals(fileResource.isDone(),lastChunk);
        }

    }


}
