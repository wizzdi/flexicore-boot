/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.service.impl;

import com.flexicore.data.BaselinkRepository;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.model.*;
import com.flexicore.request.BaselinkCreate;
import com.flexicore.request.BaselinkFilter;
import com.flexicore.request.BaselinkMassCreate;
import com.flexicore.request.BaselinkUpdate;
import com.flexicore.security.SecurityContext;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.security.data.SecuredBasicRepository;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import jakarta.ws.rs.BadRequestException;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;


@Primary
@Component("BaselinkServiceBase")
@Extension
public class BaselinkService implements com.flexicore.service.BaselinkService {
   private Logger logger = LoggerFactory.getLogger(getClass().getCanonicalName());
    @Autowired
    private BaselinkRepository repository;
    @Autowired
    @Lazy
    private SecurityService securityService;
    @Autowired
    private SecuredBasicRepository securedBasicRepository;


    @Override
    public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
        return repository.listByIds(c, ids, securityContext);
    }

    @Override
    public <T extends Baseclass> T getById(String id, Class<T> c, List<String> betch, SecurityContext securityContext) {
        return repository.getById(id, c, betch, securityContext);
    }

    @Override
    public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, List<String> batchString, SecurityContext securityContext) {
        return repository.getByIdOrNull(id, c, batchString, securityContext);
    }

    @Override
    public <T extends SecuredBasic> T linkEntities(Baseclass left, Baseclass right, Class<T> clazz) {
        SecurityContext securityContext=securityService.getAdminUserSecurityContext();
        T baselink = createBaselink(new BaselinkCreate().setLeftside(left).setRightside(right).setLinkClass(clazz),securityContext);
        return baselink;
    }

    @Override
    public <T extends SecuredBasic> T linkEntitiesNoCheck(Baseclass left, Baseclass right, Class<T> clazz, SecurityContext securityContext) {
        return linkEntitiesNoCheck(left, right, null, null, clazz, securityContext);

    }

    @Override
    public <T extends SecuredBasic> T linkEntitiesNoCheck(Baseclass left, Baseclass right, Baseclass value, String simpleVal, Class<T> clazz, SecurityContext securityContext) {
        return createBaselink(new BaselinkCreate().setLeftside(left).setRightside(right).setLinkClass(clazz).setValue(value).setSimpleValue(simpleVal),securityContext);
    }


    @Override
    public <T extends SecuredBasic> T linkEntities(Baseclass left, Baseclass right, Class<T> clazz, Baseclass value, String simpleVal) {
        SecurityContext securityContext=securityService.getAdminUserSecurityContext();
        return linkEntitiesNoCheck(left,right,value,simpleVal,clazz,securityContext);
    }

    @Override
    public <T extends SecuredBasic> T findBySides(Class<T> clazz, Baseclass left, Baseclass right) {
        List<T> baselinks = listAllBaselinks(new BaselinkFilter().setLinkClass(clazz).setLeftside(Collections.singletonList(left)).setRightside(Collections.singletonList(right)), null);
        return baselinks.isEmpty()?null:baselinks.get(0);
    }

    @Override
    public void remove(Baseclass object) {
        repository.remove(object, Baseclass.class);

    }

    @Override
    public <T extends Baseclass> T findById(String right) {
        return repository.findById(right);
    }

    @Override
    public <T extends SecuredBasic> T findBySidesAndValue(Baseclass leftside, Baseclass rightside, Baseclass value, Class<T> c) {
        List<T> baselinks = listAllBaselinks(new BaselinkFilter().setLinkClass(c).setLeftside(Collections.singletonList(leftside)).setRightside(Collections.singletonList(rightside)).setValue(value), null);
        return baselinks.isEmpty()?null:baselinks.get(0);
    }


    @Override
    public Baselink findBySidesAndValue(Baseclass leftside, Baseclass rightside, String simplevalue) {
        List<Baselink> baselinks = listAllBaselinks(new BaselinkFilter().setLinkClass(Baselink.class).setLeftside(Collections.singletonList(leftside)).setRightside(Collections.singletonList(rightside)).setSimpleValue(simplevalue), null);
        return baselinks.isEmpty()?null:baselinks.get(0);
    }

    @Override
    public <T extends SecuredBasic> T findBySidesAndValue(Baseclass leftside, Baseclass rightside, Baseclass value, String simpleValue, Class<T> c) {
        List<T> baselinks = listAllBaselinks(new BaselinkFilter().setLinkClass(c).setValue(value).setLeftside(Collections.singletonList(leftside)).setRightside(Collections.singletonList(rightside)).setSimpleValue(simpleValue), null);
        return baselinks.isEmpty()?null:baselinks.get(0);
    }

    @Override
    public void merge(Object o) {
        repository.merge(o);

    }

    @Override
    public <T extends SecuredBasic> T findBySides(Baseclass leftside, Baseclass rightside) {
        List<T> baselinks = listAllBaselinks(new BaselinkFilter().setLinkClass(Baselink.class).setLeftside(Collections.singletonList(leftside)).setRightside(Collections.singletonList(rightside)), null);
        return baselinks.isEmpty()?null:baselinks.get(0);
    }

    @Override
    public <T extends SecuredBasic> List<T> findAllBySides(Class<T> type, Baseclass left, Baseclass right,
                                                       SecurityContext securityContext) {
        return listAllBaselinks(new BaselinkFilter().setLinkClass(type).setLeftside(Collections.singletonList(left)).setRightside(Collections.singletonList(right)), securityContext);

    }


    @Override
    public <T extends SecuredBasic> List<T> findAllBySidesAndValue(Class<T> type, Baseclass left, Baseclass right, Baseclass value, String simpleValue, FilteringInformationHolder filter,
                                                               int pagesize, int current, SecurityContext securityContext) {
        return listAllBaselinks(new BaselinkFilter(filter).setLinkClass(type).setLeftside(Collections.singletonList(left)).setRightside(Collections.singletonList(right)).setPageSize(pagesize).setCurrentPage(current), securityContext);


    }

    @Override
    public <T extends SecuredBasic> List<Baseclass> getAllValues(Class<T> type, Baseclass left, Baseclass right, Baseclass value, String simpleValue, FilteringInformationHolder filter,
                                                             int pagesize, int current, SecurityContext securityContext) {
        List<T> links=findAllBySidesAndValue(type,left,right,value,simpleValue,filter,pagesize,current,securityContext);
        List<Baseclass> values = new ArrayList<>();
        for (T link : links) {
            //if(link.getValue()!=null){
            values.add(link.getValue());

            //}
        }
        return values;

    }


    @Override
    public <T extends SecuredBasic> List<T> findAllBySide(Class<T> linkType, Baseclass base, boolean right, SecurityContext securityContext) {
        BaselinkFilter baselinkFilter = new BaselinkFilter().setLinkClass(linkType);
        if(right){
            baselinkFilter.setRightside(Collections.singletonList(base));
        }
        else{
            baselinkFilter.setLeftside(Collections.singletonList(base));
        }
        return listAllBaselinks(baselinkFilter, securityContext);
    }


    @Override
    public void flush() {
        repository.flush();
    }

    @Override
    public void refrehEntityManager() {
        repository.refrehEntityManager();
    }

    @Override
    public void validate(BaselinkMassCreate createBaselinkRequest, SecurityContext securityContext) {

        if (createBaselinkRequest.getLeftsideIds() == null || createBaselinkRequest.getLeftsideIds().isEmpty()) {
            throw new BadRequestException("Must provide at least one leftside");
        }
        if (createBaselinkRequest.getRightsideIds() == null || createBaselinkRequest.getRightsideIds().isEmpty()) {
            throw new BadRequestException("Must provide at least one rightside");
        }
        Class<? extends SecuredBasic> clazz;
        String linkClazzName = createBaselinkRequest.getLinkClassName();
        try {
            clazz = linkClazzName != null ? (Class<? extends SecuredBasic>) Class.forName(linkClazzName) : null;
        } catch (ClassNotFoundException e) {
            throw new BadRequestException("no class with name:" + linkClazzName);
        }
        createBaselinkRequest.setLinkClass(clazz);
        String rightsideClassName = createBaselinkRequest.getRightsideTypeClassName();
        try {
            Class<?> rawType = rightsideClassName != null ? Class.forName(rightsideClassName) : getLinkSideClass(true, clazz);
            if(!(Baseclass.class.isAssignableFrom(rawType))){
                rawType=Baseclass.class;
            }
            createBaselinkRequest.setRightsideType(rawType);
        } catch (ClassNotFoundException e) {
            throw new BadRequestException("no class with name:" + linkClazzName);
        }

        String leftsideClassName = createBaselinkRequest.getLeftsideTypeClassName();
        try {
            Class<?> rawType = leftsideClassName != null ? Class.forName(leftsideClassName) : getLinkSideClass(false, clazz);
            if(!(Baseclass.class.isAssignableFrom(rawType))){
                rawType=Baseclass.class;
            }
            createBaselinkRequest.setLeftsideType(rawType);
        } catch (ClassNotFoundException e) {
            throw new BadRequestException("no class with name:" + linkClazzName);
        }
        Set<String> rightsideIds = createBaselinkRequest.getRightsideIds();
        Map<String, Baseclass> rightsideMap = rightsideIds.isEmpty() ? new HashMap<>() : listByIds(createBaselinkRequest.getRightsideType(),securityContext,rightsideIds);
        rightsideIds.removeAll(rightsideMap.keySet());
        if (!rightsideIds.isEmpty()) {
            throw new BadRequestException("No Rightside ids " + rightsideIds);
        }
        createBaselinkRequest.setRightside(new ArrayList<>(rightsideMap.values()));
        Set<String> leftsideIds = createBaselinkRequest.getLeftsideIds();
        Map<String, Baseclass> leftsideMap = leftsideIds.isEmpty() ? new HashMap<>() : listByIds(createBaselinkRequest.getLeftsideType(),securityContext,leftsideIds);
        leftsideIds.removeAll(leftsideMap.keySet());
        if (!leftsideIds.isEmpty()) {
            throw new BadRequestException("No Leftside ids " + leftsideIds);
        }
        createBaselinkRequest.setLeftside(new ArrayList<>(leftsideMap.values()));
        Baseclass value = createBaselinkRequest.getValueId() != null ? getByIdOrNull(createBaselinkRequest.getValueId(), Baseclass.class, null, securityContext) : null;
        if (value == null && createBaselinkRequest.getValueId() != null) {
            throw new BadRequestException("No Value with id " + createBaselinkRequest.getValueId());
        }
        createBaselinkRequest.setValue(value);
    }

    @Override
    public void validate(BaselinkFilter baselinkFilter, SecurityContext securityContext) {

        Class<? extends SecuredBasic> clazz;
        String linkClazzName = baselinkFilter.getLinkClassName();
        try {
            clazz = linkClazzName != null ? (Class<? extends SecuredBasic>) Class.forName(linkClazzName) : null;
        } catch (ClassNotFoundException e) {
            throw new BadRequestException("no class with name:" + linkClazzName);
        }
        String rightsideClassName = baselinkFilter.getRightsideTypeClassName();
        try {
            baselinkFilter.setRightsideType(rightsideClassName != null ?  Class.forName(rightsideClassName) : getLinkSideClass(true,clazz));
        } catch (ClassNotFoundException e) {
            throw new BadRequestException("no class with name:" + linkClazzName);
        }

        String leftsideClassName = baselinkFilter.getLeftsideTypeClassName();
        try {
            baselinkFilter.setLeftsideType(leftsideClassName != null ?  Class.forName(leftsideClassName) : getLinkSideClass(false,clazz));
        } catch (ClassNotFoundException e) {
            throw new BadRequestException("no class with name:" + linkClazzName);
        }

        baselinkFilter.setLinkClass(clazz);
        Set<String> rightsideIds = baselinkFilter.getRightsideIds();
        Map<String, Baseclass> rightsideMap = rightsideIds.isEmpty() ? new HashMap<>() : listByIds(baselinkFilter.getRightsideType(), securityContext, rightsideIds);
        rightsideIds.removeAll(rightsideMap.keySet());
        if (!rightsideIds.isEmpty()) {
            throw new BadRequestException("No Rightside ids " + rightsideIds);
        }
        baselinkFilter.setRightside(new ArrayList<>(rightsideMap.values()));
        Set<String> leftsideIds = baselinkFilter.getLeftsideIds();
        Map<String, Baseclass> leftsideMap = leftsideIds.isEmpty() ? new HashMap<>() :  listByIds(baselinkFilter.getLeftsideType(), securityContext, leftsideIds);
        leftsideIds.removeAll(leftsideMap.keySet());
        if (!leftsideIds.isEmpty()) {
            throw new BadRequestException("No Leftside ids " + leftsideIds);
        }
        baselinkFilter.setLeftside(new ArrayList<>(leftsideMap.values()));
        Baseclass value = baselinkFilter.getValueId() != null ? getByIdOrNull(baselinkFilter.getValueId(), Baseclass.class, null, securityContext) : null;
        if (value == null && baselinkFilter.getValueId() != null) {
            throw new BadRequestException("No Value with id " + baselinkFilter.getValueId());
        }
        baselinkFilter.setValue(value);
    }

    private Map<String, Baseclass> listByIds(Class<?> type, SecurityContext securityContext, Set<String> rightsideIds) {
        if(Baseclass.class.isAssignableFrom(type)){
            Class<? extends Baseclass> baseclassType= (Class<? extends Baseclass>) type;
            return repository.listByIds(baseclassType, rightsideIds, securityContext).parallelStream().collect(Collectors.toMap(f -> f.getId(), f -> f));
        }
        if(SecuredBasic.class.isAssignableFrom(type)){
            Class<? extends SecuredBasic> securedBasicType= (Class<? extends SecuredBasic>) type;
            return securedBasicRepository.listByIds(securedBasicType, rightsideIds, SecuredBasic_.security, securityContext).parallelStream().collect(Collectors.toMap(f -> f.getId(), f -> f.getSecurity(),(a,b)->a));

        }
        throw new BadRequestException("cannot list by ids type "+type);
    }

    private Class<? extends Baseclass> getLinkSideClass(boolean rightside, Class<? extends SecuredBasic> linkClass) {
        String name = rightside ? "getRightside" : "getLeftside";

        try {
            return (Class<? extends Baseclass>) linkClass.getMethod(name).getReturnType();
        } catch (Exception e) {
            logger.error("failed getting type of "+name +" to determine type");
        }
        return Baseclass.class;
    }

    @Override
    public List<Baselink> massCreateBaselink(BaselinkMassCreate createBaselinkRequest, SecurityContext securityContext) {
        BaselinkFilter filter = new BaselinkFilter()
                .setLeftside(createBaselinkRequest.getLeftside())
                .setRightside(createBaselinkRequest.getRightside())
                .setValue(createBaselinkRequest.getValue())
                .setSimpleValue(createBaselinkRequest.getSimpleValue())
                .setLinkClass(createBaselinkRequest.getLinkClass());
        List<Baselink> allBaselinks = listAllBaselinks(filter, null);
        Map<String, Map<String, Baselink>> existing = allBaselinks.parallelStream().collect(Collectors.groupingBy(f -> f.getLeftside().getId(), Collectors.toMap(f -> f.getRightside().getId(), f -> f, (a, b) -> a)));
        List<Object> toMerge = new ArrayList<>();
        for (Baseclass leftside : createBaselinkRequest.getLeftside()) {
            Map<String, Baselink> leftsideMap = existing.computeIfAbsent(leftside.getId(), f -> new HashMap<>());
            for (Baseclass rightside : createBaselinkRequest.getRightside()) {
                if (leftsideMap.get(rightside.getId()) == null) {
                	BaselinkCreate baselinkCreate=new BaselinkCreate()
							.setLeftside(leftside)
							.setRightside(rightside)
							.setValue(createBaselinkRequest.getValue())
							.setSimpleValue(createBaselinkRequest.getSimpleValue())
							.setLinkClass(createBaselinkRequest.getLinkClass())
							.setName(createBaselinkRequest.getLinkClass().getSimpleName());

                    Baselink baselink = createBaselinkNoMerge(baselinkCreate,securityContext);
                    toMerge.add(baselink);
                    leftsideMap.put(baselink.getId(), baselink);
                    allBaselinks.add(baselink);
                }
            }
        }
        repository.massMerge(toMerge);
        return allBaselinks;

    }

	@Override
    public <T extends SecuredBasic> T createBaselink(BaselinkCreate baselinkCreate, SecurityContext securityContext) {
        Class<T> linkClass = (Class<T>) baselinkCreate.getLinkClass();
        BaselinkFilter baselinkFilter=new BaselinkFilter()
                .setLeftside(Collections.singletonList(baselinkCreate.getLeftside()))
                .setRightside(Collections.singletonList(baselinkCreate.getRightside()))
                .setValue(baselinkCreate.getValue())
                .setSimpleValue(baselinkCreate.getSimpleValue())
                .setLinkClass(linkClass);

        List<T> existing=repository.getAllBaselinks(linkClass,baselinkFilter,securityContext);
        if(existing.isEmpty()){
            T baseLink=createBaselinkNoMerge(baselinkCreate, securityContext);
            repository.merge(baseLink);
            return baseLink;
        }
        else{
            return existing.get(0);
        }

	}

	@Override
    public Baselink updateBaselink(BaselinkUpdate baselinkUpdate, SecurityContext securityContext) {
		Baselink baseLink=baselinkUpdate.getBaselink();
		if(updateBaselinkNoMerge(baseLink,baselinkUpdate)){
			repository.merge(baseLink);
		}
		return baseLink;
	}

    @Override
    public <T extends SecuredBasic> T createBaselinkNoMerge(BaselinkCreate baselinkCreate, SecurityContext securityContext) {
        try {
            Class<T> c = (Class<T>) baselinkCreate.getLinkClass();
            Constructor<T> constructor = c.getConstructor(String.class, SecurityContextBase.class);
            T link = constructor.newInstance(baselinkCreate.getName(), securityContext);
            updateBaselinkNoMerge(link,baselinkCreate);
            return link;
        }
        catch (Exception e){
            logger.error("failed creating baselink",e);
            return null;
        }


    }

	private <T extends SecuredBasic> boolean updateBaselinkNoMerge(T link, BaselinkCreate baselinkCreate) {
		boolean update=false;
		if(baselinkCreate.getName()!=null && !baselinkCreate.getName().equals(link.getName())){
			link.setName(baselinkCreate.getName());
			update=true;
		}
		if(baselinkCreate.getDescription()!=null && !baselinkCreate.getDescription().equals(link.getDescription())){
			link.setDescription(baselinkCreate.getDescription());
			update=true;
		}
		if(baselinkCreate.getSimpleValue()!=null && !baselinkCreate.getSimpleValue().equals(link.getSimplevalue())){
			link.setSimplevalue(baselinkCreate.getSimpleValue());
			update=true;
		}
        if(baselinkCreate.getLeftside()!=null && (link.getLeftside()==null||!baselinkCreate.getLeftside().getId().equals(link.getLeftside().getId()))){
            link.setLeftside(baselinkCreate.getLeftside());
            update=true;
        }
        if(baselinkCreate.getRightside()!=null && (link.getRightside()==null||!baselinkCreate.getRightside().getId().equals(link.getRightside().getId()))){
            link.setRightside(baselinkCreate.getRightside());
            update=true;
        }


		if(baselinkCreate.getValue()!=null && (link.getValue()==null||!baselinkCreate.getValue().getId().equals(link.getValue().getId()))){
			link.setValue(baselinkCreate.getValue());
			update=true;
		}

		return update;
	}

	@Override
    public PaginationResponse<Baselink> getAllBaselinks(BaselinkFilter baselinkFilter, SecurityContext securityContext) {

        List<Baselink> list = listAllBaselinks(baselinkFilter, securityContext);
        long count = repository.countAllBaselinks(baselinkFilter.getLinkClass(), baselinkFilter, securityContext);
        return new PaginationResponse<>(list, baselinkFilter, count);
    }

    @Override
    public <T extends SecuredBasic> List<T> listAllBaselinks(BaselinkFilter baselinkFilter, SecurityContext securityContext) {
        return repository.getAllBaselinks((Class<T>) baselinkFilter.getLinkClass(), baselinkFilter, securityContext);
    }


    @Override
    public void validate(BaselinkCreate baselinkCreate, SecurityContext securityContext) {

		Class<? extends SecuredBasic> clazz;
		String linkClazzName = baselinkCreate.getLinkClassName();
		try {
			clazz = linkClazzName != null ? (Class<? extends SecuredBasic>) Class.forName(linkClazzName) : null;
		} catch (ClassNotFoundException e) {
			throw new BadRequestException("no class with name:" + linkClazzName);
		}
		baselinkCreate.setLinkClass(clazz);
		String leftsideId=baselinkCreate.getLeftsideId();
		Baseclass leftside=leftsideId==null?null:getByIdOrNull(leftsideId,Baseclass.class,null,securityContext);
		if(leftsideId!=null&&leftside==null){
			throw new BadRequestException("No Baseclass with id "+leftsideId);
		}
		baselinkCreate.setLeftside(leftside);

		String rightsideId=baselinkCreate.getRightsideId();
		Baseclass rightside=rightsideId==null?null:getByIdOrNull(rightsideId,Baseclass.class,null,securityContext);
		if(rightsideId!=null&&rightside==null){
			throw new BadRequestException("No Baseclass with id "+rightsideId);
		}
		baselinkCreate.setRightside(rightside);

		String valueId=baselinkCreate.getValueId();
		Baseclass value=valueId==null?null:getByIdOrNull(valueId,Baseclass.class,null,securityContext);
		if(valueId!=null&&value==null){
			throw new BadRequestException("No Baseclass with id "+valueId);
		}
		baselinkCreate.setValue(value);


	}
}
