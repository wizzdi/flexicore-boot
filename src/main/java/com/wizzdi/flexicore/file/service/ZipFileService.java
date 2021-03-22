package com.wizzdi.flexicore.file.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Basic;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.file.data.ZipFileRepository;
import com.wizzdi.flexicore.file.model.FileResource;
import com.wizzdi.flexicore.file.model.FileResource_;
import com.wizzdi.flexicore.file.model.ZipFile;
import com.wizzdi.flexicore.file.model.ZipFileToFileResource;
import com.wizzdi.flexicore.file.request.*;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.BasicService;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.zeroturnaround.zip.ZipUtil;

import javax.persistence.metamodel.SingularAttribute;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Extension
@Component
public class ZipFileService implements Plugin {

	private static final Logger logger= LoggerFactory.getLogger(ZipFileService.class);

	@Autowired
	private ZipFileRepository zipFileRepository;
	@Autowired
	private MD5Service md5Service;
	@Autowired
	private ZipFileToFileResourceService zipFileToFileResourceService;

	@Autowired
	private FileResourceService fileResourceService;


	public ZipFile createZipFile(ZipFileCreate zipFileCreate, SecurityContextBase securityContextBase) {
		ZipFile zipFile = createZipFileNoMerge(zipFileCreate, securityContextBase);
		Baseclass security=new Baseclass(zipFile.getName(),securityContextBase);
		zipFile.setSecurity(security);
		massMerge(Arrays.asList(zipFile,security));
		return zipFile;
	}

	public void merge(Object o) {
		zipFileRepository.merge(o);
	}

	public void massMerge(List<Object> list) {
		zipFileRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContextBase securityContextBase) {
		return zipFileRepository.listByIds(c, ids, securityContextBase);
	}

	public ZipFile createZipFileNoMerge(ZipFileCreate zipFileCreate, SecurityContextBase securityContextBase) {
		ZipFile zipFile = new ZipFile();
		zipFile.setId(Baseclass.getBase64ID());
		updateZipFileNoMerge(zipFileCreate, zipFile);
		return zipFile;
	}

	public boolean updateZipFileNoMerge(ZipFileCreate zipFileCreate, ZipFile zipFile) {
		boolean update = fileResourceService.updateFileResourceNoMerge(zipFileCreate, zipFile);
		return update;
	}

	public ZipFile updateZipFile(ZipFileUpdate zipFileUpdate, SecurityContextBase securityContextBase) {
		ZipFile ZipFile = zipFileUpdate.getZipFile();
		if (updateZipFileNoMerge(zipFileUpdate, ZipFile)) {
			zipFileRepository.merge(ZipFile);
		}
		return ZipFile;
	}

	public void validate(ZipAndDownloadRequest zipAndDownloadRequest, SecurityContextBase securityContext) {
		Set<String> fileResourceIds = zipAndDownloadRequest.getFileResourceIds();
		Map<String, FileResource> fileResourceMap = fileResourceIds.isEmpty() ? new HashMap<>() : zipFileRepository.listByIds(FileResource.class, fileResourceIds, FileResource_.security,securityContext).parallelStream().collect(Collectors.toMap(f -> f.getId(), f -> f));
		fileResourceIds.removeAll(fileResourceMap.keySet());
		if (!fileResourceIds.isEmpty()) {
			String message = "No FileResources With ids " + fileResourceIds;
			if (zipAndDownloadRequest.isFailOnMissing() || fileResourceMap.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,message);
			} else {
				logger.warn(message);
			}
		}
		zipAndDownloadRequest.setFileResources(new ArrayList<>(fileResourceMap.values()));

	}

	public void validate(ZipFileCreate zipFileCreate, SecurityContextBase securityContextBase) {
		fileResourceService.validate(zipFileCreate,securityContextBase);

	}



	public void validate(ZipFileFilter zipFileFilter, SecurityContextBase securityContextBase) {
		fileResourceService.validate(zipFileFilter, securityContextBase);


	}

	public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContextBase) {
		return zipFileRepository.getByIdOrNull(id, c, baseclassAttribute, securityContextBase);
	}

	public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContextBase) {
		return zipFileRepository.listByIds(c, ids, baseclassAttribute, securityContextBase);
	}

	public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
		return zipFileRepository.findByIds(c, ids, idAttribute);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return zipFileRepository.findByIdOrNull(type, id);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContextBase) {
		return zipFileRepository.getByIdOrNull(id, c, securityContextBase);
	}

	public PaginationResponse<ZipFile> getAllZipFiles(ZipFileFilter ZipFileFilter, SecurityContextBase securityContextBase) {
		List<ZipFile> list = listAllZipFiles(ZipFileFilter, securityContextBase);
		long count = zipFileRepository.countAllZipFiles(ZipFileFilter, securityContextBase);
		return new PaginationResponse<>(list, ZipFileFilter, count);
	}

	public List<ZipFile> listAllZipFiles(ZipFileFilter ZipFileFilter, SecurityContextBase securityContextBase) {
		return zipFileRepository.listAllZipFiles(ZipFileFilter, securityContextBase);
	}

	public <T extends Baseclass> List<T> findByIds(Class<T> c, Set<String> requested) {
		return zipFileRepository.findByIds(c, requested);
	}



	public ZipFile zipAndDownload(ZipAndDownloadRequest zipAndDownload, SecurityContextBase securityContext) {

		ZipFile existing = getExistingZipFile(zipAndDownload.getFileResources(), securityContext);
		if (existing == null) {
			List<Object> toMerge = new ArrayList<>();
			List<File> files = zipAndDownload.getFileResources().parallelStream().map(f -> new File(f.getFullPath())).collect(Collectors.toList());
			File[] arr = new File[files.size()];
			files.toArray(arr);
			File zip = new File(fileResourceService.generateNewPathForFileResource("zip", securityContext.getUser()) + ".zip");
			ZipUtil.packEntries(arr, zip);
			ZipFileCreate zipFileCreate = new ZipFileCreate().setOriginalFilename(zip.getName())
					.setFullPath(zip.getAbsolutePath())
					.setMd5(md5Service.generateMD5(zip))
					.setOffset(zip.length());
			ZipFile zipFile = createZipFileNoMerge(zipFileCreate,securityContext);
			toMerge.add(zipFile);
			List<ZipFileToFileResource> links = zipAndDownload.getFileResources().parallelStream().map(f -> zipFileToFileResourceService.createZipFileToFileResourceNoMerge(new ZipFileToFileResourceCreate().setFileResource(f).setZipFile(zipFile), securityContext)).collect(Collectors.toList());
			toMerge.addAll(links);
			zipFileRepository.massMerge(toMerge);
			existing = zipFile;

		}
		return existing;

	}

	private ZipFile getExistingZipFile(List<FileResource> fileResources, SecurityContextBase securityContext) {
		Set<String> requiredIds = fileResources.parallelStream().map(f -> f.getId()).collect(Collectors.toSet());
		List<ZipFileToFileResource> links = zipFileToFileResourceService.listAllZipFileToFileResources(new ZipFileToFileResourceFilter().setFileResources(fileResources), securityContext);
		Map<String, ZipFile> zipFileMap = links.parallelStream().map(f -> f.getZipFile()).filter(f -> f.getFullPath() != null && new File(f.getFullPath()).exists()).collect(Collectors.toMap(f -> f.getId(), f -> f, (a, b) -> a));
		Map<String, Set<String>> zipFileToFileResource = links.parallelStream().collect(Collectors.groupingBy(f -> f.getZipFile().getId(), Collectors.mapping(f -> f.getZippedFile().getId(), Collectors.toSet())));
		for (Map.Entry<String, Set<String>> stringSetEntry : zipFileToFileResource.entrySet()) {
			if (stringSetEntry.getValue().containsAll(requiredIds) && requiredIds.containsAll(stringSetEntry.getValue())) {
				return zipFileMap.get(stringSetEntry.getKey());
			}
		}
		return null;
	}

}
