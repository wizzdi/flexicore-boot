package com.wizzdi.flexicore.security.migrations;

import com.flexicore.model.SecurityOperation;
import com.wizzdi.flexicore.security.configuration.SecurityContext;
import com.wizzdi.flexicore.security.response.Operations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;

public class OperationV7Migration {
    private static final Logger logger= LoggerFactory.getLogger(OperationV7Migration.class);

    public static void migrateOperations(Statement select,Operations operations) throws SQLException {
        List<OperationHolder> toMigrate= operations.getOperations().stream().map(f->OperationHolder.ofOp(f)).filter(f->f.requiresMigration()).toList();
        for (OperationHolder operationHolder : toMigrate) {
            migrateSecurityLinks(select,operationHolder);
            migrateOperationGroupLinks(select,operationHolder);
        }

    }

    private static void migrateOperationGroupLinks(Statement select, OperationHolder operationHolder) throws SQLException {

        String sql="update operationtogroup set operationId='%s' where operationId='%s'";
        sql=sql.formatted(operationHolder.securityOperation().id(),operationHolder.oldId());
        executeUpdate(select, sql);
    }

    private static void migrateSecurityLinks(Statement select, OperationHolder operationHolder) throws SQLException {
        String sql="update securityLink set operationId='%s' where operationId='%s'";
        sql=sql.formatted(operationHolder.securityOperation().id(),operationHolder.oldId());
        executeUpdate(select, sql);
    }

    private static int executeUpdate(Statement select, String sql) throws SQLException {
        logger.info("executing sql: {}", sql);
        int update = select.executeUpdate(sql);
        logger.info("updated {} entries",update);
        return update;
    }

    record OperationHolder(SecurityOperation securityOperation,String oldId){

        public static OperationHolder ofOp(SecurityOperation securityOperation) {
            return new OperationHolder(securityOperation,getOldOperationId(securityOperation));
        }
        public boolean requiresMigration(){
            return !securityOperation.id().equals(oldId);
        }
    }


    public static String getOldOperationId(SecurityOperation securityOperation) {
        if(securityOperation.clazz()!=null){
            return getOldOperationId(securityOperation.clazz().getCanonicalName());
        }
        if(securityOperation.method()!=null){
            return getOldOperationId(securityOperation.method().toString());
        }
        throw new IllegalArgumentException("operation has not clazz or method");
    }
    public static String getOldOperationId(String input) {
        input=input.replace(SecurityContext.class.getCanonicalName(),"com.flexicore.security.SecurityContextBase");

        return UUID.nameUUIDFromBytes(input.getBytes()).toString()
                .replaceAll("-", "")
                .substring(0, 22);

    }
}
