package com.wizzdi.security.bearer.jwt;

import com.flexicore.annotations.IOperation;
import com.flexicore.model.Baseclass;
import com.wizzdi.flexicore.security.interfaces.OperationAnnotationConverter;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Component
public class SwaggerOperationExtractor implements OperationAnnotationConverter {
    @Override
    public IOperation getIOperation(Method method) {
        Operation operation = AnnotatedElementUtils.findMergedAnnotation(method, Operation.class);
        if(operation!=null){
            return getIOperationFromApiOperation(operation,method);
        }
        return null;
    }


    public IOperation getIOperationFromApiOperation(io.swagger.v3.oas.annotations.Operation apiOperation, Method method) {
        return new IOperation() {
            @Override
            public String Name() {
                return apiOperation.summary();
            }

            @Override
            public String Description() {
                return apiOperation.description();
            }

            @Override
            public String Category() {
                return "General";
            }

            @Override
            public boolean auditable() {
                return false;
            }

            @Override
            public Class<? extends Baseclass>[] relatedClazzes() {
                if(method.getReturnType()!=null && Baseclass.class.isAssignableFrom(method.getReturnType())){
                    return new Class[]{method.getReturnType()};
                }
                else{
                    return new Class[0];

                }
            }

            @Override
            public Access access() {
                return Access.allow;
            }


            @Override
            public Class<? extends Annotation> annotationType() {
                return IOperation.class;
            }
        };
    }
}
