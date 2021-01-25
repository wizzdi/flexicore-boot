package com.wizzdi.flexicore.boot.dynamic.invokers.service.plugin;


import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.dynamic.invokers.annotations.Invoker;
import com.wizzdi.flexicore.boot.dynamic.invokers.annotations.InvokerInfo;
import com.wizzdi.flexicore.boot.dynamic.invokers.annotations.InvokerMethodInfo;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collections;

@InvokerInfo(displayName = "test invoker",description = "test invoker")
@Extension
public class TestInvoker implements Invoker {



    @InvokerMethodInfo(displayName = "listTests",description = "lists all Clazzes")
    public PaginationResponse<TestEntity> listTests(TestFilter filter, SecurityContextBase securityContext) {
        if(filter==null||filter.getPageSize()==null){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,"page size must be provided");
        }
        return new PaginationResponse<>(Collections.singletonList(new TestEntity().setName("test").setDescription("test")),filter,1);
    }

    @Override
    public Class<?> getHandlingClass() {
        return TestEntity.class;
    }
}
