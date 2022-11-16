/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.service.impl;

import com.flexicore.data.DynamicInvokersRepository;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.wizzdi.flexicore.file.model.FileResource;
import com.flexicore.model.Operation;
import com.flexicore.model.SecuredBasic_;
import com.flexicore.model.dynamic.*;
import com.flexicore.request.*;
import com.flexicore.security.SecurityContext;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.dynamic.invokers.model.DynamicExecution;
import com.wizzdi.flexicore.boot.dynamic.invokers.request.*;
import com.wizzdi.flexicore.boot.dynamic.invokers.response.InvokerInfo;
import com.wizzdi.flexicore.boot.dynamic.invokers.service.DynamicExecutionService;
import com.wizzdi.flexicore.boot.dynamic.invokers.service.DynamicInvokerService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.lang3.StringUtils;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.BadRequestException;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.*;

@Primary
@Component
@Extension
public class DynamicInvokersService implements ServicePlugin {


    @Autowired
    private DynamicInvokersRepository dynamicInvokersRepository;


    @Autowired
    private FileResourceService fileResourceService;
    @Autowired
    private DynamicExecutionService dynamicExecutionService;

    @Autowired
    private DynamicInvokerService dynamicInvokerService;


    private static final Logger logger = LoggerFactory.getLogger(DynamicInvokersService.class);


    public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, List<String> batchString, SecurityContext securityContext) {
        return dynamicInvokersRepository.getByIdOrNull(id, c, batchString, securityContext);
    }

    public <T extends DynamicExecution> T getDynamicExectionByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
        return dynamicExecutionService.getByIdOrNull(id, c, SecuredBasic_.security, securityContext);
    }


    public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
        return dynamicInvokersRepository.listByIds(c, ids, securityContext);
    }


    public PaginationResponse<InvokerInfo> getAllInvokersInfo(DynamicInvokerFilter dynamicInvokerFilter, SecurityContext securityContext) {
        return new PaginationResponse<>(dynamicInvokerService.getAllDynamicInvokers(dynamicInvokerFilter, securityContext));
    }


    public PaginationResponse<DynamicInvoker> getAllInvokers(InvokersFilter invokersFilter, SecurityContext securityContext) {
        List<DynamicInvoker> list = dynamicInvokersRepository.getAllInvokers(invokersFilter, securityContext);
        long count = dynamicInvokersRepository.countAllInvokers(invokersFilter, securityContext);
        return new PaginationResponse<>(list, invokersFilter, count);
    }

    private boolean filterInvokers(InvokerInfo InvokerInfo, InvokersFilter filter) {
        return (filter.getInvokerTypes() == null || filter.getInvokerTypes().contains(InvokerInfo.getName().getCanonicalName()))
                && (filter.getClassTypes() == null || classIsAllOf(filter.getClassTypes(), InvokerInfo.getHandlingType()));
    }

    private boolean classIsAllOf(Set<Class<?>> set, Class<?> c) {
        for (Class<?> aClass : set) {
            if (!c.isAssignableFrom(aClass)) {
                return false;
            }
        }
        return true;
    }

    public List<InvokerInfo> getInvokers(Set<String> allowedOps) {
        return dynamicInvokerService.listAllDynamicInvokers(new DynamicInvokerFilter(), null);


    }


    public ExecuteInvokersResponse executeInvoker(ExecuteInvokerRequest executeInvokerRequest, SecurityContext securityContext) {
        return dynamicInvokerService.executeInvoker(executeInvokerRequest, securityContext);


    }


    @Transactional
    public void massMerge(List<?> toMerge) {
        dynamicInvokersRepository.massMerge(toMerge);
    }


    public DynamicInvoker createInvokerNoMerge(CreateInvokerRequest createInvokerRequest, SecurityContext securityContext) {
        DynamicInvoker dynamicInvoker = new DynamicInvoker(createInvokerRequest.getDisplayName(), securityContext);
        if (createInvokerRequest.getCanonicalName() != null) {
            dynamicInvoker.setId(getDynamicInvokerId(createInvokerRequest.getCanonicalName()));
        }
        updateInvokerNoMerge(createInvokerRequest, dynamicInvoker);
        return dynamicInvoker;
    }


    public DynamicExecution createDynamicExecution(CreateDynamicExecution createInvokerRequest, SecurityContext securityContext) {
        return dynamicExecutionService.createDynamicExecution(createInvokerRequest, securityContext);
    }


    public DynamicExecution updateDynamicExecution(UpdateDynamicExecution updateDynamicExecution, SecurityContext securityContext) {
        DynamicExecution dynamicExecution = updateDynamicExecution.getDynamicExecution();
        List<Object> toMerge = new ArrayList<>();
        if (dynamicExecutionService.updateDynamicExecutionNoMerge(updateDynamicExecution, toMerge, dynamicExecution)) {
            toMerge.add(dynamicExecution);
            dynamicExecutionService.massMerge(toMerge);
        }
        return dynamicExecution;
    }


    public boolean updateDynamicExecutionNoMerge(CreateDynamicExecution createDynamicExecution, DynamicExecution dynamicExecution, List<Object> toMerge) {
        return dynamicExecutionService.updateDynamicExecutionNoMerge(createDynamicExecution, toMerge, dynamicExecution);
    }

    private String getDynamicInvokerId(String canonicalName) {
        return Baseclass.generateUUIDFromString("DynamicInvoker-" + canonicalName);
    }

    public List<Operation> getInvokerOperations(InvokersOperationFilter invokersOperationFilter, SecurityContext securityContext) {
        return dynamicInvokersRepository.getInvokersOperations(invokersOperationFilter, securityContext);
    }

    public PaginationResponse<Operation> getInvokerOperationsPagination(InvokersOperationFilter invokersOperationFilter, SecurityContext securityContext) {
        List<Operation> list = dynamicInvokersRepository.getInvokersOperations(invokersOperationFilter, securityContext);
        long count = dynamicInvokersRepository.countInvokersOperations(invokersOperationFilter, securityContext);
        return new PaginationResponse<>(list, invokersOperationFilter, count);
    }

    public boolean updateInvokerNoMerge(CreateInvokerRequest createInvokerRequest, DynamicInvoker invoker) {
        boolean update = false;
        if (createInvokerRequest.getCanonicalName() != null && !createInvokerRequest.getCanonicalName().equals(invoker.getCanonicalName())) {
            invoker.setCanonicalName(createInvokerRequest.getCanonicalName());
            update = true;
        }
        if (createInvokerRequest.getDisplayName() != null && !createInvokerRequest.getDisplayName().equals(invoker.getName())) {
            invoker.setName(createInvokerRequest.getDisplayName());
            update = true;
        }
        if (createInvokerRequest.getDescription() != null && !createInvokerRequest.getDescription().equals(invoker.getDescription())) {
            invoker.setDescription(createInvokerRequest.getDescription());
            update = true;
        }
        if (!invoker.isSystemObject()) {
            invoker.setSystemObject(true);
            update = true;
        }
        return update;
    }


    public void validate(DynamicExecutionFilter dynamicExecutionFilter, SecurityContext securityContext) {
        dynamicExecutionService.validate(dynamicExecutionFilter, securityContext);

    }

    public void validate(ExecuteDynamicExecution executeDynamicExecution, SecurityContextBase securityContext) {
        dynamicExecutionService.validate(executeDynamicExecution, securityContext);
    }

    public ExecuteInvokersResponse executeDynamicExecution(ExecuteDynamicExecution executeDynamicExecution, SecurityContextBase securityContext) {
        return dynamicExecutionService.executeDynamicExecution(executeDynamicExecution, securityContext);
    }

    public PaginationResponse<DynamicExecution> getAllDynamicExecutions(DynamicExecutionFilter dynamicExecutionFilter, SecurityContext securityContext) {
        return new PaginationResponse<>(dynamicExecutionService.getAllDynamicExecutions(dynamicExecutionFilter, securityContext));
    }


    public FileResource exportDynamicExecutionResultToCSV(ExportDynamicExecution exportDynamicExecution, SecurityContext securityContext) {
        ExecuteInvokersResponse executeInvokersResponse = executeDynamicExecution(exportDynamicExecution, securityContext);
        Map<String, FieldProperties> fieldToName = exportDynamicExecution.getFieldToName();
        CSVFormat csvFormat = exportDynamicExecution.getCsvFormat();

        return invokerResponseToCSV(securityContext, executeInvokersResponse, fieldToName, csvFormat);


    }

    public FileResource exportDynamicInvokerToCSV(ExportDynamicInvoker exportDynamicExecution, SecurityContext securityContext) {
        ExecuteInvokersResponse executeInvokersResponse = executeInvoker(exportDynamicExecution, securityContext);
        Map<String, FieldProperties> fieldToName = exportDynamicExecution.getFieldProperties();
        CSVFormat csvFormat = exportDynamicExecution.getCsvFormat();

        return invokerResponseToCSV(securityContext, executeInvokersResponse, fieldToName, csvFormat);


    }

    private FileResource invokerResponseToCSV(SecurityContext securityContext, ExecuteInvokersResponse executeInvokersResponse, Map<String, FieldProperties> fieldToName, CSVFormat csvFormat) {
        File file = new File(com.flexicore.service.FileResourceService.generateNewPathForFileResource("dynamic-execution-csv", securityContext.getUser()) + ".csv");
        String[] headersArr = fieldToName.values().stream().sorted(Comparator.comparing(FieldProperties::getOrdinal)).map(FieldProperties::getName).toArray(String[]::new);

        CSVFormat format = csvFormat.withHeader(headersArr);
        Map<String, Method> fieldNameToMethod = new HashMap<>();
        if (CSVFormat.EXCEL.equals(format)) {
            try (Writer out = new OutputStreamWriter(new FileOutputStream(file, true))) {
                out.write(ByteOrderMark.UTF_BOM);

            } catch (Exception e) {
                logger.error("failed writing UTF-8 BOM", e);
            }


        }
        try (Writer out = new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8);
             CSVPrinter csvPrinter = new CSVPrinter(out, format)) {

            for (ExecuteInvokerResponse<?> respons : executeInvokersResponse.getResponses()) {
                Object response = respons.getResponse();
                if (response != null) {
                    Collection<?> collection = getCollection(response);
                    if (collection != null) {
                        List<List<Object>> expendedRecordSet = toExpendedRecordSet(fieldToName, fieldNameToMethod, collection);
                        csvPrinter.printRecords(expendedRecordSet);

                    }
                }

            }
            csvPrinter.flush();
        } catch (Exception e) {
            logger.error("unable to create csv");
        }
        FileResource fileResource = fileResourceService.createDontPersist(file.getAbsolutePath(), securityContext);
        fileResource.setKeepUntil(OffsetDateTime.now().plusMinutes(30));
        fileResourceService.merge(fileResource);
        return fileResource;
    }

    private Collection<?> getCollection(Object response) {
        if (response instanceof Collection) {
            return (Collection<?>) response;
        }
        if (response instanceof PaginationResponse) {
            return ((PaginationResponse<?>) response).getList();
        }
        if (response instanceof com.wizzdi.flexicore.security.response.PaginationResponse) {
            return ((com.wizzdi.flexicore.security.response.PaginationResponse<?>) response).getList();
        }
        return null;
    }

    public static List<List<Object>> toExpendedRecordSet(Map<String, FieldProperties> fieldToName, Map<String, Method> methodCache, Collection<?> collection){
        Set<String> failedMethodsCache = new HashSet<>();
        List<String> fieldsSorted = fieldToName.entrySet().stream().sorted(Comparator.comparing(f -> f.getValue().getOrdinal())).map(f -> f.getKey()).toList();
        return collection.parallelStream().map(f -> toExpendedRecord(methodCache, failedMethodsCache, fieldsSorted, f)).toList();


    }

    private static List<Object> toExpendedRecord(Map<String, Method> methodCache, Set<String> failedMethodsCache, List<String> fieldsSorted, Object collectionEntry)  {
        List<Object> recordAsList = new ArrayList<>();
        for (String fieldPath : fieldsSorted) {
            Object current = getValueFromFieldPath(methodCache, failedMethodsCache, collectionEntry, fieldPath);
            if (current == null) {
                recordAsList.add("");
                continue;
            }
            recordAsList.add(current);
        }
        return recordAsList;
    }


    private static Object getValueFromFieldPath(Map<String, Method> methodCache, Set<String> failedCache, Object currentCollectionItem, String fieldPath) {
        Class<?> collectionItemType = currentCollectionItem.getClass();
        String[] split = fieldPath.split("\\.");
        String fullPathSoFar = "";
        Object current = currentCollectionItem;
        Class<?> currentClass = collectionItemType;
        //for object A containing nested object B ... with fieldPath Z
        //fieldPath = b.c.....z
        for (String currentPathComponent : split) {
            fullPathSoFar += currentPathComponent;
            if (failedCache.contains(fullPathSoFar)) {
                //failed in the past
                current = null;
            }
            Method method = getGetter(methodCache, fullPathSoFar, currentClass, currentPathComponent);
            if (method == null) {
                failedCache.add(fullPathSoFar);
                current = null;
                break;
            }
            Object data = invokeMethodProtected(current, method);
            if (data == null) {
                current = null;
                break;
            }
            current = data;
            currentClass = current.getClass();
        }
        return current;
    }

    private static Object invokeMethodProtected(Object current, Method method){
        try {
            return method.invoke(current);
        }
        catch (Throwable e){
            logger.debug("failed invoking method "+method.getName() +" on object",e);
            return null;
        }
    }

    private static Method getGetter(Map<String, Method> fieldNameToMethod, String fullPathSoFar, Class<?> currentClass, String currentName) {
        Method method = fieldNameToMethod.get(fullPathSoFar);
        if (method == null) {
            try {
                method = currentClass.getMethod("get" + StringUtils.capitalize(currentName));
            } catch (Exception ignored) {
            }
            if (method == null) {
                try {
                    method = currentClass.getMethod("is" + StringUtils.capitalize(currentName));
                } catch (Exception ignored) {

                    return null;
                }
            }

            fieldNameToMethod.put(fullPathSoFar, method);
        }
        return method;
    }


    public void validateExportDynamicExecution(ExportDynamicExecution exportDynamicExecution, SecurityContext securityContext) {
        validate(exportDynamicExecution, securityContext);
        if (exportDynamicExecution.getFieldToName() == null || exportDynamicExecution.getFieldToName().isEmpty()) {
            throw new BadRequestException("Field to name map must be non null and not empty");
        }
        if (exportDynamicExecution.getCsvFormat() == null) {
            exportDynamicExecution.setCsvFormat(CSVFormat.EXCEL);
        }
    }

    public void validateExportDynamicInvoker(ExportDynamicInvoker exportDynamicInvoker, SecurityContext securityContext) {
        if (exportDynamicInvoker.getFieldProperties() == null || exportDynamicInvoker.getFieldProperties().isEmpty()) {
            throw new BadRequestException("fieldProperties map must be non null and not empty");
        }
        if (exportDynamicInvoker.getCsvFormat() == null) {
            exportDynamicInvoker.setCsvFormat(CSVFormat.EXCEL);
        }
    }


    public void validate(DynamicExecutionExampleRequest dynamicExecutionExampleRequest, SecurityContext securityContext) {
        dynamicExecutionService.validate(dynamicExecutionExampleRequest, securityContext);

    }
}
