package com.wizzdi.flexicore.file.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Basic;
import com.wizzdi.flexicore.security.configuration.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.file.data.ZipFileRepository;
import com.wizzdi.flexicore.file.model.FileResource;
import com.wizzdi.flexicore.file.model.ZipFile;
import com.wizzdi.flexicore.file.model.ZipFileToFileResource;
import com.wizzdi.flexicore.file.request.*;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.BaseclassService;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.zeroturnaround.zip.ZipUtil;

import jakarta.persistence.metamodel.SingularAttribute;
import java.io.File;
import java.nio.charset.StandardCharsets;
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


	public ZipFile createZipFile(ZipFileCreate zipFileCreate, SecurityContext securityContext) {
		ZipFile zipFile = createZipFileNoMerge(zipFileCreate, securityContext);
		Baseclass security= BaseclassService.createSecurityObjectNoMerge(zipFile, securityContext);
		massMerge(Arrays.asList(zipFile,security));
		return zipFile;
	}

	public void merge(Object o) {
		zipFileRepository.merge(o);
	}

	public void massMerge(List<Object> list) {
		zipFileRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
		return zipFileRepository.listByIds(c, ids, securityContext);
	}

	public ZipFile createZipFileNoMerge(ZipFileCreate zipFileCreate, SecurityContext securityContext) {
		ZipFile zipFile = new ZipFile();
		zipFile.setId(UUID.randomUUID().toString());
		updateZipFileNoMerge(zipFileCreate, zipFile);
		BaseclassService.createSecurityObjectNoMerge(zipFile, securityContext);
		return zipFile;
	}

	public boolean updateZipFileNoMerge(ZipFileCreate zipFileCreate, ZipFile zipFile) {
		boolean update = fileResourceService.updateFileResourceNoMerge(zipFileCreate, zipFile);
		if(zipFileCreate.getUniqueFilesMd5()!=null&&!zipFileCreate.getUniqueFilesMd5().equals(zipFile.getUniqueFilesMd5())){
			zipFile.setUniqueFilesMd5(zipFileCreate.getUniqueFilesMd5());
			update=true;
		}
		return update;
	}

	public ZipFile updateZipFile(ZipFileUpdate zipFileUpdate, SecurityContext securityContext) {
		ZipFile ZipFile = zipFileUpdate.getZipFile();
		if (updateZipFileNoMerge(zipFileUpdate, ZipFile)) {
			zipFileRepository.merge(ZipFile);
		}
		return ZipFile;
	}

	public String calculateUniqueFilesMd5(List<FileResource> files){
		Map<String,FileResource> map=files.stream().collect(Collectors.toMap(f->f.getId(),f->f,(a,b)->a));
		String raw=map.values().stream().map(f->f.getId()).sorted().collect(Collectors.joining(","));
		return md5Service.generateMD5(raw.getBytes(StandardCharsets.UTF_8));
	}

	public void validate(ZipAndDownloadRequest zipAndDownloadRequest, SecurityContext securityContext) {
		Set<String> fileResourceIds = zipAndDownloadRequest.getFileResourceIds();
		Map<String, FileResource> fileResourceMap = fileResourceIds.isEmpty() ? new HashMap<>() : zipFileRepository.listByIds(FileResource.class, fileResourceIds, securityContext).parallelStream().collect(Collectors.toMap(f -> f.getId(), f -> f));
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
		if(fileResourceMap.isEmpty()){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"must contain atleast one file");
		}

	}

	public void validate(ZipFileCreate zipFileCreate, SecurityContext securityContext) {
		fileResourceService.validate(zipFileCreate, securityContext);

	}



	public void validate(ZipFileFilter zipFileFilter, SecurityContext securityContext) {
		fileResourceService.validate(zipFileFilter, securityContext);


	}

	public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContext securityContext) {
		return zipFileRepository.getByIdOrNull(id, c, baseclassAttribute, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContext securityContext) {
		return zipFileRepository.listByIds(c, ids, baseclassAttribute, securityContext);
	}

	public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
		return zipFileRepository.findByIds(c, ids, idAttribute);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return zipFileRepository.findByIdOrNull(type, id);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
		return zipFileRepository.getByIdOrNull(id, c, securityContext);
	}

	public PaginationResponse<ZipFile> getAllZipFiles(ZipFileFilter ZipFileFilter, SecurityContext securityContext) {
		List<ZipFile> list = listAllZipFiles(ZipFileFilter, securityContext);
		long count = zipFileRepository.countAllZipFiles(ZipFileFilter, securityContext);
		return new PaginationResponse<>(list, ZipFileFilter, count);
	}

	public List<ZipFile> listAllZipFiles(ZipFileFilter ZipFileFilter, SecurityContext securityContext) {
		return zipFileRepository.listAllZipFiles(ZipFileFilter, securityContext);
	}

	public <T extends Baseclass> List<T> findByIds(Class<T> c, Set<String> requested) {
		return zipFileRepository.findByIds(c, requested);
	}



	public ZipFile zipAndDownload(ZipAndDownloadRequest zipAndDownload, SecurityContext securityContext) {
		String filesMd5 = calculateUniqueFilesMd5(zipAndDownload.getFileResources());
		ZipFile existing = getExistingZipFile(filesMd5, securityContext);
		if (existing == null) {
			List<Object> toMerge = new ArrayList<>();
			List<File> files = zipAndDownload.getFileResources().parallelStream().map(f -> new File(f.getFullPath())).collect(Collectors.toList());
			File[] arr = new File[files.size()];
			files.toArray(arr);
			File zip = new File(fileResourceService.generateNewPathForFileResource("zip", securityContext.getUser()) + ".zip");
			ZipUtil.packEntries(arr, zip);
			ZipFileCreate zipFileCreate = new ZipFileCreate()
					.setUniqueFilesMd5(filesMd5)
					.setOriginalFilename(zip.getName())
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

	private ZipFile getExistingZipFile(String md5, SecurityContext securityContext) {
		return listAllZipFiles(new ZipFileFilter().setUniqueFilesMd5(md5),securityContext).stream().filter(f -> f.getFullPath() != null && new File(f.getFullPath()).exists()).findFirst().orElse(null);

	}

	public void calculateZipsMd5(ZipFileFilter zipFileFilter, SecurityContext securityContext) {
		List<ZipFile> zipFiles = listAllZipFiles(zipFileFilter, securityContext);
		Map<String,List<ZipFileToFileResource>> map=zipFiles.isEmpty()?new HashMap<>():zipFileToFileResourceService.listAllZipFileToFileResources(new ZipFileToFileResourceFilter().setZipFiles(zipFiles),null).stream().collect(Collectors.groupingBy(f->f.getZipFile().getId()));
		for (ZipFile zipFile : zipFiles) {
			List<ZipFileToFileResource> zipFileToFileResources=map.get(zipFile.getId());
			if(zipFileToFileResources==null){
				continue;
			}
			List<FileResource> files=zipFileToFileResources.stream().map(f->f.getZippedFile()).collect(Collectors.toList());
			String md5=calculateUniqueFilesMd5(files);
			updateZipFile(new ZipFileUpdate().setZipFile(zipFile).setUniqueFilesMd5(md5),securityContext);
		}
	}
}
