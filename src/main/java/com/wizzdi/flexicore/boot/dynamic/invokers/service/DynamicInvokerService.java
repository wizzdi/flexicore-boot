package com.wizzdi.flexicore.boot.dynamic.invokers.service;

import com.flexicore.model.SecurityOperation;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.boot.dynamic.invokers.interfaces.ExecutionContext;
import com.wizzdi.flexicore.boot.dynamic.invokers.request.*;
import com.wizzdi.flexicore.boot.dynamic.invokers.response.ExceptionHolder;
import com.wizzdi.flexicore.boot.dynamic.invokers.response.InvokerHolder;
import com.wizzdi.flexicore.boot.dynamic.invokers.response.InvokerInfo;
import com.wizzdi.flexicore.boot.dynamic.invokers.response.InvokerMethodHolder;
import com.wizzdi.flexicore.security.interfaces.OperationsMethodScanner;
import com.wizzdi.flexicore.security.request.BasicPropertiesFilter;
import com.wizzdi.flexicore.security.request.PaginationFilter;
import com.wizzdi.flexicore.security.response.OperationScanContext;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.OperationValidatorService;
import com.wizzdi.flexicore.security.service.SecurityOperationService;
import org.pf4j.Extension;
import org.pf4j.PluginManager;
import org.pf4j.PluginWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.web.client.HttpClientErrorException;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Extension
@Service
public class DynamicInvokerService implements Plugin {

    private static final Logger logger = LoggerFactory.getLogger(DynamicInvokerService.class);

    @Autowired
    @Lazy
    private List<InvokerInfo> invokerInfos;

    @Autowired
    @Lazy
    private PluginManager pluginManager;
    @Autowired
    private OperationValidatorService securityService;
    @Autowired
    private OperationsMethodScanner operationsMethodScanner;
    @Autowired
    private SecurityOperationService securityOperationService;


    public void validate(DynamicInvokerFilter dynamicInvokerFilter, SecurityContextBase securityContext) {


    }

    public void validate(DynamicInvokerMethodFilter dynamicInvokerMethodFilter, SecurityContextBase securityContext) {
        if(dynamicInvokerMethodFilter.getDynamicInvokerFilter()!=null){
            validate(dynamicInvokerMethodFilter.getDynamicInvokerFilter(),securityContext);
        }
    }

    public PaginationResponse<InvokerInfo> getAllDynamicInvokers(DynamicInvokerFilter dynamicInvokerFilter, SecurityContextBase securityContext) {
        List<InvokerInfo> list = listAllDynamicInvokers(dynamicInvokerFilter, securityContext);
        long count = countAllDynamicInvokers(dynamicInvokerFilter, securityContext);
        return new PaginationResponse<>(list, dynamicInvokerFilter.getPageSize(), count);
    }

    private long countAllDynamicInvokers(DynamicInvokerFilter dynamicInvokerFilter, SecurityContextBase securityContextBase) {
        return invokerInfos.stream().filter(f -> filter(f, dynamicInvokerFilter)).count();
    }

    public List<InvokerInfo> listAllDynamicInvokers(DynamicInvokerFilter dynamicInvokerFilter, SecurityContextBase securityContextBase) {
        return paginate(invokerInfos.stream().filter(f -> filter(f, dynamicInvokerFilter)), dynamicInvokerFilter).collect(Collectors.toList());
    }

    private <T> Stream<T> paginate(Stream<T> stream, PaginationFilter paginationFilter) {
        if (paginationFilter.getCurrentPage() != null && paginationFilter.getCurrentPage() > -1 && paginationFilter.getPageSize() != null && paginationFilter.getPageSize() > 0) {
            return stream.skip((long) paginationFilter.getPageSize() * paginationFilter.getCurrentPage()).limit(paginationFilter.getPageSize());
        }
        return stream;
    }

    private boolean filter(InvokerInfo f, DynamicInvokerFilter dynamicInvokerFilter) {
        boolean pred = true;

        BasicPropertiesFilter basicPropertiesFilter = dynamicInvokerFilter.getBasicPropertiesFilter();
        if (basicPropertiesFilter !=null&& basicPropertiesFilter.getNameLike() != null) {
            pred = pred && (f.getDisplayName().contains(basicPropertiesFilter.getNameLike()) || f.getDescription().contains(basicPropertiesFilter.getNameLike()));
        }
        if (dynamicInvokerFilter.getMethodNameLike() != null) {
            pred = pred && f.getMethods().stream().map(e -> e.getName()).anyMatch(e -> e.contains(dynamicInvokerFilter.getMethodNameLike()));
        }
        if (dynamicInvokerFilter.getInvokerTypes() != null && !dynamicInvokerFilter.getInvokerTypes().isEmpty()) {
            pred = pred && dynamicInvokerFilter.getInvokerTypes().contains(f.getName().getCanonicalName());
        }
        if(dynamicInvokerFilter.getPluginIds()!=null&&!dynamicInvokerFilter.getPluginIds().isEmpty()){
            Class<?> handlingType = f.getName();
            PluginWrapper pluginWrapper = pluginManager.whichPlugin(handlingType);
            pred = pred && pluginWrapper!=null&&dynamicInvokerFilter.getPluginIds().contains(pluginWrapper.getPluginId());

        }
        if(dynamicInvokerFilter.getHandlingTypes()!=null&&!dynamicInvokerFilter.getHandlingTypes().isEmpty()){
            pred=pred&&dynamicInvokerFilter.getHandlingTypes().contains(f.getHandlingType().getCanonicalName());
        }


        return pred;
    }

    public ExecuteInvokersResponse executeInvoker(ExecuteInvokerRequest executeInvokerRequest, SecurityContextBase securityContext) {
        List<? extends Plugin> invokers = getInvokers(executeInvokerRequest.getInvokerNames());

        List<ExecuteInvokerResponse<?>> responses = new ArrayList<>();
        Object executionParametersHolder = executeInvokerRequest.getExecutionParametersHolder();
        ExecutionContext executionContext = executeInvokerRequest.getExecutionContext();

        for (Object invoker : invokers) {
            Class<?> clazz = ClassUtils.getUserClass(invoker.getClass());
            String invokerName = clazz.getCanonicalName();
            try {


                Method[] methods = clazz.getDeclaredMethods();
                for (Method method : methods) {
                    if (method.isBridge()) {
                        continue;
                    }
                    Class<?>[] parameterTypes = method.getParameterTypes();

                    if (method.getName().equals(executeInvokerRequest.getInvokerMethodName())) {
                        int bodyIndex = getParameterIndex(parameterTypes, executeInvokerRequest.getExecutionParametersHolder().getClass());
                        if (bodyIndex > -1) {
                            OperationScanContext operationScanContext = operationsMethodScanner.scanOperationOnMethod(method);
                            SecurityOperation securityOperation = operationScanContext != null ? securityOperationService.getByIdOrNull(operationScanContext.getSecurityOperationCreate().getIdForCreate(), SecurityOperation.class, null) : null;
                            SecurityOperation original = securityContext.getOperation();
                            try {
                                if (securityOperation != null) {
                                    securityContext.setOperation(securityOperation);
                                }
                                if (!securityService.checkIfAllowed(securityContext)) {
                                    throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "not allow to operation " + securityContext.getOperation());
                                }
                                Object[] parameters = new Object[parameterTypes.length];
                                parameters[bodyIndex] = executionParametersHolder;
                                for (int i = 0; i < parameterTypes.length; i++) {
                                    Class<?> parameterType = parameterTypes[i];
                                    if (SecurityContextBase.class.isAssignableFrom(parameterType)) {
                                        parameters[i] = securityContext;
                                    }
                                    if (executionContext != null && parameterType.isAssignableFrom(executionContext.getClass())) {
                                        parameters[i] = executionContext;
                                    }
                                }
                                Object ret = AopUtils.isCglibProxy(invoker.getClass()) ? AopUtils.invokeJoinpointUsingReflection(invoker, method, parameters) : method.invoke(invoker, parameters);
                                ExecuteInvokerResponse<?> e = new ExecuteInvokerResponse<>(invokerName, true, ret);
                                responses.add(e);
                                break;
                            } finally {
                                securityContext.setOperation(original);
                            }
                        }


                    }
                }
            } catch (Throwable e) {
                logger.error("failed executing " + invokerName, e);
                responses.add(new ExecuteInvokerResponse<>(invokerName, false, new ExceptionHolder(e)));
            }

        }


        return new ExecuteInvokersResponse(responses);


    }

    private int getParameterIndex(Class<?>[] parameterTypes, Class<?> aClass) {
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            if (parameterType.isAssignableFrom(aClass)) {
                return i;
            }
        }
        return -1;
    }

    private List<Plugin> getInvokers(Set<String> invokerNames) {
        Map<String, Plugin> allPlugins = pluginManager.getExtensions(Plugin.class).stream().filter(Objects::nonNull).collect(Collectors.toMap(f -> ClassUtils.getUserClass(f.getClass()).getName(), f -> f, (a, b) -> a));
        return invokerNames.stream().map(f -> getInvoker(allPlugins, f)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private Plugin getInvoker(Map<String, Plugin> allPlugins, String invokerClassName) {
        Plugin invoker = allPlugins.get(invokerClassName);
        if (invoker == null) {
            logger.warn("invoker " + invokerClassName + " could not be found");
        }
        return invoker;
    }


    public PaginationResponse<InvokerHolder> getAllDynamicInvokerHolders(DynamicInvokerFilter dynamicInvokerFilter, SecurityContextBase securityContext) {
        PaginationResponse<InvokerInfo> paginationResponse = getAllDynamicInvokers(dynamicInvokerFilter, securityContext);
        List<InvokerHolder> invokerHolders = paginationResponse.getList().stream().map(f -> new InvokerHolder(f)).collect(Collectors.toList());
        return new PaginationResponse<>(invokerHolders, dynamicInvokerFilter, paginationResponse.getTotalRecords());
    }

    public PaginationResponse<InvokerMethodHolder> getAllInvokerMethodHolders(DynamicInvokerMethodFilter dynamicInvokerMethodFilter, SecurityContextBase securityContext) {
        List<InvokerMethodHolder> invokerMethodHolders = listAllInvokerMethodHolders(dynamicInvokerMethodFilter);
        long count = countAllInvokerMethodHolders(dynamicInvokerMethodFilter);
        return new PaginationResponse<>(invokerMethodHolders, dynamicInvokerMethodFilter, count);
    }

    private List<InvokerMethodHolder> listAllInvokerMethodHolders(DynamicInvokerMethodFilter dynamicInvokerMethodFilter) {
        Stream<InvokerInfo> stream = invokerInfos.stream();
        if(dynamicInvokerMethodFilter.getDynamicInvokerFilter()!=null){
            stream=stream.filter(f->filter(f,dynamicInvokerMethodFilter.getDynamicInvokerFilter()));
        }
        return paginate(stream.map(f -> getMethodHolders(f)).filter(f -> f != null).flatMap(List::stream).filter(f -> filterMethods(f, dynamicInvokerMethodFilter)).sorted(Comparator.comparing(f->f.getName())), dynamicInvokerMethodFilter).collect(Collectors.toList());
    }

    private List<InvokerMethodHolder> getMethodHolders(InvokerInfo invokerInfo) {

        return invokerInfo.getMethods().stream().map(f->new InvokerMethodHolder(invokerInfo.getName().getCanonicalName(),invokerInfo.getPluginId(),f)).collect(Collectors.toList());
    }

    private long countAllInvokerMethodHolders(DynamicInvokerMethodFilter dynamicInvokerMethodFilter) {
        Stream<InvokerInfo> stream = invokerInfos.stream();
        if(dynamicInvokerMethodFilter.getDynamicInvokerFilter()!=null){
            stream=stream.filter(f->filter(f,dynamicInvokerMethodFilter.getDynamicInvokerFilter()));
        }
        return stream.map(f ->  getMethodHolders(f)).filter(f -> f != null).flatMap(List::stream).filter(f -> filterMethods(f, dynamicInvokerMethodFilter)).count();
    }

    private boolean filterMethods(InvokerMethodHolder invokerMethodHolder, DynamicInvokerMethodFilter dynamicInvokerMethodFilter) {
        boolean pred = true;
        BasicPropertiesFilter basicPropertiesFilter = dynamicInvokerMethodFilter.getBasicPropertiesFilter();
        if (basicPropertiesFilter !=null&& basicPropertiesFilter.getNameLike() != null) {
            pred = pred && (invokerMethodHolder.getDisplayName().contains(basicPropertiesFilter.getNameLike()) || invokerMethodHolder.getDescription().contains(basicPropertiesFilter.getNameLike()));
        }
        boolean byCategories = dynamicInvokerMethodFilter.getCategories() != null && !dynamicInvokerMethodFilter.getCategories().isEmpty();
        if (byCategories ||dynamicInvokerMethodFilter.isEmptyCategories()) {
            pred = pred && invokerMethodHolder.getCategories() != null &&
                    ((dynamicInvokerMethodFilter.isEmptyCategories()&&invokerMethodHolder.getCategories().isEmpty())
                            || (byCategories&&invokerMethodHolder.getCategories().stream().anyMatch(e->dynamicInvokerMethodFilter.getCategories().contains(e))));
        }


        return pred;
    }
}
