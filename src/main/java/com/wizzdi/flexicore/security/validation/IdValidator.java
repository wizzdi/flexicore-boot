package com.wizzdi.flexicore.security.validation;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Basic;
import com.flexicore.model.SecuredBasic;
import com.flexicore.model.SecuredBasic_;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.security.data.SecuredBasicRepository;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class IdValidator implements ConstraintValidator<IdValid, Object> {

    public static final String SECURITY_CONTEXT_ATTRIBUTE_NAME = "securityContext";
    private String field;
    private Class<?> fieldType;
    private String targetField;
    @Autowired
    @Lazy
    private SecuredBasicRepository securedBasicRepository;


    @Override
    public void initialize(IdValid constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.fieldType = constraintAnnotation.fieldType();
        this.targetField = constraintAnnotation.targetField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        SecurityContextBase securityContext = (SecurityContextBase) RequestContextHolder.getRequestAttributes().getAttribute(SECURITY_CONTEXT_ATTRIBUTE_NAME, RequestAttributes.SCOPE_REQUEST);
        BeanWrapperImpl objectWrapper = new BeanWrapperImpl(value);
        Object fieldValue = objectWrapper
                .getPropertyValue(field);
        if (fieldValue instanceof Collection) {
            Collection<?> collection = (Collection<?>) fieldValue;
            Set<String> ids = collection.stream().filter(f -> f instanceof String).map(f -> (String) f).collect(Collectors.toSet());
            if (!ids.isEmpty()) {
                Map<String, Basic> basicMap;
                if (SecuredBasic.class.isAssignableFrom(fieldType)) {
                    Class<? extends SecuredBasic> c = (Class<? extends SecuredBasic>) fieldType;
                    basicMap = securedBasicRepository.listByIds(c, ids, SecuredBasic_.security, securityContext).stream().collect(Collectors.toMap(f -> f.getId(), f -> f, (a, b) -> a));

                } else {
                    if(Baseclass.class.isAssignableFrom(fieldType)){
                        Class<? extends Baseclass> c = (Class<? extends Baseclass>) fieldType;
                        basicMap = securedBasicRepository.listByIds(c, ids, securityContext).stream().collect(Collectors.toMap(f -> f.getId(), f -> f, (a, b) -> a));

                    }
                    else{
                        Class<Basic> basicClass = (Class<Basic>) fieldType;
                        basicMap = securedBasicRepository.findByIds(basicClass, ids).stream().collect(Collectors.toMap(f -> f.getId(), f -> f, (a, b) -> a));

                    }


                }

                ids.removeAll(basicMap.keySet());
                if (!ids.isEmpty()) {
                    constraintValidatorContext.buildConstraintViolationWithTemplate("cannot find "+fieldType.getCanonicalName() +" with ids \""+ids+"\"").addPropertyNode(field).addConstraintViolation();
                    return false;
                }
                objectWrapper.setPropertyValue(targetField, new ArrayList<>(basicMap.values()));
            }

        } else {
            if (fieldValue instanceof String) {
                String id = (String) fieldValue;
                Basic basic;

                if (SecuredBasic.class.isAssignableFrom(fieldType)) {
                    Class<? extends SecuredBasic> c = (Class<? extends SecuredBasic>) fieldType;
                    basic = securedBasicRepository.getByIdOrNull(id, c, SecuredBasic_.security, securityContext);

                } else {
                    if(Baseclass.class.isAssignableFrom(fieldType)){
                        Class<? extends Baseclass> c = (Class<? extends Baseclass>) fieldType;
                        basic = securedBasicRepository.getByIdOrNull(id, c, securityContext);
                    }
                    else{
                        Class<Basic> basicClass = (Class<Basic>) fieldType;
                        basic = securedBasicRepository.findByIdOrNull(basicClass, id);
                    }

                }
                if (basic == null) {
                    constraintValidatorContext.buildConstraintViolationWithTemplate("cannot find "+fieldType.getCanonicalName() +" with id \""+id+"\"").addPropertyNode(field).addConstraintViolation();
                    return false;
                }
                objectWrapper.setPropertyValue(targetField, basic);

            }
        }
        return true;
    }
}
