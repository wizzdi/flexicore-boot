package com.flexicore.response;

import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.Syncable;

public class ExecuteInvokerResponseSyncable extends ExecuteInvokerResponse<PaginationResponse<? extends Syncable>> {

    public ExecuteInvokerResponseSyncable(String invokerName, boolean executed, PaginationResponse<? extends Syncable> response) {
        super(invokerName, executed, response);
    }
}
