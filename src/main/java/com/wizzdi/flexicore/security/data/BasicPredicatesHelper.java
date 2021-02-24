package com.wizzdi.flexicore.security.data;

import com.flexicore.model.Basic;
import com.flexicore.model.Basic_;
import com.wizzdi.flexicore.security.request.BasicPropertiesFilter;
import com.wizzdi.flexicore.security.request.PaginationFilter;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CommonAbstractCriteria;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import java.util.List;


public class BasicPredicatesHelper {

	public static <T> boolean addPagination(PaginationFilter basicFilter, TypedQuery<T> q){
		if(basicFilter.getPageSize()!=null&&basicFilter.getCurrentPage()!=null&&basicFilter.getCurrentPage()>=0&&basicFilter.getPageSize()>0){
			q.setFirstResult(basicFilter.getCurrentPage()*basicFilter.getPageSize());
			q.setMaxResults(basicFilter.getPageSize());
		}
		return false;
	}

	public static <T extends Basic> void addBasicPropertiesFilter(BasicPropertiesFilter basicPropertiesFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates) {
		if(basicPropertiesFilter.getNames()!=null&&!basicPropertiesFilter.getNames().isEmpty()){
			predicates.add(r.get(Basic_.name).in(basicPropertiesFilter.getNames()));
		}
		if(basicPropertiesFilter.getNameLike()!=null&&!basicPropertiesFilter.getNameLike().isEmpty()){
			predicates.add(cb.like(r.get(Basic_.name),basicPropertiesFilter.getNameLike()));
		}
	}

	
}
