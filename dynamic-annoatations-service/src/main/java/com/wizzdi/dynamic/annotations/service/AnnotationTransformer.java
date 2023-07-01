package com.wizzdi.dynamic.annotations.service;

import java.lang.annotation.Annotation;

public interface AnnotationTransformer<T extends Annotation> {

    void applyTransformation(Class<?> c);

    Class<T> getType();
}
