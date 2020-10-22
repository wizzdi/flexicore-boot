package com.flexicore.service;

import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.FlexiCoreService;
import com.flexicore.model.Baseclass;
import com.flexicore.model.FileResource;
import com.flexicore.model.dynamic.DynamicExecution;
import com.flexicore.model.dynamic.DynamicInvoker;
import com.flexicore.model.dynamic.ExecutionContext;
import com.flexicore.request.*;
import com.flexicore.response.ExecuteInvokersResponse;
import com.flexicore.response.InvokerInfo;
import com.flexicore.security.SecurityContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface DynamicInvokersService extends FlexiCoreService {
    <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, List<String> batchString, SecurityContext securityContext);

    <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext);

    /**
     * returns list of invoker info object , describing the available dynamic invokers their methods and parameters
     * @param invokersFilter object used to filter the result set
     * @param securityContext security context of the user to execute the action
     * @return
     */
    PaginationResponse<InvokerInfo> getAllInvokersInfo(InvokersFilter invokersFilter, SecurityContext securityContext);

    /**
     * returns a list of dynamic invoker
     * @param invokersFilter object used to filter the result set
     * @param securityContext security context of the user to execute the action
     * @return list of dynamic invokers
     */
    PaginationResponse<DynamicInvoker> getAllInvokers(InvokersFilter invokersFilter, SecurityContext securityContext);

    /**
     * executes a dynamic invoker
     * @param executeInvokerRequest object containing the required info for executing the invoker(s)
     * @param securityContext security context of the user to execute the action
     * @return object containing the response for the invoker(s)
     */
    ExecuteInvokersResponse executeInvoker(ExecuteInvokerRequest executeInvokerRequest, SecurityContext securityContext);

    @Transactional
    void massMerge(List<?> toMerge);

    /**
     * transforms a dynamic execution to #ExecuteInvokerRequest
     * @param dynamicExecution dynamic execution to execute
     * @param securityContext security context of the user to execute the action
     * @return object used to execute the invoker using {@link #executeInvoker(ExecuteInvokerRequest, SecurityContext)}
     */
    ExecuteInvokerRequest getExecuteInvokerRequest(DynamicExecution dynamicExecution, SecurityContext securityContext);

    /**
     * transforms a dynamic execution to #ExecuteInvokerRequest
     * @param dynamicExecution dynamic execution to execute
     * @param executionContext object used to describe the context of execution
     * @param securityContext security context of the user to execute the action
     * @return object used to execute the invoker using {@link #executeInvoker(ExecuteInvokerRequest, SecurityContext)}
     */
    ExecuteInvokerRequest getExecuteInvokerRequest(DynamicExecution dynamicExecution, ExecutionContext executionContext, SecurityContext securityContext);

    /**
     * creates dynamic execution
     * @param createInvokerRequest object used to create invoker
     * @param toMerge list to fill with object to merge
     * @param securityContext security context of the user to execute the action
     * @return dynamic execution
     */
    DynamicExecution createDynamicExecutionNoMerge(CreateDynamicExecution createInvokerRequest, List<Object> toMerge, SecurityContext securityContext);

    /**
     * creates dynamic execution
     * @param createInvokerRequest object used to create dynamic execution
     * @param securityContext security context of the user to execute the action
     * @return dynamic execution
     */
    DynamicExecution createDynamicExecution(CreateDynamicExecution createInvokerRequest, SecurityContext securityContext);
    /**
     * updates dynamic execution
     * @param updateDynamicExecution object used to update dynamic execution
     * @param securityContext security context of the user to execute the action
     * @return updated dynamic execution
     */
    DynamicExecution updateDynamicExecution(UpdateDynamicExecution updateDynamicExecution, SecurityContext securityContext);

    /**
     * updates dynamic execution
     * @param createDynamicExecution object used to update the dynamic execution
     * @param dynamicExecution security context of the user to execute the action
     * @return true if updated false otherwise
     */
    boolean updateDynamicExecutionNoMerge(CreateDynamicExecution createDynamicExecution, DynamicExecution dynamicExecution, List<Object> toMerge);


    void validate(DynamicExecutionFilter dynamicExecutionFilter, SecurityContext securityContext);

    void validate(ExecuteDynamicExecution executeDynamicExecution, SecurityContext securityContext);

    PaginationResponse<DynamicExecution> getAllDynamicExecutions(DynamicExecutionFilter dynamicExecutionFilter, SecurityContext securityContext);

    /**
     * executes dynamic execution
     * @param executeDynamicExecution object used to execute the dynamic execution
     * @param securityContext security context of the user to execute the action
     * @return execution response
     */
    ExecuteInvokersResponse executeInvoker(ExecuteDynamicExecution executeDynamicExecution, SecurityContext securityContext);

    /**
     * exports dynamic execution result to CSV
     * @param exportDynamicExecution object used to export the dynamic execution
     * @param securityContext security context of the user to execute the action
     * @return file resource containing CSV
     */
    FileResource exportDynamicExecutionResultToCSV(ExportDynamicExecution exportDynamicExecution, SecurityContext securityContext);

    void validateExportDynamicExecution(ExportDynamicExecution exportDynamicExecution, SecurityContext securityContext);

    void validate(DynamicExecutionExampleRequest dynamicExecutionExampleRequest, SecurityContext securityContext);
}
