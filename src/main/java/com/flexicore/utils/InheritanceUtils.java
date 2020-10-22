package com.flexicore.utils;

import com.flexicore.annotations.AnnotatedClazz;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.model.Baseclass;
import com.flexicore.model.Clazz;
import com.flexicore.request.GetClassInfo;
import com.flexicore.response.ClassInfo;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InheritanceUtils {
    private static Map<String, Boolean> handled = new ConcurrentHashMap<>();
    public static Map<String, String> simpleToSimpleInhereting = new ConcurrentHashMap<>();
    private static Map<String, Set<ClassInfo>> inheretingClasses = new ConcurrentHashMap<>();

    public static void registerInheretingClass(Class<?> c) {
        registerClass(c);
    }

    public static void registerClass(Class<?> c) {
        registerClass(c, true);
    }

    public static void registerClass(Class<?> c, boolean classInfo) {
        if(!classInfo){
            return;
        }
        String canonicalName = c.getCanonicalName();
        if(canonicalName!=null && (!handled.containsKey(canonicalName)||(!handled.get(canonicalName)&&classInfo))){
            handled.put(canonicalName, classInfo);
            Class<?> current = c;
            while (current.getSuperclass() != null) {
                Class<?> superC = current.getSuperclass();
                simpleToSimpleInhereting.put(current.getSimpleName(), superC.getSimpleName());
                if (classInfo) {
                    Set<ClassInfo> list = inheretingClasses.computeIfAbsent(superC.getCanonicalName(), f -> new HashSet<>());
                    AnnotatedClazz annotatedClazz = current.getDeclaredAnnotation(AnnotatedClazz.class);
                    list.add(new ClassInfo(current, annotatedClazz));
                }

                current = superC;

            }

        }

    }

    public static PaginationResponse<ClassInfo> listInheritingClassesWithFilter(GetClassInfo getClassInfo) {
        if (getClassInfo.getNameLike() != null) {
            getClassInfo.setNameLike(getClassInfo.getNameLike().replace("%", ""));
        }
        Set<ClassInfo> classes = listInheritingClasses(getClassInfo.getClassName());
        for (ClassInfo aClass : classes) {
            if (aClass.getClazzId() == null && aClass.getClazz() != null) {
                Clazz clazzbyname = Baseclass.getClazzByName(aClass.getClazz().getCanonicalName());
                aClass.setClazzId(clazzbyname != null ? clazzbyname.getId() : null);
            }
        }
        int max = getClassInfo.getPageSize() != null ? getClassInfo.getPageSize() : classes.size();
        int currentPage = getClassInfo.getCurrentPage() != null ? getClassInfo.getCurrentPage() : 0;
        List<ClassInfo> toRet = classes.parallelStream().skip(currentPage * max).limit(max).filter(f -> filterInheriting(f.getClazz(), getClassInfo)).collect(Collectors.toList());
        return new PaginationResponse<>(toRet, getClassInfo, classes.size());
    }

    private static boolean filterInheriting(Class<?> f, GetClassInfo getClassInfo) {
        return getClassInfo.getNameLike() == null || f.getCanonicalName().toLowerCase().contains(getClassInfo.getNameLike());
    }

    private static Set<ClassInfo> listInheritingClasses(String className) {
        Set<ClassInfo> list = new HashSet<>();
        Set<ClassInfo> currentSet = inheretingClasses.get(className);
        if (currentSet != null) {
            for (ClassInfo aClass : currentSet) {
                list.addAll(listInheritingClasses(aClass.getClazz().getCanonicalName()));
                list.add(aClass);
            }
        }
        return list;
    }
}
