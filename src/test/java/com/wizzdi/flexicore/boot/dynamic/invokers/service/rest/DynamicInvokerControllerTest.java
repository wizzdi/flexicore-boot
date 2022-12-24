package com.wizzdi.flexicore.boot.dynamic.invokers.service.rest;

import com.wizzdi.flexicore.boot.dynamic.invokers.request.DynamicInvokerFilter;
import com.wizzdi.flexicore.boot.dynamic.invokers.request.DynamicInvokerMethodFilter;
import com.wizzdi.flexicore.boot.dynamic.invokers.response.InvokerHolder;
import com.wizzdi.flexicore.boot.dynamic.invokers.response.InvokerInfo;
import com.wizzdi.flexicore.boot.dynamic.invokers.response.InvokerMethodHolder;
import com.wizzdi.flexicore.boot.dynamic.invokers.service.app.App;
import com.wizzdi.flexicore.security.request.BasicPropertiesFilter;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = App.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")

public class DynamicInvokerControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

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
    @Order(2)
    public void testListAllDynamicInvokers() {
        DynamicInvokerFilter request=new DynamicInvokerFilter()
                .setBasicPropertiesFilter(new BasicPropertiesFilter().setNameLike("Test"))
                .setMethodNameLike("listTests");
        ParameterizedTypeReference<PaginationResponse<InvokerInfo>> t= new ParameterizedTypeReference<>() {};

        ResponseEntity<PaginationResponse<InvokerInfo>> dynamicInvokerResponse = this.restTemplate.exchange("/dynamicInvoker/getAll", HttpMethod.POST, new HttpEntity<>(request), t);
        Assertions.assertEquals(200, dynamicInvokerResponse.getStatusCodeValue());
        PaginationResponse<InvokerInfo> body = dynamicInvokerResponse.getBody();
        Assertions.assertNotNull(body);
        List<InvokerInfo> dynamicInvokers = body.getList();
        Assertions.assertNotEquals(0,dynamicInvokers.size());
        System.out.println("received "+dynamicInvokers.size() +" invokers: "+dynamicInvokers);


    }

    @Test
    @Order(2)
    public void testListAllDynamicInvokerHolders() {
        DynamicInvokerFilter request=new DynamicInvokerFilter()
                .setBasicPropertiesFilter(new BasicPropertiesFilter().setNameLike("Test"))
                .setMethodNameLike("listTests");
        ParameterizedTypeReference<PaginationResponse<InvokerHolder>> t= new ParameterizedTypeReference<>() {};

        ResponseEntity<PaginationResponse<InvokerHolder>> dynamicInvokerResponse = this.restTemplate.exchange("/dynamicInvoker/getAllInvokerHolders", HttpMethod.POST, new HttpEntity<>(request), t);
        Assertions.assertEquals(200, dynamicInvokerResponse.getStatusCodeValue());
        PaginationResponse<InvokerHolder> body = dynamicInvokerResponse.getBody();
        Assertions.assertNotNull(body);
        List<InvokerHolder> dynamicInvokers = body.getList();
        Assertions.assertNotEquals(0,dynamicInvokers.size());
        System.out.println("received "+dynamicInvokers.size() +" invokers: "+dynamicInvokers);


    }

    @Test
    @Order(3)
    public void testListAllDynamicInvokerMethodHolders() {
        DynamicInvokerMethodFilter request=new DynamicInvokerMethodFilter()
                .setEmptyCategories(true)
                .setCategories(Collections.singleton("TYPE_ACTION"))
                .setBasicPropertiesFilter(new BasicPropertiesFilter().setNameLike("listTests"));
        ParameterizedTypeReference<PaginationResponse<InvokerMethodHolder>> t= new ParameterizedTypeReference<>() {};

        ResponseEntity<PaginationResponse<InvokerMethodHolder>> dynamicInvokerResponse = this.restTemplate.exchange("/dynamicInvokerMethod/getAllInvokerMethodHolders", HttpMethod.POST, new HttpEntity<>(request), t);
        Assertions.assertEquals(200, dynamicInvokerResponse.getStatusCodeValue());
        PaginationResponse<InvokerMethodHolder> body = dynamicInvokerResponse.getBody();
        Assertions.assertNotNull(body);
        List<InvokerMethodHolder> dynamicInvokers = body.getList();
        Assertions.assertNotEquals(0,dynamicInvokers.size());
        System.out.println("received "+dynamicInvokers.size() +" invoker methods: "+dynamicInvokers);


    }


}
