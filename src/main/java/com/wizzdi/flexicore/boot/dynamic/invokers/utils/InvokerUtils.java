package com.wizzdi.flexicore.boot.dynamic.invokers.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.annotations.IOperation;
import com.flexicore.model.Baseclass;
import com.wizzdi.flexicore.boot.dynamic.invokers.annotations.FieldInfo;
import com.wizzdi.flexicore.boot.dynamic.invokers.annotations.IdRefFieldInfo;
import com.wizzdi.flexicore.boot.dynamic.invokers.annotations.ListFieldInfo;
import com.wizzdi.flexicore.boot.dynamic.invokers.response.ParameterInfo;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

public class InvokerUtils {


	public static IOperation getIOOperation(Method method) {
		Class<?> methodSubject = getMethodSubject(method);

		return new IOperation() {
			@Override
			public String Name() {
				return method.getName();
			}

			@Override
			public String Description() {
				return method.getName();
			}

			@Override
			public String Category() {
				return "General";
			}

			@Override
			public boolean auditable() {
				return false;
			}

			@Override
			public Class<? extends Baseclass>[] relatedClazzes() {
				return Baseclass.class.isAssignableFrom(methodSubject) ? new Class[]{methodSubject} : new Class[0];
			}

			@Override
			public Access access() {
				return Access.allow;
			}

			@Override
			public Class<? extends Annotation> annotationType() {
				return IOperation.class;
			}
		};
	}

	public static Class<?> getMethodSubject(Method f) {
		return f.getReturnType();
	}


	public static com.wizzdi.flexicore.boot.dynamic.invokers.annotations.InvokerMethodInfo getInvokerMethodInfo(Class<?> c, Method method, IOperation iOperation) {
		Class<?> methodSubject = getMethodSubject(method);
		Optional<String> relatedMethod = Arrays.stream(c.getMethods()).filter(f -> f.getName().equals(method.getName() + "_state")).map(f -> f.getName()).findFirst();
		return new com.wizzdi.flexicore.boot.dynamic.invokers.annotations.InvokerMethodInfo() {
			@Override
			public String displayName() {
				return iOperation.Name();
			}

			@Override
			public String description() {
				return iOperation.Description();
			}

			@Override
			public String[] categories() {
				return PaginationResponse.class.equals(methodSubject) ? new String[0] : new String[]{method.getName().contains("create") ? "TYPE_ACTION" : "ACTION"};
			}

			@Override
			public String[] relatedMethodNames() {
				return relatedMethod.isPresent() ? new String[]{relatedMethod.get()} : new String[0];
			}

			@Override
			public IOperation.Access access() {
				return iOperation.access();
			}

			@Override
			public Class<? extends Baseclass>[] relatedClasses() {
				return iOperation.relatedClazzes();
			}

			@Override
			public Class<? extends Annotation> annotationType() {
				return com.wizzdi.flexicore.boot.dynamic.invokers.annotations.InvokerMethodInfo.class;
			}
		};
	}

	public static List<Field> getAllFields(Class<?> type) {
		List<Field> fields = new ArrayList<>();
		for (Class<?> current = type; current != null; current = current.getSuperclass()) {
			fields.addAll(Arrays.asList(current.getDeclaredFields()));
		}

		return fields.stream().filter(f -> !Modifier.isStatic(f.getModifiers())).collect(Collectors.toList());
	}


	public static ParameterInfo detectAutomatically(Field field, List<Field> fields, Field[] declaredFields) {
		if (AnnotatedElementUtils.findMergedAnnotation(field,JsonIgnore.class) == null) {
			String fieldName = field.getName();
			if (fieldName.toLowerCase().endsWith("id")) {
				String refName = fieldName.substring(0, fieldName.length() - 2);
				Optional<IdRefFieldInfo> related = fields.stream().filter(f -> f.getName().equals(refName)).findFirst().map(f -> getIdRefInfo(field, f.getType(), false)).or(() -> fromDeclared(field, declaredFields).map(f -> getIdRefInfo(field, f.getType(), true)));

				if (related.isPresent()) {
					IdRefFieldInfo idRefFieldInfo = related.get();
					return new ParameterInfo(field, idRefFieldInfo);
				}
			}
			FieldInfo fieldInfo = getFieldInfo(field);
			return new ParameterInfo(field, fieldInfo);
		}
		return null;


	}

	public static FieldInfo getFieldInfo(Field field) {
		return new FieldInfo() {
			@Override
			public String displayName() {
				return field.getName();
			}

			@Override
			public String description() {
				return field.getName();
			}

			@Override
			public boolean mandatory() {
				return false;
			}

			@Override
			public String defaultValue() {
				return "";
			}

			@Override
			public boolean enableRegexValidation() {
				return false;
			}

			@Override
			public String regexValidation() {
				return "";
			}

			@Override
			public boolean rangeEnabled() {
				return false;
			}

			@Override
			public double rangeMin() {
				return Double.NEGATIVE_INFINITY;
			}

			@Override
			public double rangeMax() {
				return Double.POSITIVE_INFINITY;
			}

			@Override
			public double valueSteps() {
				return Double.MIN_VALUE;
			}

			@Override
			public Class<? extends Annotation> annotationType() {
				return FieldInfo.class;
			}
		};
	}

	public static Optional<? extends Field> fromDeclared(Field field, Field[] declaredFields) {
		if (declaredFields.length == 2) {
			return Arrays.stream(declaredFields).filter(f -> !f.equals(field)).findFirst();
		}
		return Optional.empty();
	}

	public static IdRefFieldInfo getIdRefInfo(Field field, Class<?> type, boolean action) {
		return new IdRefFieldInfo() {
			@Override
			public String displayName() {
				return field.getName();
			}

			@Override
			public String description() {
				return field.getName();
			}

			@Override
			public boolean mandatory() {
				return false;
			}

			@Override
			public Class<?> refType() {
				return type;
			}

			@Override
			public boolean list() {
				return false;
			}

			@Override
			public boolean actionId() {
				return action;
			}

			@Override
			public Class<? extends Annotation> annotationType() {
				return IdRefFieldInfo.class;
			}
		};
	}
}
