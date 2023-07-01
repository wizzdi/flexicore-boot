package com.wizzdi.flexicore.security.test.rest;

import com.flexicore.model.Role;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.security.request.RoleFilter;
import com.wizzdi.flexicore.security.request.SecurityPolicyCreate;
import com.wizzdi.flexicore.security.service.RoleService;
import com.wizzdi.flexicore.security.test.app.App;
import com.wizzdi.flexicore.security.validation.Create;
import com.wizzdi.flexicore.security.validation.Update;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = App.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")

public class ValidationTest {

    @Autowired
    private Validator validator;
    @Autowired
    private RoleService roleService;
    @Autowired
    private SecurityContextBase adminSecurityContext;


    @Test
    public void testValidationProgrammatically(){
        Role role = roleService.listAllRoles(new RoleFilter().setPageSize(1).setCurrentPage(0), null).stream().findFirst().orElseThrow(() -> new RuntimeException("no roles"));
        SecurityPolicyCreate securityPolicyCreate=new SecurityPolicyCreate()
                .setPolicyRoleId(role.getId());
        try {
            RequestContextHolder.setRequestAttributes(new CustomRequestScopeAttr(Map.of("securityContext", adminSecurityContext)));
            DataBinder binder = new DataBinder(securityPolicyCreate);
            binder.addValidators(validator);
            binder.validate(Create.class, Update.class);
            BindingResult br = binder.getBindingResult();
        }finally {
            RequestContextHolder.resetRequestAttributes();
        }
    }

    public class CustomRequestScopeAttr implements RequestAttributes {
        private Map<String, Object> requestAttributeMap;

        public CustomRequestScopeAttr(Map<String, Object> requestAttributeMap) {
            this.requestAttributeMap = new HashMap<>(requestAttributeMap);
        }

        @Override
        public Object getAttribute(String name, int scope) {
            if(scope== RequestAttributes.SCOPE_REQUEST) {
                return this.requestAttributeMap.get(name);
            }
            return null;
        }
        @Override
        public void setAttribute(String name, Object value, int scope) {
            if(scope== RequestAttributes.SCOPE_REQUEST){
                this.requestAttributeMap.put(name, value);
            }
        }
        @Override
        public void removeAttribute(String name, int scope) {
            if(scope== RequestAttributes.SCOPE_REQUEST) {
                this.requestAttributeMap.remove(name);
            }
        }
        @Override
        public String[] getAttributeNames(int scope) {
            if(scope== RequestAttributes.SCOPE_REQUEST) {
                return this.requestAttributeMap.keySet().toArray(new String[0]);
            }
            return  new String[0];
        }
        @Override public void registerDestructionCallback(String name, Runnable callback, int scope) {
            // Not Supported
        }
        @Override
        public Object resolveReference(String key) {
            // Not supported
            return null;
        }
        @Override
        public String getSessionId() {
            return null;
        }
        @Override
        public Object getSessionMutex() {
            return null;
        }
    }
}
