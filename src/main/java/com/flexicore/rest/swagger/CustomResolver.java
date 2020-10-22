package com.flexicore.rest.swagger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flexicore.data.jsoncontainers.Views;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.media.Schema;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Iterator;

public class CustomResolver extends ModelResolver {
    private Class<? extends Views.Unrefined> view;

    public CustomResolver(ObjectMapper mapper, Class<? extends Views.Unrefined> view) {
        super(mapper);
        this.view = view;
    }

    @Override
    public Schema resolve(AnnotatedType annotatedType, ModelConverterContext context, Iterator<ModelConverter> next) {

        Annotation[] ctxAnnotations = annotatedType.getCtxAnnotations();
        if (ctxAnnotations != null) {
            boolean ignore = Arrays.stream(ctxAnnotations).anyMatch(f -> jsonViewIgnore(f) || f instanceof JsonIgnore);
            if (ignore) {
                return null;
            }


        }
        annotatedType.skipJsonIdentity(true);
        return super.resolve(annotatedType, context, next);

    }







    private boolean jsonViewIgnore(Annotation f) {
        if (f instanceof JsonView) {
            for (Class<?> aClass : ((JsonView) f).value()) {
                if (!view.equals(aClass) && view.isAssignableFrom(aClass)) {
                    return true;
                }
            }
        }
        return false;
    }






}
