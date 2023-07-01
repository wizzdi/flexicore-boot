/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flexicore.annotations.Baseclassroot;
import com.flexicore.data.BaseclassRepository;
import com.flexicore.data.BaselinkRepository;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.data.jsoncontainers.SetBaseclassTenantRequest;
import com.flexicore.data.jsoncontainers.Views;
import com.flexicore.interfaces.dynamic.ListingInvoker;
import com.flexicore.model.*;
import com.flexicore.request.*;
import com.flexicore.response.BaseclassCount;
import com.flexicore.security.SecurityContext;
import com.wizzdi.flexicore.boot.dynamic.invokers.request.DynamicExecutionExampleRequest;
import com.wizzdi.flexicore.boot.dynamic.invokers.response.ParameterInfo;
import com.wizzdi.flexicore.boot.dynamic.invokers.service.DynamicExecutionService;
import com.wizzdi.flexicore.file.model.FileResource;
import com.wizzdi.flexicore.security.data.BasicRepository;
import com.wizzdi.flexicore.security.data.SecuredBasicRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.pf4j.Extension;
import org.pf4j.PluginManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import jakarta.persistence.OneToMany;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.core.Response;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Primary
@Component("BaseclassServiceBase")
@Extension
public class BaseclassService implements com.flexicore.service.BaseclassService {
    @Autowired
    @Baseclassroot
    private BaseclassRepository baseclassRepository;

    @Autowired
    private BasicRepository basicRepository;
    @Autowired
    private SecuredBasicRepository securedBasicRepository;

    @Autowired
    private BaselinkRepository baselinkRepository;

    private static final Logger logger = LoggerFactory.getLogger(BaseclassService.class);
    private static ObjectMapper objectMapper;

    @Autowired
    private FileResourceService fileResourceService;

    @Autowired
    @Lazy
    private PluginManager pluginManager;

    @Autowired
    private OperationService operationService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private BaselinkService baselinkService;

    @Autowired
    private DynamicExecutionService dynamicInvokersService;

    private static Map<String, LinkSide> sideCache = new ConcurrentHashMap<>();


    static {
        objectMapper = new ObjectMapper();
        objectMapper.setConfig(objectMapper.getSerializationConfig().withView(Views.ForSwaggerOnly.class).with());


    }

    private static final Set<String> knownTypes = new HashSet<>(Arrays.asList(OffsetDateTime.class.getCanonicalName(),LocalDateTime.class.getCanonicalName(),
            Date.class.getCanonicalName(), ZonedDateTime.class.getCanonicalName(), List.class.getCanonicalName(), Map.class.getCanonicalName()));


    public BaseclassService() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public <T extends Baseclass> long count(Class<T> clazz, FilteringInformationHolder filteringInformationHolder, SecurityContext securityContext) {
        QueryInformationHolder<T> queryInformationHolder = new QueryInformationHolder<>(filteringInformationHolder, clazz, securityContext);
        return baseclassRepository.countAllFiltered(queryInformationHolder);
    }


    @Override
    public void persist(Baseclass base) {
        baseclassRepository.Persist(base);
    }

    @Override
    public <T extends Baseclass> T find(Class<T> type, String id) {
        return baseclassRepository.findById(type, id);
    }

    @Override
    public <T extends Baseclass> T findByIdOrNull(Class<T> type, String id) {
        return baseclassRepository.findByIdOrNull(type, id);
    }


    @Override
    public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
        return baseclassRepository.listByIds(c, ids, securityContext);
    }

    @Override
    public boolean remove(Baseclass base) {
        return baseclassRepository.remove(base, Baseclass.class);
    }

    @Override
    public <T extends Baseclass> int removeById(String id, QueryInformationHolder<T> queryInformationHolder) {
        return baseclassRepository.removeById(id, queryInformationHolder);
    }

    @Override
    public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, List<String> batchString, SecurityContext securityContext) {
        return baseclassRepository.getByIdOrNull(id, c, batchString, securityContext);
    }

    @Override
    public <T extends Baseclass> List<T> getAllByKeyWordAndCategory(QueryInformationHolder<T> queryInformationHolder) {
        return baseclassRepository.getAllFiltered(queryInformationHolder);
    }

    @Override
    public <T extends Baseclass> List<T> getAllFiltered(QueryInformationHolder<T> queryInformationHolder) {
        return baseclassRepository.getAllFiltered(queryInformationHolder);
    }

    @Override
    public <T extends Baseclass> List<T> getAllFiltered(FilteringInformationHolder filteringInformationHolder, Class<T> c, SecurityContext securityContext) {
        return baseclassRepository.getAllFiltered(new QueryInformationHolder<>(filteringInformationHolder, c, securityContext));
    }

    @Override
    public <T extends Baseclass> long countAllFiltered(QueryInformationHolder<T> queryInformationHolder) {
        return baseclassRepository.countAllFiltered(queryInformationHolder);
    }

    @Override
    public <T extends Baseclass> long countAllFiltered(FilteringInformationHolder filteringInformationHolder, Class<T> c, SecurityContext securityContext) {
        return baseclassRepository.countAllFiltered(new QueryInformationHolder<>(filteringInformationHolder, c, securityContext));
    }


    @Override
    public <T extends Baseclass> String serializeBaseclssForExport(T baseclass, SecurityContext securityContext) throws JsonProcessingException {
        return objectMapper.writeValueAsString(baseclass);
    }


    @Override
    public <T extends Baseclass> T deserializeBaseclassForImport(String json, Class<T> type, SecurityContext securityContext) throws IOException {
        return objectMapper.readValue(json, type);
    }

    @Override
    public void validate(MassDeleteRequest massDeleteRequest, SecurityContext securityContext) {
        Set<String> ids = massDeleteRequest.getIds();
        Map<String, Baseclass> map = ids.isEmpty() ? new HashMap<>() : listByIds(Baseclass.class, ids, securityContext).parallelStream().collect(Collectors.toMap(f -> f.getId(), f -> f));
        ids.removeAll(map.keySet());
        massDeleteRequest.setBaseclass(new ArrayList<>(map.values()));
    }

    @Override
    public void massDelete(MassDeleteRequest massDeleteRequest, SecurityContext securityContext) {
        if (massDeleteRequest.getBaseclass().isEmpty()) {
            return;
        }
        baseclassRepository.massDelete(massDeleteRequest);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Baselink, E extends Baseclass> List<String> getConnectedClasses(Clazz c, String id, Clazz linkClazz,
                                                                                      FilteringInformationHolder filteringInformationHolder, int pagesize, int currentPage, Baseclass value, String simpleValue, SecurityContext securityContext) {
        try {
            ;
            Class<E> type = (Class<E>) Class.forName(c.getName());
            Class<T> linkclass = (Class<T>) Class.forName(linkClazz.getName());
            Boolean right = onRight(type, linkclass);
            if (right == null) {
                throw new BadRequestException("could not find connection between " + c.getName() + " and " + linkClazz.getName());
            }
            Baseclass base = baseclassRepository.getByIdOrNull(id, Baseclass.class, null, securityContext);
            if (base == null) {
                throw new BadRequestException("No Baseclass with id " + id);
            }

            return null;//baselinkRepository.getConnectedClasses(linkclass, type, base, right, filteringInformationHolder, pagesize, currentPage, value, simpleValue, securityContext);
        } catch (ClassNotFoundException e) {
            throw new BadRequestException("could not find class ", e);
        }


    }

    @Override
    public ParameterInfo getClassInfo(GetClassInfo filteringInformationHolder) {
        return null;
    }

    @Override
    public Object getExample(GetClassInfo filteringInformationHolder) {
        DynamicExecutionExampleRequest dynamicExecutionExampleRequest = new DynamicExecutionExampleRequest().setId(filteringInformationHolder.getClassName());
        dynamicInvokersService.validate(dynamicExecutionExampleRequest,null);
      return dynamicInvokersService.getExample(dynamicExecutionExampleRequest.getClazz());

    }

    @Override
    public <T, E extends FilteringInformationHolder> FileResource exportBaseclassGeneric(ExportBaseclassGeneric<E> baseclassGeneric, SecurityContext securityContext) {
        FilteringInformationHolder filteringInformationHolder = baseclassGeneric.getFilter();
        Map<String, FieldProperties> fieldToName = baseclassGeneric.getFieldToName();
        Map<String, Method> fieldNameToMethod = new HashMap<>();
        PaginationResponse<T> paginationResponse = listAllBaseclassGeneric(filteringInformationHolder, securityContext);
        List<T> collection = paginationResponse.getList();
        String[] headersArr  = fieldToName.values().stream().sorted(Comparator.comparing(FieldProperties::getOrdinal)).map(FieldProperties::getName).toArray(String[]::new);
        CSVFormat format = baseclassGeneric.getCsvFormat();
        File file = new File(com.flexicore.service.FileResourceService.generateNewPathForFileResource("dynamic-execution-csv", securityContext.getUser()) + ".csv");

        if (CSVFormat.EXCEL.equals(format)) {
            try (Writer out = new OutputStreamWriter(new FileOutputStream(file, true))) {
                out.write(ByteOrderMark.UTF_BOM);

            } catch (Exception e) {
                logger.error( "failed writing UTF-8 BOM", e);
            }


        }
        format = format.withHeader(headersArr);
        try (Writer out = new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8);
             CSVPrinter csvPrinter = new CSVPrinter(out, format)) {
            List<List<Object>> expendedRecordSet = DynamicInvokersService.toExpendedRecordSet(fieldToName, fieldNameToMethod, collection);
            csvPrinter.printRecords(expendedRecordSet);
        } catch (Exception e) {
            logger.error( "failed exporting data", e);
        }


        FileResource fileResource = fileResourceService.createDontPersist(file.getAbsolutePath(), securityContext);
        fileResource.setKeepUntil(OffsetDateTime.now().plusMinutes(30));
        fileResourceService.merge(fileResource);
        return fileResource;


    }

    @Override
    public List<BaseclassCount> getBaseclassCount(BaseclassCountRequest baseclassCountRequest, SecurityContext securityContext) {
        return baseclassRepository.getBaseclassCount(baseclassCountRequest, securityContext);
    }

    public void validate(SoftDeleteRequest softDeleteRequest, SecurityContext securityContext) {
        String type=softDeleteRequest.getType();
        String id=softDeleteRequest.getId();
        if(type==null){
            type=Baseclass.class.getCanonicalName();
        }
        Class<? extends Basic> basicType;
        try {
            Class<?> c = Class.forName(type);
            if(!Basic.class.isAssignableFrom(c)){
                throw new BadRequestException("Type "+type +" is not assignable from Basic");
            }
            basicType= (Class<? extends Basic>) c;
        }
        catch (Throwable e){
            throw new BadRequestException("No Type "+type);
        }
        softDeleteRequest.setClazz(basicType);
        Basic basic;
        if(SecuredBasic.class.isAssignableFrom(basicType)){
            basic=securedBasicRepository.getByIdOrNull(id,(Class<? extends SecuredBasic>)basicType,SecuredBasic_.security,securityContext);
        }
        else{
            if(Baseclass.class.isAssignableFrom(basicType)){
                basic=baseclassRepository.getByIdOrNull(id,Baseclass.class,null,securityContext);
            }
            else{
                basic=basicRepository.findByIdOrNull(basicType,id);
            }
        }
        if (basic == null) {
            throw new BadRequestException("could not find basic with id: " + id);
        }
        softDeleteRequest.setBasic(basic);
    }

    public <T extends Basic> T findById(String id, String classname, SecurityContext securityContext) {
            Class<T> clazz;
            try {
                clazz = (Class<T>) Class.forName(classname);
                if(Baseclass.class.isAssignableFrom(clazz)){
                    return (T) findByIdBaseclass(id,(Class<? extends Baseclass>)clazz,securityContext);
                }
                if(SecuredBasic.class.isAssignableFrom(clazz)){
                    return (T) findByIdSecuredBasic(id,(Class<? extends SecuredBasic>)clazz,securityContext);
                }
                throw new BadRequestException("could not determine how to fetch entity of type "+clazz);

            } catch (ClassNotFoundException e) {
                throw new BadRequestException("unable to find class: ", e);
            }

    }

    private <T extends Baseclass> T findByIdBaseclass(String id, Class<T> clazz, SecurityContext securityContext) {
        long start = System.currentTimeMillis();
        T result = baseclassRepository.getByIdOrNull(id, clazz, null, securityContext);
        if(result==null){
            throw new BadRequestException("no baseclass of type "+clazz.getSimpleName() +" with id "+ id);
        }
        logger.info( "Find by id took: " + (System.currentTimeMillis() - start) + " MS");
        return result;
    }

    private <T extends SecuredBasic> T findByIdSecuredBasic(String id, Class<T> clazz, SecurityContext securityContext) {
        long start = System.currentTimeMillis();
        T result = securedBasicRepository.getByIdOrNull(id, clazz, SecuredBasic_.security, securityContext);
        if(result==null){
            throw new BadRequestException("no securedBasic of type "+clazz.getSimpleName() +" with id "+ id);
        }
        logger.info( "Find by id took: " + (System.currentTimeMillis() - start) + " MS");
        return result;
    }

    public enum LinkSide {
        RIGHT, LEFT, NONE
    }

    @Override
    public void validate(GetDisconnected getDisconnected, SecurityContext securityContext) {
        if (getDisconnected.getWantedClassName() == null) {
            throw new BadRequestException("No Classname " + getDisconnected.getWantedClassName());
        }
        try {
            Class<?> wantedClass = Class.forName(getDisconnected.getWantedClassName());
            getDisconnected.setWantedClass(wantedClass);
        } catch (ClassNotFoundException e) {
            throw new BadRequestException("No Class with name " + getDisconnected.getWantedClassName());
        }
        BaselinkFilter baselinkFilter = getDisconnected.getBaselinkFilter();
        if (baselinkFilter == null) {
            throw new BadRequestException("baselink filter must be non null");
        }
        if (baselinkFilter.getLinkClassName() == null) {
            throw new BadRequestException("Baselink filter class name must be non null");
        }
        baselinkService.validate(baselinkFilter, securityContext);
    }

    @Override
    public void validate(GetConnected getConnected, SecurityContext securityContext) {
        if (getConnected.getWantedClassName() == null) {
            throw new BadRequestException("No Classname " + getConnected.getWantedClassName());
        }
        try {
            Class<? extends Baseclass> wantedClass = (Class<? extends Baseclass>) Class.forName(getConnected.getWantedClassName());
            getConnected.setWantedClass(wantedClass);
        } catch (ClassNotFoundException e) {
            throw new BadRequestException("No Class with name " + getConnected.getWantedClassName());
        }
        if (getConnected.getBaselinkFilter() == null) {
            throw new BadRequestException("baselink filter must be non null");
        }
        if (getConnected.getBaselinkFilter().getLinkClassName() == null) {
            throw new BadRequestException("Baselink filter class name must be non null");
        }
        baselinkService.validate(getConnected.getBaselinkFilter(), securityContext);
    }


    @Override
    public <T extends Baselink, E extends Baseclass> PaginationResponse<E> getDisconnected(GetDisconnected getDisconnected, SecurityContext securityContext) {
        Class<?> rawType = getDisconnected.getWantedClass();
        if(!(Baseclass.class.isAssignableFrom(rawType))){
            Clazz clazz=Baseclass.getClazzByName(rawType.getCanonicalName());
            if(clazz==null){
                throw new BadRequestException("Could not find clazz for non baseclass type "+rawType);
            }
            rawType=Baseclass.class;
            getDisconnected.setWantedClass(rawType);
            getDisconnected.setClazzIds(Collections.singletonList(new ClazzIdFiltering().setId(clazz.getId())));
        }
        Class<E> type = (Class<E>) rawType;
        Class<T> linkclass = (Class<T>) getDisconnected.getBaselinkFilter().getLinkClass();
        Boolean right = onRight(type, linkclass);
        if (right == null) {
            throw new BadRequestException("could not find connection between " + type.getName() + " and " + linkclass.getName());
        }
        List<E> list = baselinkRepository.getDisconnected(getDisconnected, right, securityContext);
        long count = baselinkRepository.countDisconnected(getDisconnected, right, securityContext);
        ;
        return new PaginationResponse<>(list, getDisconnected, count);
    }

    @Override
    public <T extends Baselink, E extends Baseclass> PaginationResponse<E> getConnected(GetConnected getConnected, SecurityContext securityContext) {
        Class<?> rawType = getConnected.getWantedClass();
        if(!(Baseclass.class.isAssignableFrom(rawType))){
            Clazz clazz=Baseclass.getClazzByName(rawType.getCanonicalName());
            if(clazz==null){
                throw new BadRequestException("Could not find clazz for non baseclass type "+rawType);
            }
            rawType=Baseclass.class;
            getConnected.setWantedClass(rawType);
            getConnected.setClazzIds(Collections.singletonList(new ClazzIdFiltering().setId(clazz.getId())));
        }
        Class<E> type = (Class<E>) rawType;
        Class<T> linkclass = (Class<T>) getConnected.getBaselinkFilter().getLinkClass();
        Boolean right = onRight(type, linkclass);
        if (right == null) {
            throw new BadRequestException("could not find connection between " + type.getName() + " and " + linkclass.getName());
        }
        List<E> list = baselinkRepository.getConnected(getConnected, right, securityContext);
        long count = baselinkRepository.countConnected(getConnected, right, securityContext);
        return new PaginationResponse<>(list, getConnected, count);

    }

    @Override
    public <T extends Baselink> Boolean onRight(Class<?> wanted, Class<T> link) {
        String key = getOnRightKey(wanted, link);

        LinkSide res = sideCache.get(key);
        if (res == null) {

            int inheritenceDist = Integer.MAX_VALUE;
            List<Field> fields = FieldUtils.getFieldsListWithAnnotation(wanted, OneToMany.class);

            for (Field field : fields) {
                if (field.getType().equals(List.class)) {
                    OneToMany oneToMany = field.getAnnotation(OneToMany.class);
                    if (oneToMany != null) {
                        Class<?> aClass = oneToMany.targetEntity();
                        if (aClass.isAssignableFrom(link)) {
                            int dist = calculateInheritanceDist(aClass, link);
                            if (dist < inheritenceDist) {
                                res = oneToMany.mappedBy().equalsIgnoreCase("rightside") ? LinkSide.RIGHT : LinkSide.LEFT;
                                inheritenceDist = dist;

                            }
                        }
                        if (link.isAssignableFrom(aClass)) {
                            int dist = calculateInheritanceDist(link, aClass);
                            if (dist < inheritenceDist) {
                                res = oneToMany.mappedBy().equalsIgnoreCase("rightside") ? LinkSide.RIGHT : LinkSide.LEFT;
                                inheritenceDist = dist;

                            }
                        }
                    }
                }

            }
            if (res == null) {
                res = LinkSide.NONE;
            }
            sideCache.put(key, res);
        }

        return LinkSide.NONE.equals(res) ? null : LinkSide.RIGHT.equals(res);
    }

    private String getOnRightKey(Class<?> wanted, Class<?> link) {
        return wanted.getCanonicalName() + "-" + link.getCanonicalName();
    }

    private int calculateInheritanceDist(Class<?> parent, Class<?> child) {
        int i = 0;
        for (Class<?> current = child; current != null; current = current.getSuperclass()) {
            if (current.equals(parent)) {
                return i;
            }
            i++;
        }
        return Integer.MAX_VALUE;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Baselink> List<Baseclass> getDisconnected(Clazz c, String id, Clazz linkClazz,
                                                                FilteringInformationHolder filteringInformationHolder, int pagesize, int currentPage, Baseclass value, String simpleValue, SecurityContext securityContext) throws Exception {
        List<Baseclass> list;
        Class<?> type = Class.forName(c.getName());
        Class<T> linkclass = (Class<T>) Class.forName(linkClazz.getName());
        Boolean right = onRight(type, linkclass);
        if (right == null) {
            throw new Exception("could not find connection between " + c.getName() + " and " + linkClazz.getName());
        }
        Baseclass base = baseclassRepository.getById(id, Baseclass.class, null, securityContext);

        list = null;//baselinkRepository.getdisconnectedBaseLinksBaseClassesBySide(linkclass, c, base, right, filteringInformationHolder, pagesize, currentPage, value, simpleValue, securityContext);


        return list;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Baselink> long countDisconnected(Clazz c, String id, Clazz linkClazz,
                                                       FilteringInformationHolder filteringInformationHolder, int pagesize, int currentPage, Baseclass value, String simpleValue, SecurityContext securityContext) throws Exception {
        Class<?> type = Class.forName(c.getName());
        Class<T> linkclass = (Class<T>) Class.forName(linkClazz.getName());
        Boolean right = onRight(type, linkclass);
        if (right == null) {
            throw new Exception("could not find connection between " + c.getName() + " and " + linkClazz.getName());
        }
        Baseclass base = baseclassRepository.getById(id, Baseclass.class, null, securityContext);

        return 0;//baselinkRepository.countDisconnectedBaseLinksBaseClassesBySide(linkclass, c, base, right, filteringInformationHolder, pagesize, currentPage, value, simpleValue, securityContext);


    }


    @Override
    @SuppressWarnings("unchecked")
    public <T extends Baselink, E extends Baseclass> List<E> getConnected(Clazz c, String id, Clazz linkClazz,
                                                                          FilteringInformationHolder filteringInformationHolder, int pagesize, int currentPage, Baseclass value, String simpleValue, SecurityContext securityContext) {
        try {
            ;
            Class<E> type = (Class<E>) Class.forName(c.getName());
            Class<T> linkclass = (Class<T>) Class.forName(linkClazz.getName());
            Boolean right = onRight(type, linkclass);
            if (right == null) {
                throw new BadRequestException("could not find connection between " + c.getName() + " and " + linkClazz.getName());
            }
            Baseclass base = baseclassRepository.getByIdOrNull(id, Baseclass.class, null, securityContext);
            if (base == null) {
                throw new BadRequestException("No Baseclass with id " + id);
            }

            return null;//baselinkRepository.getBaseLinksBaseClassesBySide(linkclass, type, base, right, filteringInformationHolder, pagesize, currentPage, value, simpleValue, securityContext);
        } catch (ClassNotFoundException e) {
            throw new BadRequestException("could not find class ", e);
        }


    }


    @Override
    @SuppressWarnings("unchecked")
    public <T extends Baselink, E extends Baseclass> long countConnected(Clazz c, String id, Clazz linkClazz,
                                                                         FilteringInformationHolder filteringInformationHolder, int pagesize, int currentPage, Baseclass value, String simpleValue, SecurityContext securityContext) {
        try {
            List<Baseclass> list = new ArrayList<>();
            Class<E> type = (Class<E>) Class.forName(c.getName());
            Class<T> linkclass = (Class<T>) Class.forName(linkClazz.getName());
            Boolean right = onRight(type, linkclass);
            if (right == null) {
                throw new BadRequestException("could not find connection between " + c.getName() + " and " + linkClazz.getName());
            }
            Baseclass base = baseclassRepository.getByIdOrNull(id, Baseclass.class, null, securityContext);
            if (base == null) {
                throw new BadRequestException("No Baseclass with id " + id);
            }

            return 0;//baselinkRepository.countBaseLinksBaseClassesBySide(linkclass, type, base, right, filteringInformationHolder, pagesize, currentPage, value, simpleValue, securityContext);

        } catch (ClassNotFoundException e) {
            throw new BadRequestException("could not find class ", e);
        }

    }

    @Override
    public boolean updateInfo(String id, String name, String description, SecurityContext securityContext) {
        Baseclass b = baseclassRepository.getById(id, Baseclass.class, null, securityContext);
        boolean changed = false;
        if (b != null) {
            if (name != null && name.length() < 51 && name.length() > 0) {
                b.setName(name);
                changed = true;
            }

            if (description != null) {
                b.setDescription(description);
                changed = true;
            }
            if (changed) {
                b.setUpdateDate(OffsetDateTime.now());
                baseclassRepository.merge(b);
                return true;
            }
        }

        return false;
    }


    @Override
    public <T extends Baseclass> List<T> getByNameLike(String name, Class<T> c, List<String> batchString,
                                                       SecurityContext securityContext) {
        return baseclassRepository.getByNameLike(name, c, batchString, securityContext);
    }

    @Override
    public <T extends Baseclass> List<T> getAllUnsecure(Class<T> c) {
        return baseclassRepository.getAll(c);
    }


    @Override
    public void merge(Object base) {
        baseclassRepository.merge(base);
    }

    @Override
    public void softDelete(Baseclass baseclass, SecurityContext securityContext) {
       softDelete(new SoftDeleteRequest().setBasic(baseclass),securityContext);

    }

    public void softDelete(SoftDeleteRequest softDeleteRequest, SecurityContext securityContext) {
        Basic basic=softDeleteRequest.getBasic();
        basic.setSoftDelete(true);
        if(basic instanceof Baseclass){
            baseclassRepository.merge(basic);
        }
        else{
            securedBasicRepository.merge(basic);
        }

    }

    @Override
    public long setBaseclassTenant(SetBaseclassTenantRequest setBaseclassTenantRequest, SecurityContext securityContext) {
        List<Object> toMerge = new ArrayList<>();
        Tenant targetTenant = setBaseclassTenantRequest.getTenant();
        for (Baseclass baseclass : setBaseclassTenantRequest.getBaseclasses()) {
            if (baseclass.getTenant() == null || !baseclass.getTenant().getId().equals(targetTenant.getId())) {
                baseclass.setTenant(targetTenant);
                toMerge.add(baseclass);

            }
        }
        baseclassRepository.massMerge(toMerge);
        return toMerge.size();
    }

    @Override
    public void refrehEntityManager() {
        baseclassRepository.refrehEntityManager();
        baselinkRepository.refrehEntityManager();
        fileResourceService.refrehEntityManager();
    }

    @Override
    public <T, E extends FilteringInformationHolder> PaginationResponse<T> listAllBaseclassGeneric(E filteringInformationHolder, SecurityContext securityContext) {

        List<ListingInvoker> plugins = pluginManager.getExtensions(ListingInvoker.class);
        String msg;
        for (ListingInvoker<?, ?> plugin : plugins) {
            if (plugin.getFilterClass().equals(filteringInformationHolder.getClass()) && plugin.getHandlingClass().getCanonicalName().equals(filteringInformationHolder.getResultType())) {
                try {
                    Method method = plugin.getClass().getDeclaredMethod("listAll", plugin.getFilterClass(), SecurityContext.class);
                    String operationId = Baseclass.generateUUIDFromString(method.toString());
                    Operation operation = operationService.findById(operationId);
                    securityContext.setOperation(operation);
                    if (securityService.checkIfAllowed(securityContext)) {
                        ListingInvoker<T, E> invoker = (ListingInvoker<T, E>) plugin;
                        return invoker.listAll(filteringInformationHolder, securityContext);
                    } else {
                        throw new ClientErrorException("user is not authorized for this resource", Response.Status.UNAUTHORIZED);
                    }


                } catch (NoSuchMethodException e) {
                    logger.error( "unable to get method", e);
                }

            }
        }
        msg = "no invoker matches  " + filteringInformationHolder.getResultType() + " with filter type " + filteringInformationHolder.getClass();
        logger.error( msg);

        throw new BadRequestException(msg);


    }


}
