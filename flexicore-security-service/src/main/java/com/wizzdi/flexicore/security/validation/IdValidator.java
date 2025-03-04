package com.wizzdi.flexicore.security.validation;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Basic;
import com.flexicore.model.Clazz;
import com.wizzdi.flexicore.security.configuration.SecurityContext;
import com.wizzdi.flexicore.security.data.SecuredBasicRepository;
import com.wizzdi.flexicore.security.service.ClazzService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class IdValidator implements ConstraintValidator<IdValid, Object> {

    public static final String SECURITY_CONTEXT_ATTRIBUTE_NAME = "securityContext";
    private static final Logger log = LoggerFactory.getLogger(IdValidator.class);
    private String field;
    private Class<?> fieldType;
    private String fieldTypeFromField;
    private String targetField;
    @Autowired
    @Lazy
    private SecuredBasicRepository securedBasicRepository;
    @Autowired
    private ClazzService clazzService;


    @Override
    public void initialize(IdValid constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.fieldType = constraintAnnotation.fieldType();
        this.fieldTypeFromField= constraintAnnotation.fieldTypeFromField();
        this.targetField = constraintAnnotation.targetField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        SecurityContext securityContext = (SecurityContext) RequestContextHolder.getRequestAttributes().getAttribute(SECURITY_CONTEXT_ATTRIBUTE_NAME, RequestAttributes.SCOPE_REQUEST);
        BeanWrapperImpl objectWrapper = new BeanWrapperImpl(value);
        Object fieldValue = objectWrapper
                .getPropertyValue(field);
        Class<?> computedFieldType = getFieldType(objectWrapper);
        if (fieldValue instanceof Collection) {
            Collection<?> collection = (Collection<?>) fieldValue;
            Set<String> ids = collection.stream().filter(f -> f instanceof String).map(f -> (String) f).collect(Collectors.toSet());
            if (!ids.isEmpty()) {
                Map<String, Basic> basicMap;
                    if(Baseclass.class.isAssignableFrom(computedFieldType)){
                        Class<? extends Baseclass> c = (Class<? extends Baseclass>) computedFieldType;
                        basicMap = securedBasicRepository.listByIds(c, ids, securityContext).stream().collect(Collectors.toMap(f -> f.getId(), f -> f, (a, b) -> a));

                    }
                    else{
                        if(Basic.class.isAssignableFrom(computedFieldType)){
                            Class<Basic> basicClass = (Class<Basic>) computedFieldType;
                            basicMap = securedBasicRepository.findByIds(basicClass, ids).stream().collect(Collectors.toMap(f -> f.getId(), f -> f, (a, b) -> a));
                        }
                        else{
                            log.warn("will not validate a non Basic type: {}",computedFieldType);
                            return true;
                        }

                    }



                ids.removeAll(basicMap.keySet());
                if (!ids.isEmpty()) {
                    constraintValidatorContext.buildConstraintViolationWithTemplate("cannot find "+ computedFieldType.getCanonicalName() +" with ids \""+ids+"\"").addPropertyNode(field).addConstraintViolation();
                    return false;
                }
                objectWrapper.setPropertyValue(targetField, new ArrayList<>(basicMap.values()));
            }

        } else {
            if (fieldValue instanceof String) {
                String id = (String) fieldValue;
                Basic basic;

                    if(Baseclass.class.isAssignableFrom(computedFieldType)){
                        Class<? extends Baseclass> c = (Class<? extends Baseclass>) computedFieldType;
                        basic = securedBasicRepository.getByIdOrNull(id, c, securityContext);
                    }
                    else{
                        if(Basic.class.isAssignableFrom(computedFieldType)){
                            Class<Basic> basicClass = (Class<Basic>) computedFieldType;
                            basic = securedBasicRepository.findByIdOrNull(basicClass, id);
                        }
                        else{
                            log.warn("will not validate a non Basic type: {}",computedFieldType);
                            return true;
                        }
                    }

                if (basic == null) {
                    constraintValidatorContext.buildConstraintViolationWithTemplate("cannot find "+ computedFieldType.getCanonicalName() +" with id \""+id+"\"").addPropertyNode(field).addConstraintViolation();
                    return false;
                }
                objectWrapper.setPropertyValue(targetField, basic);

            }
        }
        return true;
    }

    private Class<?> getFieldType(BeanWrapperImpl objectWrapper) {
        try {
            if (fieldTypeFromField.isBlank()) {
                return fieldType;
            }
            Object propertyValue = objectWrapper.getPropertyValue(fieldTypeFromField);
            return switch (propertyValue) {
                case Clazz clazz -> clazz.c();
                case String canonicalClassName -> Class.forName(canonicalClassName);
                case null, default -> fieldType;
            };
        }
        catch (Throwable e){
            log.error("failed getting field type for {}.{}",objectWrapper.getRootClass(),fieldTypeFromField);
        }
        return fieldType;
    }
}
