package com.wizzdi.flexicore.file.service;

import com.flexicore.model.Basic;
import com.wizzdi.flexicore.security.configuration.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.file.data.ZipFileToFileResourceRepository;
import com.wizzdi.flexicore.file.model.ZipFileToFileResource;
import com.wizzdi.flexicore.file.request.ZipFileToFileResourceCreate;
import com.wizzdi.flexicore.file.request.ZipFileToFileResourceFilter;
import com.wizzdi.flexicore.file.request.ZipFileToFileResourceUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.BasicService;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.metamodel.SingularAttribute;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Extension
@Component
public class ZipFileToFileResourceService implements Plugin {

	private static final Logger logger= LoggerFactory.getLogger(ZipFileToFileResourceService.class);

	@Autowired
	private ZipFileToFileResourceRepository zipFileToFileResourceRepository;
	@Autowired
	private BasicService basicService;


	public ZipFileToFileResource createZipFileToFileResource(ZipFileToFileResourceCreate zipFileToFileResourceCreate, SecurityContext securityContext) {
		ZipFileToFileResource zipFileToFileResource = createZipFileToFileResourceNoMerge(zipFileToFileResourceCreate, securityContext);
		zipFileToFileResourceRepository.merge(zipFileToFileResource);
		return zipFileToFileResource;
	}



	public ZipFileToFileResource createZipFileToFileResourceNoMerge(ZipFileToFileResourceCreate zipFileToFileResourceCreate, SecurityContext securityContext) {
		ZipFileToFileResource zipFileToFileResource = new ZipFileToFileResource();
		zipFileToFileResource.setId(UUID.randomUUID().toString());
		updateZipFileToFileResourceNoMerge(zipFileToFileResourceCreate, zipFileToFileResource);
		return zipFileToFileResource;
	}

	public boolean updateZipFileToFileResourceNoMerge(ZipFileToFileResourceCreate zipFileToFileResourceCreate, ZipFileToFileResource zipFileToFileResource) {
		boolean update = basicService.updateBasicNoMerge(zipFileToFileResourceCreate, zipFileToFileResource);
		if(zipFileToFileResourceCreate.getFileResource()!=null&&(zipFileToFileResource.getZippedFile()==null||!zipFileToFileResourceCreate.getFileResource().getId().equals(zipFileToFileResource.getZippedFile().getId()))){
			zipFileToFileResource.setZippedFile(zipFileToFileResourceCreate.getFileResource());
			update=true;
		}
		if(zipFileToFileResourceCreate.getZipFile()!=null&&(zipFileToFileResource.getZipFile()==null||!zipFileToFileResourceCreate.getZipFile().getId().equals(zipFileToFileResource.getZipFile().getId()))){
			zipFileToFileResource.setZipFile(zipFileToFileResourceCreate.getZipFile());
			update=true;
		}
		return update;
	}

	public ZipFileToFileResource updateZipFileToFileResource(ZipFileToFileResourceUpdate zipFileToFileResourceUpdate, SecurityContext securityContext) {
		ZipFileToFileResource ZipFileToFileResource = zipFileToFileResourceUpdate.getZipFileToFileResource();
		if (updateZipFileToFileResourceNoMerge(zipFileToFileResourceUpdate, ZipFileToFileResource)) {
			zipFileToFileResourceRepository.merge(ZipFileToFileResource);
		}
		return ZipFileToFileResource;
	}


	public void validate(ZipFileToFileResourceCreate zipFileToFileResourceCreate, SecurityContext securityContext) {

	}



	public void validate(ZipFileToFileResourceFilter zipFileToFileResourceFilter, SecurityContext securityContext) {


	}



	public PaginationResponse<ZipFileToFileResource> getAllZipFileToFileResources(ZipFileToFileResourceFilter ZipFileToFileResourceFilter, SecurityContext securityContext) {
		List<ZipFileToFileResource> list = listAllZipFileToFileResources(ZipFileToFileResourceFilter, securityContext);
		long count = zipFileToFileResourceRepository.countAllZipFileToFileResources(ZipFileToFileResourceFilter, securityContext);
		return new PaginationResponse<>(list, ZipFileToFileResourceFilter, count);
	}

	public List<ZipFileToFileResource> listAllZipFileToFileResources(ZipFileToFileResourceFilter ZipFileToFileResourceFilter, SecurityContext securityContext) {
		return zipFileToFileResourceRepository.listAllZipFileToFileResources(ZipFileToFileResourceFilter, securityContext);
	}

	public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
		return zipFileToFileResourceRepository.findByIds(c, ids, idAttribute);
	}

	public <T extends Basic> List<T> findByIds(Class<T> c, Set<String> requested) {
		return zipFileToFileResourceRepository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return zipFileToFileResourceRepository.findByIdOrNull(type, id);
	}

	public void merge(Object base) {
		zipFileToFileResourceRepository.merge(base);
	}

	public void merge(Object base, boolean updateDate) {
		zipFileToFileResourceRepository.merge(base, updateDate);
	}

	public void massMerge(List<?> toMerge) {
		zipFileToFileResourceRepository.massMerge(toMerge);
	}

	public void massMerge(List<?> toMerge, boolean updatedate) {
		zipFileToFileResourceRepository.massMerge(toMerge, updatedate);
	}
}
