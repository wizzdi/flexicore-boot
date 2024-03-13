package com.wizzdi.dynamic.properties.converter.postgresql;


import jakarta.persistence.criteria.*;

import java.util.*;

public class FilterStaticPropertiesUtils {


    public static List<Predicate> filterStatic(Map<String, DynamicFilterItem> genericPredicates, CriteriaBuilder cb, Root<?> r) {
        List<Predicate> existingPredicates = new ArrayList<>();

        filterStatic(genericPredicates, cb,r, existingPredicates);
        return existingPredicates;
    }


    public static void filterStatic(Map<String, DynamicFilterItem> genericPredicates, CriteriaBuilder cb, From<?,?> current, List<Predicate> existingPredicates) {
        for (Map.Entry<String, DynamicFilterItem> entry : genericPredicates.entrySet()) {
            DynamicFilterItem dynamicFilterItem = entry.getValue();
            String key = entry.getKey();
            if (dynamicFilterItem instanceof DynamicNodeItem node) {
                From<?,?> newPath=current.join(key);
                filterStatic(node.getChildren(), cb, newPath,existingPredicates);
            }
            if(dynamicFilterItem instanceof DynamicPredicateItem dynamicPredicate) {
                Expression<?> newPath=current.get(key);

                Object value = dynamicPredicate.getValue();

                Predicate pred= switch (dynamicPredicate.getFilterType()){

                    case EQUALS -> cb.equal(newPath, value);
                    case NOT_EQUALS -> cb.notEqual(newPath, value);
                    case IN -> newPath.in((Collection<?>) value);
                    case NOT_IN -> cb.not(newPath.in(value));
                    case CONTAINS -> cb.like((Expression<String>)newPath, "%" + value + "%");
                    case LESS_THAN -> cb.lessThan((Expression<Comparable>)newPath, ((Comparable) value));
                    case LESS_THAN_OR_EQUAL ->  cb.lessThanOrEqualTo((Expression<Comparable>)newPath, ((Comparable) value));
                    case GREATER_THAN -> cb.greaterThan((Expression<Comparable>)newPath, ((Comparable) value));
                    case GREATER_THAN_OR_EQUAL -> cb.greaterThanOrEqualTo((Expression<Comparable>)newPath, ((Comparable) value));
                    case IS_NULL -> newPath.isNull();
                    case IS_NOT_NULL -> newPath.isNotNull();
                };
                existingPredicates.add(pred);

            }
        }

    }
}
