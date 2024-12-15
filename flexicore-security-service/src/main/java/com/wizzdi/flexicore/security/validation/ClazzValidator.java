package com.wizzdi.flexicore.security.validation;

import com.flexicore.model.Clazz;
import com.wizzdi.flexicore.security.data.ClazzRepository;
import com.wizzdi.flexicore.security.request.BasicPropertiesFilter;
import com.wizzdi.flexicore.security.request.ClazzFilter;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ClazzValidator implements ConstraintValidator<ClazzValid, Object> {

    @Autowired
    @Lazy
    private ClazzRepository clazzRepository;


    @Override
    public void initialize(ClazzValid constraintAnnotation) {

    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        return switch (value){
            case Collection<?> collection->isValidClazzCollection(collection,constraintValidatorContext);
            case Clazz clazz->isValidClazz(clazz,constraintValidatorContext);
            case null->true;
            default -> throw new IllegalArgumentException("invalid type for ClazzValid: "+value.getClass().getCanonicalName());
        };

    }

    private boolean isValidClazzCollection(Collection<?> collection, ConstraintValidatorContext constraintValidatorContext) {
        List<Clazz> clazzes=collection.stream().filter(f->f instanceof Clazz).map(f->(Clazz)f).toList();
        Set<String> clazzesNames=clazzes.stream().map(f->f.name()).collect(Collectors.toSet());
        Set<String> validClazzes = clazzRepository.listAllClazzs(new ClazzFilter().setBasicPropertiesFilter(new BasicPropertiesFilter().setNames(clazzesNames)) ).stream().map(f->f.name()).collect(Collectors.toSet());
        clazzesNames.removeAll(validClazzes);
        return clazzesNames.isEmpty();
    }

    private boolean isValidClazz(Clazz clazz, ConstraintValidatorContext constraintValidatorContext) {
        return !clazzRepository.listAllClazzs(new ClazzFilter().setBasicPropertiesFilter(new BasicPropertiesFilter().setNames(Set.of(clazz.name())))).isEmpty();
    }
}
