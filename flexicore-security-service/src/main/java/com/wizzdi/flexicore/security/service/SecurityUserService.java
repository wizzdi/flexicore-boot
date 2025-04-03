package com.wizzdi.flexicore.security.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Basic;
import com.flexicore.model.SecurityLink;
import com.flexicore.model.SecurityUser;
import com.wizzdi.flexicore.security.configuration.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.SecurityUserRepository;
import com.wizzdi.flexicore.security.interfaces.SearchStringProvider;
import com.wizzdi.flexicore.security.request.SecurityLinkCreate;
import com.wizzdi.flexicore.security.request.SecurityLinkFilter;
import com.wizzdi.flexicore.security.request.SecurityUserCreate;
import com.wizzdi.flexicore.security.request.SecurityUserFilter;
import com.wizzdi.flexicore.security.request.SecurityUserUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import jakarta.persistence.metamodel.SingularAttribute;
import org.pf4j.Extension;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Extension
@Component
public class SecurityUserService implements Plugin, InitializingBean {

	@Autowired
	private SecurityEntityService securityEntityService;
	@Autowired
	private SecurityUserRepository securityUserRepository;
	@Autowired
	private ObjectProvider<SearchStringProvider<?>> searchStringProviders;
	private final Map<String, SearchStringProvider<?>> searchStringProviderCache=new ConcurrentHashMap<>();

	public <X extends SecurityUser, T extends X> Optional<SearchStringProvider<X>> getSearchStringProvider(Class<T> c) {
		String key = c.getCanonicalName();
		SearchStringProvider<?> searchStringProvider = searchStringProviderCache.get(key);
		if (searchStringProvider != null) {
			return Optional.of((SearchStringProvider<X>) searchStringProvider);
		}
		searchStringProvider = searchStringProviders.stream().filter(f -> f.forType().equals(c)).findFirst().orElse(null);
		if (searchStringProvider != null) {
			searchStringProviderCache.put(key, searchStringProvider);
			return Optional.of((SearchStringProvider<X>) searchStringProvider);
		}
		return searchStringProviders.stream().filter(f -> f.forType().isAssignableFrom(c)).map(f -> ((SearchStringProvider<X>) f)).findFirst();
	}


	public SecurityUser createSecurityUser(SecurityUserCreate securityUserCreate, SecurityContext securityContext) {
		SecurityUser securityUser = createSecurityUserNoMerge(securityUserCreate, securityContext);
		securityUserRepository.merge(securityUser);
		return securityUser;
	}


	public SecurityUser createSecurityUserNoMerge(SecurityUserCreate securityUserCreate, SecurityContext securityContext) {
		SecurityUser securityUser = new SecurityUser();
		securityUser.setId(UUID.randomUUID().toString());
		updateSecurityUserNoMerge(securityUserCreate, securityUser);
		BaseclassService.createSecurityObjectNoMerge(securityUser, securityContext);
		return securityUser;
	}

	public boolean updateSecurityUserNoMerge(SecurityUserCreate securityUserCreate, SecurityUser securityUser) {
		boolean update = securityEntityService.updateNoMerge(securityUserCreate, securityUser);
		String searchString = getSearchString(securityUser).orElse(null);
		if (searchString != null && !searchString.equals(securityUser.getSearchString())) {
			securityUser.setSearchString(searchString);
			update = true;
		}
		return update;
	}

	public <X extends SecurityUser, T extends X> Optional<String> getSearchString(T t) {
		Optional<SearchStringProvider<X>> searchStringProvider = getSearchStringProvider((Class<X>) t.getClass());
		return searchStringProvider.map(f -> getSearchString(f, t));
	}

	private <T extends SecurityUser> String getSearchString(SearchStringProvider<T> f, T t) {
		return f.getSearchString(t);
	}

	public SecurityUser updateSecurityUser(SecurityUserUpdate securityUserUpdate, SecurityContext securityContext) {
		SecurityUser securityUser = securityUserUpdate.getSecurityUser();
		if (updateSecurityUserNoMerge(securityUserUpdate, securityUser)) {
			securityUserRepository.merge(securityUser);
		}
		return securityUser;
	}


	public PaginationResponse<SecurityUser> getAllSecurityUsers(SecurityUserFilter securityUserFilter, SecurityContext securityContext) {
		List<SecurityUser> list = listAllSecurityUsers(securityUserFilter, securityContext);
		long count = securityUserRepository.countAllSecurityUsers(securityUserFilter, securityContext);
		return new PaginationResponse<>(list, securityUserFilter, count);
	}

	public List<SecurityUser> listAllSecurityUsers(SecurityUserFilter securityUserFilter, SecurityContext securityContext) {
		return securityUserRepository.listAllSecurityUsers(securityUserFilter, securityContext);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
		return securityUserRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
		return securityUserRepository.getByIdOrNull(id, c, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContext securityContext) {
		return securityUserRepository.getByIdOrNull(id, c, baseclassAttribute, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContext securityContext) {
		return securityUserRepository.listByIds(c, ids, baseclassAttribute, securityContext);
	}

	public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
		return securityUserRepository.findByIds(c, ids, idAttribute);
	}

	public <T extends Basic> List<T> findByIds(Class<T> c, Set<String> requested) {
		return securityUserRepository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return securityUserRepository.findByIdOrNull(type, id);
	}


	public <T> T merge(T base) {
		return securityUserRepository.merge(base);
	}


	public <T> T merge(T base, boolean updateDate, boolean propagateEvents) {
		return securityUserRepository.merge(base, updateDate, propagateEvents);
	}


	public void massMerge(List<?> toMerge, boolean updatedate, boolean propagateEvents) {
		securityUserRepository.massMerge(toMerge, updatedate, propagateEvents);
	}


	public <T> T merge(T base, boolean updateDate) {
		return securityUserRepository.merge(base, updateDate);
	}


	public void massMerge(List<?> toMerge) {
		securityUserRepository.massMerge(toMerge);
	}


	public void massMerge(List<?> toMerge, boolean updatedate) {
		securityUserRepository.massMerge(toMerge, updatedate);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		securityUserRepository.createSearchIndex();
	}

	public void refreshSearchString(SecurityUserFilter securityUserFilter, SecurityContext securityContext) {
		List<SecurityUser> list=listAllSecurityUsers(securityUserFilter,securityContext);
		for (List<SecurityUser> securityUsers : partition(list, 100)) {
			List<Object> toMerge=new ArrayList<>();
			for (SecurityUser securityUser : securityUsers) {
				if(updateSecurityUserNoMerge(new SecurityUserCreate(),securityUser)){
					toMerge.add(securityUser);
				}

			}
			securityUserRepository.massMerge(toMerge);

		}

	}
	public static <T> List<List<T>> partition(List<T> list, int size) {
		if (size <= 0) {
			throw new IllegalArgumentException("Size must be greater than 0");
		}
		List<List<T>> partitions = new ArrayList<>();
		for (int i = 0; i < list.size(); i += size) {
			partitions.add(list.subList(i, Math.min(i + size, list.size())));
		}
		return partitions;
	}
}
