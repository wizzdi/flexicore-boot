package com.wizzdi.flexicore.security.data;

import com.flexicore.model.Clazz;
import com.wizzdi.flexicore.boot.jpa.service.EntitiesHolder;
import com.wizzdi.flexicore.security.request.BasicPropertiesFilter;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.ClazzFilter;
import jakarta.persistence.Entity;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Extension
public class ClazzRepository implements Plugin {

	private final List<Clazz> entities;

	public ClazzRepository(List<EntitiesHolder> entitiesHolder) {
		this.entities = new ArrayList<>(entitiesHolder.stream().map(f->f.getEntities()).flatMap(Set::stream)
				.map(f->Clazz.ofClass(f))
				.collect(Collectors.toMap(f->f.name(), f->f,(a, b)->a)).values());
	}




	public List<Clazz> listAllClazzs(ClazzFilter clazzFilter ) {
		return streamClazz(clazzFilter).sorted(Comparator.comparing(f->f.name())).collect(Collectors.toList());

	}

	private Stream<Clazz> streamClazz(ClazzFilter clazzFilter) {
		return entities.stream().filter(f -> filter(f, clazzFilter.getBasicPropertiesFilter()));
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



	public long countAllClazzs(ClazzFilter clazzFilter) {
		return streamClazz(clazzFilter).count();

	}
}
