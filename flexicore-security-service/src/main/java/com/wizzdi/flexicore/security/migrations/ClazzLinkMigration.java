package com.wizzdi.flexicore.security.migrations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.sql.Statement;

public class ClazzLinkMigration {

    private static final Logger logger= LoggerFactory.getLogger(ClazzLinkMigration.class);

    public static void migrateClazzLink(Statement select) throws SQLException {
        logger.info("Starting Migration of clazz link");
        String sql = "update baseclass set dtype='Clazz' where dtype='ClazzLink'";
        logger.info("executing SQL: " + sql);
        int updatedEntries = select.executeUpdate(sql);
    }
}
