package com.wizzdi.dynamic.properties.converter.postgresql;

import com.wizzdi.dynamic.annotations.service.AnnotationTransformer;
import com.wizzdi.dynamic.annotations.service.cn.xdean.jex.AnnotationUtil;
import com.wizzdi.dynamic.annotations.service.cn.xdean.jex.ReflectUtil;
import com.wizzdi.dynamic.properties.converter.DynamicColumnDefinition;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Component
public class PostgresqlJsonColumnTransformer implements AnnotationTransformer<DynamicColumnDefinition> {

    public static final String POSTGRESQL_DYNAMIC_COLUMN_DEFINITION = "jsonb";

    @Override
    public void applyTransformation(Class<?> clazz) {
        for (Method declaredMethod : clazz.getDeclaredMethods()) {
            if(declaredMethod.isAnnotationPresent(DynamicColumnDefinition.class)){
                Method rootMethod = ReflectUtil.getRootMethod(declaredMethod);
                Column existingColumn=rootMethod.getAnnotation(Column.class);
                if(existingColumn!=null){
                    AnnotationUtil.changeAnnotationValue(existingColumn,"columnDefinition", POSTGRESQL_DYNAMIC_COLUMN_DEFINITION);
                }
                else{
                    AnnotationUtil.addAnnotation(rootMethod, getColumnAnnotation());

                }
            }
        }

    }

    private Column getColumnAnnotation() {
        return new Column(){
            @Override
            public String name() {
                return "";
            }

            @Override
            public boolean unique() {
                return false;
            }

            @Override
            public boolean nullable() {
                return true;
            }

            @Override
            public boolean insertable() {
                return true;
            }

            @Override
            public boolean updatable() {
                return true;
            }

            @Override
            public String columnDefinition() {
                return POSTGRESQL_DYNAMIC_COLUMN_DEFINITION;
            }

            @Override
            public String table() {
                return "";
            }

            @Override
            public int length() {
                return 255;
            }

            @Override
            public int precision() {
                return 0;
            }

            @Override
            public int scale() {
                return 0;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return Column.class;
            }
        };
    }

    @Override
    public Class<DynamicColumnDefinition> getType() {
        return DynamicColumnDefinition.class;
    }
}
