package com.flexicore.data;

import org.pf4j.Extension;
import org.springframework.stereotype.Component;
import com.flexicore.interfaces.PluginRepository;
import com.flexicore.model.nosql.BaseclassNoSQL;
import com.flexicore.request.BaseclassNoSQLFilter;
import com.flexicore.request.GetClassInfo;
import com.flexicore.utils.InheritanceUtils;
import com.mongodb.MongoClientSettings;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.*;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;


public interface BaseclassNoSQLRepository extends PluginRepository {

    static Logger logger = Logger.getLogger(BaseclassNoSQLRepository.class.getCanonicalName());
    String BASECLASS_NO_SQL = "BaseclassNoSQL";
    String ID = "_id";
    String TYPE = "type";
    String DATE_CREATED = "dateCreated";


    AtomicReference<CodecRegistry> pojoCodecRegistry = new AtomicReference<>(null);
    Queue<Class<?>> clazzToRegister = new ConcurrentLinkedQueue<>();
    AtomicReference<Set<Class<?>>> lastRegistered = new AtomicReference<>(new HashSet<>());
    static Object codecLock=new Object();

    public static CodecRegistry getPojoCodecRegistry() {
        return pojoCodecRegistry.get();
    }

    public static void addClassForMongoCodec(Class<?>... c) {
        for (Class<?> aClass : c) {
            addClassForMongoCodec(aClass);
        }
    }

    public static void addClassForMongoCodec(Class<?> c) {
        synchronized (codecLock){
            System.out.println("Adding codec class " + c);
            clazzToRegister.add(c);
            System.out.println("registered codec classes are " + clazzToRegister.parallelStream().map(f -> f.getCanonicalName()).collect(Collectors.joining(",")));
            PojoCodecProvider.Builder builder = PojoCodecProvider.builder();
            for (Class<?> aClass : clazzToRegister) {
                builder = builder.register(aClass);
            }
            CodecRegistry newValue = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), fromProviders(builder.build()));
            pojoCodecRegistry.set(newValue);
            for (Class<?> aClass : clazzToRegister) {
                Codec<?> codec =null;
                try {
                    codec = newValue.get(aClass);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                if(codec==null){
                    System.out.println("ERROR: codec for "+aClass +" failed to be registered");
                }
            }
            System.out.println("Rebuilt mongo reg- " + clazzToRegister.parallelStream().map(f -> f.getCanonicalName()).collect(Collectors.joining(",")));

        }

    }

    public static Bson getBaseclassNoSQLPredicates(BaseclassNoSQLFilter baseclassNoSQLFilter) {
        Bson pred = null;
        String baseclassNoSQLType = baseclassNoSQLFilter.getBaseclassNoSQLType();
        if (baseclassNoSQLType != null) {
            Set<String> names = InheritanceUtils.listInheritingClassesWithFilter(new GetClassInfo().setClassName(baseclassNoSQLType)).getList().parallelStream().map(f -> f.getClazz().getCanonicalName()).collect(Collectors.toSet());
            names.add(baseclassNoSQLType);
            Bson eq = in(TYPE, names);
            pred = pred == null ? eq : and(pred, eq);
        }

        if(baseclassNoSQLFilter.getFromDate()!=null){
            Date fromDate= Date.from(baseclassNoSQLFilter.getFromDate().toInstant());
            Bson gt = baseclassNoSQLFilter.isFromDateExclusive()?gt(DATE_CREATED, fromDate):gte(DATE_CREATED, fromDate);
            pred = pred == null ? gt : and(pred, gt);
        }

        if(baseclassNoSQLFilter.getToDate()!=null){
            Date toDate= Date.from(baseclassNoSQLFilter.getToDate().toInstant());
            Bson lt = baseclassNoSQLFilter.isToDateExclusive()?lt(DATE_CREATED, toDate):lte(DATE_CREATED, toDate);
            pred = pred == null ? lt : and(pred, lt);
        }
        return pred;
    }

    <T extends BaseclassNoSQL> T getByIdOrNull(Class<T> c, String id);

    <T extends BaseclassNoSQL> List<T> listByIds(Class<T> c, Set<String> ids);

    void mergeBaseclassNoSQL(BaseclassNoSQL o);

    void massMergeBaseclassNoSQL(List<? extends BaseclassNoSQL> o);

    <T extends BaseclassNoSQL> T getByIdOrNull(Class<T> c,String collectionName, String id);

    <T extends BaseclassNoSQL> List<T> listByIds(Class<T> c,String collectionName, Set<String> ids);

    void mergeBaseclassNoSQL(BaseclassNoSQL o,String collectionName);

    void massMergeBaseclassNoSQL(List<? extends BaseclassNoSQL> o,String collectionName);


    void refreshEntityManager();


    void persist(Object o);

    void merge(Object o);

    void batchMerge(List<Object> o);

    void batchPersist(List<?> o);

}
