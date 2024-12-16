package com.wizzdi.flexicore.security.validation;

import com.flexicore.model.SecurityOperation;
import com.wizzdi.flexicore.security.data.SecurityOperationRepository;
import com.wizzdi.flexicore.security.request.BasicPropertiesFilter;
import com.wizzdi.flexicore.security.request.SecurityOperationFilter;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OperationValidator implements ConstraintValidator<OperationValid, Object> {

    @Autowired
    @Lazy
    private SecurityOperationRepository securityOperationRepository;
    private String targetField;
    private String sourceField;


    @Override
    public void initialize(OperationValid constraintAnnotation) {

        this.targetField = constraintAnnotation.targetField();
        this.sourceField = constraintAnnotation.sourceField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        BeanWrapperImpl objectWrapper = new BeanWrapperImpl(value);
        Object fieldValue = objectWrapper.getPropertyValue(sourceField);
        return switch (fieldValue) {
            case Collection<?> collection ->
                    isValidOperationCollection(collection, objectWrapper, constraintValidatorContext);
            case String securityOperationId ->
                    isValidOperation(securityOperationId, objectWrapper, constraintValidatorContext);
            case null -> true;
            default ->
                    throw new IllegalArgumentException("invalid type for ClazzValid: " + fieldValue.getClass().getCanonicalName());
        };

    }

    private boolean isValidOperationCollection(Collection<?> collection, BeanWrapperImpl objectWrapper, ConstraintValidatorContext constraintValidatorContext) {

        Set<String> operationIds = collection.stream().filter(f -> f instanceof String).map(f -> (String) f).collect(Collectors.toSet());
        List<SecurityOperation> operations = securityOperationRepository.listAllOperations(new SecurityOperationFilter().setBasicPropertiesFilter(new BasicPropertiesFilter().setNames(operationIds)));
        Set<String> validOperationsIds = operations.stream().map(f -> f.name()).collect(Collectors.toSet());
        operationIds.removeAll(validOperationsIds);
        if (!operationIds.isEmpty()) {
            return false;
        }
        objectWrapper.setPropertyValue(targetField, new ArrayList<>(operations));
        return true;
    }

    private boolean isValidOperation(String securityOperationId, BeanWrapperImpl objectWrapper, ConstraintValidatorContext constraintValidatorContext) {
        SecurityOperation operation = securityOperationRepository.listAllOperations(new SecurityOperationFilter().setBasicPropertiesFilter(new BasicPropertiesFilter().setNames(Set.of(securityOperationId)))).stream().findFirst().orElse(null);
        if (operation == null) {
            return false;
        }
        objectWrapper.setPropertyValue(targetField, operation);
        return true;
    }
}
