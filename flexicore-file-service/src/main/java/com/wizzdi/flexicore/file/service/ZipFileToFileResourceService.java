package com.wizzdi.flexicore.file.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Basic;
import com.flexicore.security.SecurityContextBase;
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
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.metamodel.SingularAttribute;
import java.util.List;
import java.util.Set;

@Extension
@Component
public class ZipFileToFileResourceService implements Plugin {

	private static final Logger logger= LoggerFactory.getLogger(ZipFileToFileResourceService.class);

	@Autowired
	private ZipFileToFileResourceRepository zipFileToFileResourceRepository;
	@Autowired
	private BasicService basicService;


	public ZipFileToFileResource createZipFileToFileResource(ZipFileToFileResourceCreate zipFileToFileResourceCreate, SecurityContextBase securityContextBase) {
		ZipFileToFileResource zipFileToFileResource = createZipFileToFileResourceNoMerge(zipFileToFileResourceCreate, securityContextBase);
		zipFileToFileResourceRepository.merge(zipFileToFileResource);
		return zipFileToFileResource;
	}



	public ZipFileToFileResource createZipFileToFileResourceNoMerge(ZipFileToFileResourceCreate zipFileToFileResourceCreate, SecurityContextBase securityContextBase) {
		ZipFileToFileResource zipFileToFileResource = new ZipFileToFileResource();
		zipFileToFileResource.setId(Baseclass.getBase64ID());
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

	public ZipFileToFileResource updateZipFileToFileResource(ZipFileToFileResourceUpdate zipFileToFileResourceUpdate, SecurityContextBase securityContextBase) {
		ZipFileToFileResource ZipFileToFileResource = zipFileToFileResourceUpdate.getZipFileToFileResource();
		if (updateZipFileToFileResourceNoMerge(zipFileToFileResourceUpdate, ZipFileToFileResource)) {
			zipFileToFileResourceRepository.merge(ZipFileToFileResource);
		}
		return ZipFileToFileResource;
	}


	public void validate(ZipFileToFileResourceCreate zipFileToFileResourceCreate, SecurityContextBase securityContextBase) {
		basicService.validate(zipFileToFileResourceCreate,securityContextBase);

	}



	public void validate(ZipFileToFileResourceFilter zipFileToFileResourceFilter, SecurityContextBase securityContextBase) {
		basicService.validate(zipFileToFileResourceFilter, securityContextBase);


	}



	public PaginationResponse<ZipFileToFileResource> getAllZipFileToFileResources(ZipFileToFileResourceFilter ZipFileToFileResourceFilter, SecurityContextBase securityContextBase) {
		List<ZipFileToFileResource> list = listAllZipFileToFileResources(ZipFileToFileResourceFilter, securityContextBase);
		long count = zipFileToFileResourceRepository.countAllZipFileToFileResources(ZipFileToFileResourceFilter, securityContextBase);
		return new PaginationResponse<>(list, ZipFileToFileResourceFilter, count);
	}

	public List<ZipFileToFileResource> listAllZipFileToFileResources(ZipFileToFileResourceFilter ZipFileToFileResourceFilter, SecurityContextBase securityContextBase) {
		return zipFileToFileResourceRepository.listAllZipFileToFileResources(ZipFileToFileResourceFilter, securityContextBase);
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

	@Transactional
	public void merge(Object base) {
		zipFileToFileResourceRepository.merge(base);
	}

	@Transactional
	public void merge(Object base, boolean updateDate) {
		zipFileToFileResourceRepository.merge(base, updateDate);
	}

	@Transactional
	public void massMerge(List<?> toMerge) {
		zipFileToFileResourceRepository.massMerge(toMerge);
	}

	@Transactional
	public void massMerge(List<?> toMerge, boolean updatedate) {
		zipFileToFileResourceRepository.massMerge(toMerge, updatedate);
	}
}
