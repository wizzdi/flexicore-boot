package com.wizzdi.flexicore.security.migrations;

import com.flexicore.model.SecurityUser;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FlexiCoreV4ToV5Migration {

    private static final Logger logger = LoggerFactory.getLogger(FlexiCoreV4ToV5Migration.class);

    public static final List<FieldMigration> SECURITY_LINK_FIELD_MIGRATIONS = List.of(new FieldMigration("value_id", "operation_id"), new FieldMigration("simplevalue", "access"));
    private static final List<TypeMigration> toMigrate = List.of(
            new TypeMigration("Clazz", Collections.emptyList()),
            new TypeMigration("SecurityUser", "UserTable", "SecurityUser", false, true, Collections.emptyList()),
            new TypeMigration("SecurityOperation", List.of(new FieldMigration("defaultaccess", "defaultAccess"))),
            new TypeMigration("SecurityTenant", Collections.emptyList()),
            new TypeMigration("Role", Collections.emptyList()),
            new TypeMigration("SecurityWildcard", "SecurityWildcard", "SecurityWildcard", false, false, Collections.emptyList()),
            new TypeMigration("OperationCategory", "OperationCategory", "OperationCategory", false, false, Collections.emptyList()),
            new TypeMigration("PermissionGroup", Collections.emptyList()),
            new TypeMigration("OperationToClazz", "OperationToClazz", "OperationToClazz", false, false, List.of(new FieldMigration("leftside_id", "operation_id"), new FieldMigration("rightside_id", "clazz_id"))),
            new TypeMigration("PermissionGroupToBaseclass", List.of(new FieldMigration("leftside_id", "permissionGroup_id"), new FieldMigration("rightside_id", "baseclass_id"))),
            new TypeMigration("TenantToUser", List.of(new FieldMigration("leftside_id", "tenant_id"), new FieldMigration("rightside_id", "user_id"), new FieldMigration("defualtTennant", "defaultTenant"))),
            new TypeMigration("RoleToUser", List.of(new FieldMigration("leftside_id", "role_id"), new FieldMigration("rightside_id", "user_id"))),
            new TypeMigration("SecurityLink", "SecurityLink", true, SECURITY_LINK_FIELD_MIGRATIONS), //special case
            new TypeMigration("RoleToBaseclass", "SecurityLink", true, join(SECURITY_LINK_FIELD_MIGRATIONS, List.of(new FieldMigration("leftside_id", "role_id")))),//special case
            new TypeMigration("TenantToBaseClassPremission", "SecurityLink", "TenantToBaseclass", true, true, join(SECURITY_LINK_FIELD_MIGRATIONS, List.of(new FieldMigration("leftside_id", "tenant_id")))),//special case
            new TypeMigration("UserToBaseClass", "SecurityLink", true, join(SECURITY_LINK_FIELD_MIGRATIONS, List.of(new FieldMigration("leftside_id", "user_id")))));//special case
    public static final String MIGRATE_WITHOUT_DTYPE =
            """
            insert into {0}(id,name,description,security_id,creationDate,updateDate,softDelete)
            select id,name,description,id,creationDate,updateDate,softDelete from baseclass where dtype=''{2}'' on conflict(id) do nothing
            """;
    public static final String MIGRATE_WITH_DTYPE = """
           insert into {0}(id,name,description,security_id,dtype,creationDate,updateDate,softDelete)
           select id,name,description,id,''{1}'',creationDate,updateDate,softDelete from baseclass where dtype=''{2}'' on conflict(id) do nothing
            """;

    public static final String MIGRATE_WITHOUT_SECURITY = """
            insert into {0}(id,name,description,creationDate,updateDate,softDelete)
            select id,name,description,creationDate,updateDate,softDelete from baseclass where dtype=''{2}'' on conflict(id) do nothing
            """;

    private static <T> List<T> join(List<T>... arrays) {
        return Arrays.stream(arrays).flatMap(List::stream).toList();
    }


    public static void migrateToFCV5(Statement select, Class<? extends SecurityUser>... classesExtendingUser) throws SQLException {
        List<TypeMigration> additional = getAdditionalTypes(classesExtendingUser);
        migrateTypes(select, additional);
        migrateTypeFields(select, additional);
        migrateSecurityLinksSpecific(select);
        migrateFK(select);
        dropUnnecessaryColumns(select, additional);
        migrateDtypes(select, additional);
    }

    private static void migrateDtypes(Statement select, List<TypeMigration> additional) throws SQLException {
        {
            String commaDelimitedTypesToMakeBaseclass = toMigrate.stream().map(f -> "'" + f.oldClassName() + "'").collect(Collectors.joining(","));
            String sql = MessageFormat.format(
                    "update baseclass set dtype=''Baseclass'' where dtype in ({0})",
                    commaDelimitedTypesToMakeBaseclass
            );
            logger.info("migrating dtypes SQL: {}", sql);
            select.execute(sql);
        }
        if(additional.isEmpty()){
            return;
        }
        {
            String commaDelimitedTypesToMakeBaseclass = additional.stream().map(f -> "'" + f.oldClassName() + "'").collect(Collectors.joining(","));
            String sql = MessageFormat.format(
                    "update baseclass set dtype=''Baseclass'' where dtype in ({0})",
                    commaDelimitedTypesToMakeBaseclass
            );
            logger.info("migrating additional dtypes SQL: {}", sql);
            select.execute(sql);
        }
    }

    private static void dropUnnecessaryColumns(Statement select, List<TypeMigration> additional) throws SQLException {
        {
            String sql = """
                    alter table baseclass 
                    drop column if exists simplevalue , 
                    drop column if exists value_id ,
                    drop column if exists defualtTennant ,
                    drop column if exists defaultaccess ,
                    drop column if exists leftside_id ,
                    drop column if exists rightside_id ,
                    drop column if exists left_id ,
                    drop column if exists right_id 
                    """;
            logger.info("dropping unnecessary columns {}", sql);
            select.execute(sql);
        }
        if(additional.isEmpty()){
            return;
        }
        {
            String sql = additional.stream().map(f -> "drop column if exists " + f.oldClassName()).collect(Collectors.joining(",", "alter table baseclass ", ""));
            logger.info("dropping additional columns {}", sql);
            select.execute(sql);


        }
    }

    private static void migrateFK(Statement select) throws SQLException {
        {  //drop constraints
            String sql = """
                    alter table baseclass 
                    drop constraint IF EXISTS  fk_baseclass_clazz_id, 
                    drop constraint IF EXISTS fk_baseclass_creator_id, 
                    drop constraint IF EXISTS fk_baseclass_tenant_id
                    """;
            logger.info("dropping constraints SQL: {}", sql);
            select.execute(sql);
        }
        {
            //create constraints
            logger.info("creating constraints");
            String sql = """
                    alter table baseclass 
                    add constraint fk_baseclass_clazz_id foreign key (clazz_id) references clazz(id),
                    add constraint fk_baseclass_creator_id foreign key (creator_id) references userTable(id),
                    add constraint fk_baseclass_tenant_id foreign key (tenant_id) references securitytenant(id)
                       
                    """;
            select.execute(sql);
        }
    }

    private static List<TypeMigration> getAdditionalTypes(Class<? extends SecurityUser>[] classesExtendingUser) {
        return Arrays.stream(classesExtendingUser).map(f -> getType(f)).toList();
    }

    private static TypeMigration getType(Class<? extends SecurityUser> aClass) {
        List<FieldMigration> fieldMigrations = Arrays.stream(aClass.getDeclaredFields()).map(f -> getFieldMigration(f)).filter(f -> f != null).toList();
        String simpleName = aClass.getSimpleName();
        return new TypeMigration(simpleName, "UserTable", simpleName, true, true, fieldMigrations);
    }

    private static FieldMigration getFieldMigration(Field f) {
        if (f.isAnnotationPresent(OneToMany.class)) {
            return null;
        }
        if (f.isAnnotationPresent(ManyToOne.class) || f.isAnnotationPresent(OneToOne.class)) {
            return new FieldMigration(f.getName() + "_id");
        }
        return new FieldMigration(f.getName());
    }

    private static void migrateSecurityLinksSpecific(Statement select) throws SQLException {
        {
            String sql = "update securitylink set permissionGroup_id=l.rightside_id  from baseclass as l join baseclass as t on t.id=l.rightside_id where securitylink.id=l.id and t.dtype='PermissionGroup'";


            logger.info("migrating permission group security links", sql);
            int updatedEntries = select.executeUpdate(sql);
        }

        {
            String sql = "update securitylink set clazz_id=l.rightside_id  from  baseclass as l join baseclass as t on t.id=l.rightside_id where securitylink.id=l.id and t.dtype='Clazz'";


            logger.info("migrating clazz security links", sql);
            int updatedEntries = select.executeUpdate(sql);
        }

        {
            String sql = "update securitylink set baseclass_id=l.rightside_id from baseclass as l join baseclass as t on t.id=l.rightside_id where securitylink.id=l.id and not t.dtype in ('Clazz','PermissionGroup')";


            logger.info("migrating baseclass security links", sql);
            int updatedEntries = select.executeUpdate(sql);
        }
    }

    private static void migrateTypeFields(Statement select, List<TypeMigration> additional) throws SQLException {
        for (TypeMigration typeMigration : toMigrate) {
            fieldMigration(select, typeMigration);
        }
        for (TypeMigration typeMigration : additional) {
            fieldMigration(select, typeMigration);
        }
    }

    private static void fieldMigration(Statement select, TypeMigration typeMigration) throws SQLException {
        for (FieldMigration fieldMigration : typeMigration.fieldMigrations()) {
            String sql = MessageFormat.format(
                    "update {0} set {1}=baseclass.{2} from baseclass where {0}.id=baseclass.id and baseclass.dtype=''{3}''",
                    typeMigration.tableName(),
                    fieldMigration.newName(),
                    fieldMigration.oldName(),
                    typeMigration.oldClassName()
            );            logger.info("migrating field {} SQL: {}", fieldMigration.oldName(), sql);
            int updatedEntries = select.executeUpdate(sql);

        }
    }

    private static void migrateTypes(Statement select, List<TypeMigration> additional) throws SQLException {
        for (TypeMigration typeMigration : toMigrate) {
            migrateType(select, typeMigration);
        }
        for (TypeMigration typeMigration : additional) {
            migrateType(select, typeMigration);
        }
    }

    private static void migrateType(Statement select, TypeMigration typeMigration) throws SQLException {
        String template = getTemplate(typeMigration);
        String sql = MessageFormat.format(template,
                typeMigration.tableName(),
                typeMigration.newClassName(),
                typeMigration.oldClassName()
        );
        logger.info("migrating type {} SQL: {}", typeMigration.oldClassName(), sql);
        int updatedEntries = select.executeUpdate(sql);
    }

    private static String getTemplate(TypeMigration typeMigration) {
        if (!typeMigration.hasSecurity()) {
            return MIGRATE_WITHOUT_SECURITY;
        }
        return typeMigration.hasDtype() ? MIGRATE_WITH_DTYPE : MIGRATE_WITHOUT_DTYPE;
    }

    public record FieldMigration(String oldName, String newName) {
        public FieldMigration {
        }

        public FieldMigration(String oldName) {
            this(oldName, oldName);
        }
    }

    public record TypeMigration(String oldClassName, String tableName, String newClassName, boolean hasDtype,
                                boolean hasSecurity, List<FieldMigration> fieldMigrations) {
        public TypeMigration {
        }


        public TypeMigration(String oldClassName, List<FieldMigration> fieldMigrations) {
            this(oldClassName, oldClassName, oldClassName, false, true, fieldMigrations);
        }

        public TypeMigration(String oldClassName, String tableName, boolean hasDtype, List<FieldMigration> fieldMigrations) {
            this(oldClassName, tableName, oldClassName, hasDtype, true, fieldMigrations);
        }

        public TypeMigration(String oldClassName, String tableName, List<FieldMigration> fieldMigrations) {
            this(oldClassName, tableName, oldClassName, false, true, fieldMigrations);
        }
    }


}
