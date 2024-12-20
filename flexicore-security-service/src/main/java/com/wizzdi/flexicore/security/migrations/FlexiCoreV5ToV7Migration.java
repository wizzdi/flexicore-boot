package com.wizzdi.flexicore.security.migrations;

import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class FlexiCoreV5ToV7Migration {

    private static final Logger logger = LoggerFactory.getLogger(FlexiCoreV5ToV7Migration.class);


    public static void migrateToFCV7(Statement select) throws SQLException {
         List<Reference> tableNamesWithSecurityReference = getTableNamesWithSecurityReference(select);
        List<Reference> securityReference=tableNamesWithSecurityReference.stream().filter(f->f.columnName().toLowerCase().equals("security_id")).toList();
        addColumns(securityReference,select);
        flattenSecurity(securityReference,select);
        dropConstraints(tableNamesWithSecurityReference,select);
        dropUnnecessaryColumns(tableNamesWithSecurityReference,select);

        alterSecurityLink(select);
        updateSecurityLink(select);
        dropSecurityLinkColumns(select);

        dropOperationToClazz(select);

        alterOperationToGroup(select);
        updateOperationToGroup(select);
        dropOperationToGroupColumns(select);

        alterPermissionGroupToBaseclass(select);
        updatePermissionGroupToBaseclass(select);
        dropPermissionGroupToBaseclassColumns(select);
        updateRole(select);

        dropBaseclass(select);
    }

    private static void updateRole(Statement select) throws SQLException {
        {
            String sql = "update role set superAdmin=false where superAdmin is null;";
            executeSQL(select, sql);
        }

        {
            String sql = "update role set superAdmin=true where id='HzFnw-nVR0Olq6WBvwKcQg';";
            executeSQL(select, sql);
        }
    }

    private static void dropOperationToClazz(Statement select) throws SQLException {
        String sql="drop table if exists operationToClazz";
        executeSQL(select,sql);
    }

    private static void dropSecurityLinkColumns(Statement select) throws SQLException {
        String sql="alter table securitylink drop column if exists baseclass_id , drop column if exists clazz_id, drop column if exists operation_id";
        executeSQL(select,sql);
    }

    private static void updateSecurityLink(Statement select) throws SQLException {

        {
            String sql = """
            update securityLink set securedId=baseclass_id where securedId is null and not baseclass_id is
            null;""";
            executeSQL(select,sql);
        }

        {
            String sql = """
            update securityLink set securedType=(string_to_array(clazz.name, '.'))[array_length(string_to_array(clazz.name, '.'), 1)] from clazz where securedType is null and clazz_id=clazz.id;""";
            executeSQL(select,sql);
        }

        {
            String sql = """
            update securityLink set operationId=operation_id where operationId is null and not operation_id is null;""";
            executeSQL(select,sql);
        }
    }

    private static void alterSecurityLink(Statement select) throws SQLException {
        String sql= """
                alter table securitylink
                add column if not exists securedId varchar(255),
                add column if not exists securedType varchar(255),
                add column if not exists operationId varchar(255),
                drop constraint if exists baseclass_id,
                drop constraint if exists clazz_id,
                drop constraint if exists operation_id;""";
        executeSQL(select,sql);
    }

    private static void dropPermissionGroupToBaseclassColumns(Statement select) throws SQLException {
        String sql="alter table permissiongrouptobaseclass drop column if exists baseclass_id";
        executeSQL(select,sql);
    }

    private static void updatePermissionGroupToBaseclass(Statement select) throws SQLException {

        {
            String sql = """
            update permissiongrouptobaseclass set securedId=baseclass_id where securedId is null and not baseclass_id is
            null;""";
            executeSQL(select,sql);
        }

        {
            String sql = """
            update permissiongrouptobaseclass set securedType=(string_to_array(clazz.name, '.'))[array_length(string_to_array(clazz.name, '.'), 1)] from baseclass join clazz on clazz.id=baseclass.clazz_id where securedType is null and baseclass_id=baseclass.id;""";
            executeSQL(select,sql);
        }
    }

    private static void alterPermissionGroupToBaseclass(Statement select) throws SQLException {
        String sql= """
                alter table permissiongrouptobaseclass 
                add column if not exists securedId varchar(255),
                add column if not exists securedType varchar(255);""";
        executeSQL(select,sql);
    }

    private static void dropOperationToGroupColumns(Statement select) throws SQLException {
        String sql="alter table operationToGroup drop column if exists operation_id";
        executeSQL(select,sql);
    }

    private static void updateOperationToGroup(Statement select) throws SQLException {

        {
            String sql = """
            update operationToGroup set operationId=operation_id where operationId is null and not operation_id is
            null;""";
            executeSQL(select,sql);
        }

    }

    private static void alterOperationToGroup(Statement select) throws SQLException {
        String sql= """
                alter table operationToGroup 
                add column if not exists operationId varchar(255);""";
        executeSQL(select,sql);
    }

    private static void dropBaseclass(Statement select) throws SQLException {
        String sql="drop table baseclass cascade";
        executeSQL(select,sql);
    }

    private static void dropConstraints(List<Reference> securityReference, Statement select) throws SQLException {
        for (Reference reference : securityReference) {
           String sql="alter table %s drop constraint if exists %s;";
           sql=sql.formatted(reference.tableName(),reference.constraintName());
            executeSQL(select, sql);
        }
    }

    private static void addColumns(List<Reference> securityReference, Statement select) throws SQLException {
        for (Reference reference : securityReference) {
            {

                String sql = "alter table %s  DROP CONSTRAINT IF EXISTS fk_%s_creator_id , DROP CONSTRAINT IF EXISTS fk_%s_tenant_id;";
                sql=sql.formatted(reference.tableName(),reference.tableName(),reference.tableName());
                executeSQL(select, sql);
            }
            {
                String sql = "alter table %s add column if not exists creator_id varchar(255), ADD CONSTRAINT fk_%s_creator_id FOREIGN KEY (creator_id) REFERENCES usertable(id);";
                sql=sql.formatted(reference.tableName(),reference.tableName());
                executeSQL(select, sql);
            }

            {
                String sql = "alter table %s add column if not exists tenant_id varchar(255), ADD CONSTRAINT fk_%s_tenant_id FOREIGN KEY (tenant_id) REFERENCES securitytenant(id);";
                sql=sql.formatted(reference.tableName(),reference.tableName());
                executeSQL(select, sql);
            }
            {
                String sql = "alter table %s add column if not exists securityId varchar(255);";
                sql=sql.formatted(reference.tableName());
                executeSQL(select, sql);
            }
        }
    }

    private static void executeSQL(Statement select, String sql) throws SQLException {
        logger.info("executing sql: {}",sql);
        select.execute(sql);
    }

    private static void flattenSecurity(List<Reference> securityReference, Statement select) throws SQLException {
        for (Reference reference : securityReference) {

            String sql="update %s set creator_id=COALESCE(%s.creator_id,baseclass.creator_id),tenant_id=COALESCE(%s.tenant_id,baseclass.tenant_id),securityId=baseclass.id from baseclass where security_id=baseclass.id;";
            sql=sql.formatted(reference.tableName(),reference.tableName(),reference.tableName());
            executeSQL(select, sql);
        }
    }

    record Reference(String tableName,String columnName,String constraintName){}

    private static List<Reference> getTableNamesWithSecurityReference(Statement select) throws SQLException {
        String sql = """
                SELECT
                    conrelid::regclass AS referencing_table,
                    a.attname AS referencing_column,
                    conname AS constraint_name
                    
                FROM
                    pg_constraint AS c
                JOIN
                    pg_attribute AS a
                    ON a.attnum = ANY(c.conkey) AND a.attrelid = c.conrelid
                JOIN
                    pg_attribute AS af
                    ON af.attnum = ANY(c.confkey) AND af.attrelid = c.confrelid
                WHERE
                    c.confrelid = 'baseclass'::regclass
                    AND c.contype = 'f'
                    AND a.attname='security_id';""";
        logger.info("getting fields for tables SQL: {}", sql);
        ResultSet resultSet = select.executeQuery(sql);
        List<Reference> hashSet = new ArrayList<>();
        while (resultSet.next()) {
            hashSet.add(new Reference(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3)));
        }
        return hashSet;
    }

    private static void dropUnnecessaryColumns(List<Reference> references, Statement select) throws SQLException {

        for (Reference reference : references) {
            String sql="alter table %s drop column if exists %s;";
            sql=sql.formatted(reference.tableName(),reference.columnName());
            select.execute(sql);
        }

    }

}
