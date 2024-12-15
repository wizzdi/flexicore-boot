package com.wizzdi.flexicore.security.data;

import com.flexicore.model.Clazz;
import com.wizzdi.flexicore.security.request.BasicPropertiesFilter;
import com.wizzdi.segmantix.model.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.ClazzFilter;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Component
@Extension
public class ClazzRepository implements Plugin {
	@PersistenceContext
	private EntityManager em;



	public List<Clazz> listAllClazzs(ClazzFilter clazzFilter ) {
		return streamClazz(clazzFilter).sorted(Comparator.comparing(f->f.name())).toList();

	}

	private Stream<Clazz> streamClazz(ClazzFilter clazzFilter) {
		return em.getMetamodel().getEntities().stream().map(f -> f.getName())
				.map(f -> getClazz(f)).filter(f -> filter(f, clazzFilter.getBasicPropertiesFilter()));
	}

	private boolean filter(Clazz clazz, BasicPropertiesFilter basicPropertiesFilter) {
		if(basicPropertiesFilter==null){
			return true;
		}
		boolean pass=true;
		if(basicPropertiesFilter.getNameLike()!=null){
			String like = basicPropertiesFilter.getNameLike().replace("%", "");
			if(basicPropertiesFilter.isNameLikeCaseSensitive()){
				pass=pass&&clazz.name().contains(like);
			}
			else{

				pass=pass&&clazz.name().toLowerCase().contains(like.toLowerCase());
			}
		}
		if(basicPropertiesFilter.getNameLike()!=null){
			pass=pass&&basicPropertiesFilter.getNameLike().contains(clazz.name());
		}
		return pass;

	}

	private Clazz getClazz(String name) {
		return new Clazz(name);
	}


	public long countAllClazzs(ClazzFilter clazzFilter) {
		return streamClazz(clazzFilter).count();

	}
}
