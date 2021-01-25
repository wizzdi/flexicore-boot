package com.wizzdi.flexicore.boot.dynamic.invokers.response;

import com.flexicore.annotations.IOperation;
import com.flexicore.model.Baseclass;
import com.wizzdi.flexicore.boot.dynamic.invokers.annotations.Invoker;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class InvokerInfo {


	private Class<?> name;
	private String description;
	private String displayName;
	private Class<?> handlingType;
	private List<InvokerMethodInfo> methods = new ArrayList<>();

	public InvokerInfo() {
	}

	public InvokerInfo(InvokerInfo other, Set<String> allowedOps) {
		this.name = other.name;
		this.description = other.description;
		this.displayName = other.displayName;
		this.handlingType = other.handlingType;
		this.methods = other.methods.parallelStream().filter(f -> allowedOps.contains(f.getId())).collect(Collectors.toList());

	}

	public InvokerInfo(Object invoker) {
		com.wizzdi.flexicore.boot.dynamic.invokers.annotations.InvokerInfo InvokerInfo = AnnotationUtils.findAnnotation(invoker.getClass(), com.wizzdi.flexicore.boot.dynamic.invokers.annotations.InvokerInfo.class);
		name = invoker.getClass();
		displayName = InvokerInfo != null && !InvokerInfo.displayName().isEmpty() ? InvokerInfo.displayName() : invoker.getClass().getName();
		description = InvokerInfo != null && !InvokerInfo.description().isEmpty() ? InvokerInfo.description() : "No Description";
		handlingType = invoker instanceof Invoker ? ((Invoker) invoker).getHandlingClass() : getAutomatically(invoker);
		Method[] methods = invoker.getClass().getDeclaredMethods();
		for (Method method : methods) {
			com.wizzdi.flexicore.boot.dynamic.invokers.annotations.InvokerMethodInfo InvokerMethodInfo = AnnotationUtils.findAnnotation(method, com.wizzdi.flexicore.boot.dynamic.invokers.annotations.InvokerMethodInfo.class);
			if (InvokerMethodInfo != null) {
				this.methods.add(new InvokerMethodInfo(method, InvokerMethodInfo));
			}
			else{
                RequestMapping requestMapping = AnnotationUtils.findAnnotation(method, RequestMapping.class);
                if(requestMapping!=null){
                    IOperation iOperation=AnnotationUtils.findAnnotation(method,IOperation.class);
                    if(iOperation==null){
                        iOperation=getIOOperation(method);
                    }
                    com.wizzdi.flexicore.boot.dynamic.invokers.annotations.InvokerMethodInfo invokerMethodInfo=getInvokerMethodInfo(invoker.getClass(),method,iOperation);
                    this.methods.add(new InvokerMethodInfo(method, invokerMethodInfo));
                }

            }
		}


	}

    private IOperation getIOOperation(Method method) {
        Class<?> methodSubject = getMethodSubject(method);

        return new IOperation(){
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
                return Baseclass.class.isAssignableFrom(methodSubject) ?new Class[]{methodSubject}:new Class[0];
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

    private com.wizzdi.flexicore.boot.dynamic.invokers.annotations.InvokerMethodInfo getInvokerMethodInfo(Class<?> c, Method method, IOperation iOperation) {
        Class<?> methodSubject = getMethodSubject(method);
        Optional<String> relatedMethod= Arrays.stream(c.getMethods()).filter(f->f.getName().equals(method.getName()+"_state")).map(f->f.getName()).findFirst();
        return new com.wizzdi.flexicore.boot.dynamic.invokers.annotations.InvokerMethodInfo(){
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
                return PaginationResponse.class.equals(methodSubject)?new String[0]:new String[]{method.getName().contains("create")?"TYPE_ACTION":"ACTION"};
            }

            @Override
            public String[] relatedMethodNames() {
                return relatedMethod.isPresent()?new String[]{relatedMethod.get()}:new String[0];
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

    private Class<?> getAutomatically(Object invoker) {
		Map<Class<?>, List<Class<?>>> collect = Arrays.stream(invoker.getClass().getMethods()).filter(f -> !f.isBridge()).map(f -> getMethodSubject(f)).collect(Collectors.groupingBy(f -> f));
		return collect.entrySet().stream().max(Comparator.comparing(e -> e.getValue().size())).map(f -> f.getKey()).orElse(null);
	}

	private Class<?> getMethodSubject(Method f) {
		return f.getReturnType();
	}


	public Class<?> getName() {
		return name;
	}

	public InvokerInfo setName(Class<? extends Invoker> name) {
		this.name = name;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public InvokerInfo setDescription(String description) {
		this.description = description;
		return this;
	}

	public String getDisplayName() {
		return displayName;
	}

	public InvokerInfo setDisplayName(String displayName) {
		this.displayName = displayName;
		return this;
	}

	public List<InvokerMethodInfo> getMethods() {
		return methods;
	}

	public InvokerInfo setMethods(List<InvokerMethodInfo> methods) {
		this.methods = methods;
		return this;
	}

	public Class<?> getHandlingType() {
		return handlingType;
	}

	public InvokerInfo setHandlingType(Class<?> handlingType) {
		this.handlingType = handlingType;
		return this;
	}

	@Override
	public String toString() {
		return "InvokerInfo{" +
				"name=" + name +
				", description='" + description + '\'' +
				", displayName='" + displayName + '\'' +
				", handlingType=" + handlingType +
				", methods=" + methods +
				'}';
	}
}
