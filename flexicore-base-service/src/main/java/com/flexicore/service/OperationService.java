package com.flexicore.service;

import com.flexicore.annotations.IOperation;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.FlexiCoreService;
import com.flexicore.model.*;
import com.flexicore.request.CreateOperationRequest;
import com.flexicore.request.OperationCreate;
import com.flexicore.request.OperationFiltering;
import com.flexicore.request.OperationUpdate;
import com.flexicore.security.SecurityContext;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface OperationService extends FlexiCoreService {
    String USER_TYPE = "USER";
    String ROLE_TYPE = "ROLE";
    String TENANT_TYPE = "TENANT";

    static String getAccessControlKey(String type, String opId, String securityEntityId, IOperation.Access access){
        return type+"."+opId+"."+securityEntityId+"."+access.name();
    }

    IOperation getIOperationFromApiOperation(io.swagger.v3.oas.annotations.Operation apiOperation, Method method);

    Operation findById(String id);

    <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, List<String> batchString, SecurityContext securityContext);

    @Transactional
    void merge(Object o);

    boolean tennantAllowed(Operation operation, Tenant tenant);

    boolean tennantDenied(Operation operation, Tenant tenant);

    List<Operation> findAllOrderedByName(QueryInformationHolder<Operation> queryInformationHolder);

    boolean userAllowed(Operation operation, User user);

    boolean userDenied(Operation operation, User user);

    boolean roleAllowed(Operation operation, User user);

    boolean roleDenied(Operation operation, User user);

    boolean checkUser(Operation operation, User user, IOperation.Access access);

    <T extends Baseclass> List<T> getAllFiltered(QueryInformationHolder<T> queryInformationHolder);

    void updateCahce(Operation operation);

    void refrehEntityManager();

    @Transactional
    void massMerge(List<?> toMerge);

    @Transactional
    Operation createOperation(CreateOperationRequest createOperationRequest);

    Operation createOperationNoMerge(CreateOperationRequest createOperationRequest);

    boolean updateOperationNoMerge(CreateOperationRequest updateOperationRequest, Operation operation);

    void handleOperationRelatedClassesNoMerge(Operation operation, Class<? extends Baseclass>[] related, Map<String, OperationToClazz> existingMap, List<Object> toMerge);

    List<OperationToClazz> getRelatedClasses(Set<String> operationIds);
    PaginationResponse<Operation> getAllOperations(OperationFiltering operationFiltering, SecurityContext securityContext);
    List<Operation> listAllOperations(OperationFiltering operationFiltering, SecurityContext securityContext);
    Operation createOperationNoMerge(OperationCreate operationCreate, SecurityContext securityContext);
    Operation createOperation(OperationCreate operationCreate, SecurityContext securityContext);
    Operation updateOperation(OperationUpdate operationUpdate, SecurityContext securityContext);
    boolean updateOperationNoMerge(OperationCreate operationCreate, Operation operation);
}
