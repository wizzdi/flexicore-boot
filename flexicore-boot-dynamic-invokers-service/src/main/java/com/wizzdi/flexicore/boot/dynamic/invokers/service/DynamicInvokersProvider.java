package com.wizzdi.flexicore.boot.dynamic.invokers.service;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.init.FlexiCorePluginManager;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.boot.dynamic.invokers.annotations.FieldInfo;
import com.wizzdi.flexicore.boot.dynamic.invokers.annotations.IdRefFieldInfo;
import com.wizzdi.flexicore.boot.dynamic.invokers.annotations.Invoker;
import com.wizzdi.flexicore.boot.dynamic.invokers.annotations.ListFieldInfo;
import com.wizzdi.flexicore.boot.dynamic.invokers.interfaces.InvokerMethodScanner;
import com.wizzdi.flexicore.boot.dynamic.invokers.interfaces.InvokerParameterScanner;
import com.wizzdi.flexicore.boot.dynamic.invokers.request.ScanFieldRequest;
import com.wizzdi.flexicore.boot.dynamic.invokers.response.InvokerInfo;
import com.wizzdi.flexicore.boot.dynamic.invokers.response.InvokerMethodInfo;
import com.wizzdi.flexicore.boot.dynamic.invokers.response.ParameterInfo;
import org.pf4j.Extension;
import org.pf4j.PluginWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

import static com.wizzdi.flexicore.boot.dynamic.invokers.utils.InvokerUtils.*;

@Configuration
@Extension
public class DynamicInvokersProvider implements Plugin {

    private static final Logger logger = LoggerFactory.getLogger(DynamicInvokersProvider.class);
    private static final Set<Class<?>> KNOWN_SYSTEM_TYPES = new HashSet<>(Arrays.asList(
            Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Void.class, String.class, OffsetDateTime.class, ZonedDateTime.class, LocalDateTime.class, LocalDate.class, LocalTime.class,OffsetTime.class,Class.class
    ));


    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    @ConditionalOnMissingBean(InvokerParameterScanner.class)
    public InvokerParameterScanner invokerParameterScanner() {
        return (type, brotherFields, handledField) -> scanField(new ScanFieldRequest(type, brotherFields, handledField));
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    @ConditionalOnMissingBean(InvokerMethodScanner.class)
    public InvokerMethodScanner invokerMethodScanner() {
        return this::scanMethod;
    }


    @Lazy
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public List<InvokerInfo> invokerInfos(FlexiCorePluginManager flexiCorePluginManager, InvokerMethodScanner invokerMethodScanner, InvokerParameterScanner invokerParameterScanner) {
        List<ApplicationContext> collect = flexiCorePluginManager.getStartedPlugins().stream().map(f -> flexiCorePluginManager.getApplicationContext(f)).collect(Collectors.toList());
        collect.add(flexiCorePluginManager.getApplicationContext());
        return collect.stream().map(f -> getInvokers(f)).flatMap(List::stream).map(f -> scan(flexiCorePluginManager,invokerMethodScanner, invokerParameterScanner, f)).collect(Collectors.toList());
    }

    private InvokerInfo scan(FlexiCorePluginManager flexiCorePluginManager, InvokerMethodScanner invokerMethodScanner, InvokerParameterScanner invokerParameterScanner, Object invoker) {
        PluginWrapper pluginWrapper = flexiCorePluginManager.whichPlugin(invoker.getClass());
        InvokerInfo invokerInfo = new InvokerInfo(invoker,pluginWrapper);
        Class<?> invokerClass = invokerInfo.getName();
        Method[] methods = invokerClass.getDeclaredMethods();
        for (Method method : methods) {
            InvokerMethodInfo invokerMethodInfo = invokerMethodScanner.scan(invokerClass, method);
            if (invokerMethodInfo != null) {
                invokerInfo.addInvokerMethodInfo(invokerMethodInfo);
                Optional<Parameter> parameterOptional = getBodyParameter(method);
                if (parameterOptional.isPresent()) {
                    Parameter bodyParameter = parameterOptional.get();
                    Class<?> bodyParameterType = bodyParameter.getType();
                    String parameterHolderType = bodyParameterType.getCanonicalName();
                    invokerMethodInfo.setParameterHolderType(parameterHolderType);
                    List<Field> allFields = getAllFields(bodyParameterType);
                    for (Field field : allFields) {
                        ParameterInfo parameterInfo = invokerParameterScanner.scan(bodyParameter.getType(), allFields, field);
                        if (parameterInfo != null) {
                            invokerMethodInfo.addParameterInfo(parameterInfo);
                        }
                    }
                }
            }
        }
        return invokerInfo;
    }

    private Optional<Parameter> getBodyParameter(Method method) {
        Parameter[] parameters = method.getParameters();
        return Arrays.stream(parameters).filter(this::isBodyParameter).findFirst();

    }

    private boolean isBodyParameter(Parameter parameter) {
        return ((!parameter.getType().equals(String.class) && !SecurityContextBase.class.isAssignableFrom(parameter.getType())) || AnnotatedElementUtils.findMergedAnnotation(parameter, RequestBody.class) != null);
    }

    private ParameterInfo scanField(ScanFieldRequest scanFieldRequest) {
        ParameterInfo parameterInfo = scanFieldRequest.getParameterInfoCache().get(scanFieldRequest.getHandledField());
        if(parameterInfo!=null){
            logger.debug("Cache hit "+scanFieldRequest.getHandledField());
            return parameterInfo;
        }
        IdRefFieldInfo idRefFieldInfo = AnnotatedElementUtils.findMergedAnnotation(scanFieldRequest.getHandledField(), IdRefFieldInfo.class);
        if (idRefFieldInfo != null) {
            parameterInfo = new ParameterInfo(scanFieldRequest.getHandledField(), idRefFieldInfo);
        } else {
            FieldInfo fieldInfo = AnnotatedElementUtils.findMergedAnnotation(scanFieldRequest.getHandledField(), FieldInfo.class);
            if (fieldInfo != null) {
                parameterInfo = new ParameterInfo(scanFieldRequest.getHandledField(), fieldInfo);
            } else {
                ListFieldInfo listFieldInfo = AnnotatedElementUtils.findMergedAnnotation(scanFieldRequest.getHandledField(), ListFieldInfo.class);
                if (listFieldInfo != null) {
                    parameterInfo = new ParameterInfo(scanFieldRequest.getHandledField(), listFieldInfo);
                } else {

                    parameterInfo = detectAutomatically(scanFieldRequest.getHandledField(), scanFieldRequest.getBrotherFields(), scanFieldRequest.getType().getDeclaredFields());
                }

            }
        }
        if (parameterInfo != null) {
            scanFieldRequest.getParameterInfoCache().put(scanFieldRequest.getHandledField(), parameterInfo);
            if (!parameterInfo.isIdRef() && parameterInfo.getIterationType() != null && !parameterInfo.isIgnoreSubParameters()) {
                List<ParameterInfo> innerSubParameters = getInnerSubParameters(scanFieldRequest.getParameterInfoCache(), parameterInfo.getIterationType());
                parameterInfo.setSubParameters(innerSubParameters);
            }
        }

        return parameterInfo;
    }


    private List<ParameterInfo> getInnerSubParameters(Map<Field, ParameterInfo> cache, Class<?> type) {
        List<ParameterInfo> subParameters = new ArrayList<>();
        if (!type.isPrimitive() && !KNOWN_SYSTEM_TYPES.contains(type)) {
            List<Field> brotherFields = getAllFields(type);
            for (Field field : brotherFields) {
                ParameterInfo fieldParameterInfo = scanField(new ScanFieldRequest(cache, type, brotherFields, field));

                if (fieldParameterInfo != null) {
                    subParameters.add(fieldParameterInfo);
                }

            }
        }
        return subParameters;
    }


    private InvokerMethodInfo scanMethod(Class<?> invokerClass, Method method) {
        com.wizzdi.flexicore.boot.dynamic.invokers.annotations.InvokerMethodInfo InvokerMethodInfo = AnnotatedElementUtils.findMergedAnnotation(method, com.wizzdi.flexicore.boot.dynamic.invokers.annotations.InvokerMethodInfo.class);
        if (InvokerMethodInfo != null) {
            return new InvokerMethodInfo(method, InvokerMethodInfo);
        } else {
            RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
            IOperation iOperation = AnnotatedElementUtils.findMergedAnnotation(method, IOperation.class);

            if (requestMapping != null || iOperation != null) {
                if (iOperation == null) {
                    iOperation = getIOOperation(method);
                }
                com.wizzdi.flexicore.boot.dynamic.invokers.annotations.InvokerMethodInfo invokerMethodInfo = getInvokerMethodInfo(invokerClass, method, iOperation);
                return new InvokerMethodInfo(method, invokerMethodInfo);
            }

        }
        return null;
    }

    private List<Object> getInvokers(ApplicationContext f) {
        Map<String, Object> restControllers = f.getBeansWithAnnotation(RestController.class);
        Map<String, Object> ops = f.getBeansWithAnnotation(OperationsInside.class);
        restControllers.putAll(ops);
        Map<String, Invoker> invokers = f.getBeansOfType(Invoker.class);

        restControllers.putAll(invokers);

        return new ArrayList<>(restControllers.values());
    }


}
