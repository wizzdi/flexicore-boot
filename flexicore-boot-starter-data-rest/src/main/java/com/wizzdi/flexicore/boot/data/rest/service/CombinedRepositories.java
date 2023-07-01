package com.wizzdi.flexicore.boot.data.rest.service;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.support.Repositories;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CombinedRepositories extends Repositories {

	private List<Repositories> more;

	/**
	 * Creates a new {@link Repositories} instance by looking up the repository instances and meta information from the
	 * given {@link ListableBeanFactory}.
	 *
	 * @param main must not be {@literal null}.
	 */
	public CombinedRepositories(ListableBeanFactory main, List<Repositories> more) {
		super(main);
		this.more = more;

	}


	@Override
	public boolean hasRepositoryFor(Class<?> domainClass) {
		return super.hasRepositoryFor(domainClass) || more.stream().anyMatch(f -> f.hasRepositoryFor(domainClass));
	}

	@Override
	public Optional<Object> getRepositoryFor(Class<?> domainClass) {
		Optional<Object> repositoryFor = super.getRepositoryFor(domainClass);
		if (repositoryFor.isEmpty()) {
			for (Repositories classes : more) {
				Optional<Object> moreRepo = classes.getRepositoryFor(domainClass);
				if (moreRepo.isPresent()) {
					return moreRepo;
				}
			}
		}
		return repositoryFor;
	}


	@Override
	public Optional<RepositoryInformation> getRepositoryInformationFor(Class<?> domainClass) {
		Optional<RepositoryInformation> repositoryInformationFor = super.getRepositoryInformationFor(domainClass);
		if (repositoryInformationFor.isEmpty()) {
			for (Repositories classes : more) {
				repositoryInformationFor = classes.getRepositoryInformationFor(domainClass);
				if (repositoryInformationFor.isPresent()) {
					return repositoryInformationFor;
				}
			}
		}
		return repositoryInformationFor;
	}

	@Override
	public RepositoryInformation getRequiredRepositoryInformation(Class<?> domainType) {
		try {
			return super.getRequiredRepositoryInformation(domainType);
		} catch (Throwable e) {
			for (Repositories classes : more) {
				try {
					return classes.getRequiredRepositoryInformation(domainType);
				} catch (Throwable ignored) {

				}
			}
			throw e;
		}
	}

	@Override
	public <T, S> EntityInformation<T, S> getEntityInformationFor(Class<?> domainClass) {
		try {
			return super.getEntityInformationFor(domainClass);
		} catch (Throwable e) {
			for (Repositories classes : more) {
				try {
					return classes.getEntityInformationFor(domainClass);
				}
				catch (Throwable ignored) {

				}
			}
			throw e;
		}

	}

	@Override
	public Optional<RepositoryInformation> getRepositoryInformation(Class<?> repositoryInterface) {
		Optional<RepositoryInformation> repositoryInformation = super.getRepositoryInformation(repositoryInterface);
		if (repositoryInformation.isEmpty()) {
			for (Repositories classes : more) {
				repositoryInformation = classes.getRepositoryInformation(repositoryInterface);
				if (repositoryInformation.isPresent()) {
					return repositoryInformation;
				}
			}
		}

		return repositoryInformation;
	}

	@Override
	public PersistentEntity<?, ?> getPersistentEntity(Class<?> domainClass) {
		try {
			return super.getPersistentEntity(domainClass);
		}
		catch (Throwable e){
			for (Repositories classes : more) {
				try {
					return classes.getPersistentEntity(domainClass);
				}
				catch (Throwable ignored){

				}
			}
			throw e;
		}
	}

	@Override
	public List<QueryMethod> getQueryMethodsFor(Class<?> domainClass) {
		try {
			return super.getQueryMethodsFor(domainClass);
		}
		catch (Throwable e){
			for (Repositories classes : more) {
				try{
					return classes.getQueryMethodsFor(domainClass);
				}
				catch (Throwable ignored){

				}
			}
			throw e;
		}
	}

	@Override
	public Iterator<Class<?>> iterator() {
		List<Iterator<Class<?>>> iterators = new ArrayList<>();
		iterators.add(super.iterator());
		iterators.addAll(more.stream().map(f -> f.iterator()).collect(Collectors.toList()));
		return new IteratorIterator<>(iterators);
	}
}
