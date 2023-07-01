package com.flexicore.data.jsoncontainers;

import com.flexicore.annotations.IOperation;

/**
 * Created by Asaf on 26/07/2016.
 */
public class OperationInfo {

    private IOperation iOperation;
    private String operationId;


    public OperationInfo(IOperation iOperation, String operationId) {
        this.iOperation = iOperation;
        this.operationId = operationId;
    }

    public OperationInfo() {
    }

    public IOperation getiOperation() {
        return iOperation;
    }

    public void setiOperation(IOperation iOperation) {
        this.iOperation = iOperation;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }
}
