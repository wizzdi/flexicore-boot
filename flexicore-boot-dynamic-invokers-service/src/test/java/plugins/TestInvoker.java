package plugins;


import com.flexicore.annotations.IOperation;
import com.wizzdi.flexicore.security.configuration.SecurityContext;
import com.wizzdi.flexicore.boot.dynamic.invokers.annotations.Invoker;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collections;

@RestController
@RequestMapping("/test/")
@Extension
public class TestInvoker implements Invoker {



    @IOperation(Name = "listTests",Description = "lists all Tests")
    @PostMapping("/listTests")
    public PaginationResponse<TestEntity> listTests(
			@RequestBody TestFilter filter, @RequestAttribute("securityContext") SecurityContext securityContext) {
        if(filter==null||filter.getPageSize()==null){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,"page size must be provided");
        }
        return new PaginationResponse<>(Collections.singletonList(new TestEntity().setName("test").setDescription("test")),filter,1);
    }

    @IOperation(Name = "createTestEntity",Description = "createTest")
    @PostMapping("/createTestEntity")
    public TestEntity createTest(
			@RequestBody TestEntityCreate testEntityCreate, @RequestAttribute("securityContext") SecurityContext securityContext) {
        return new TestEntity().setName("test").setDescription("test");
    }

    @Override
    public Class<?> getHandlingClass() {
        return TestEntity.class;
    }
}
