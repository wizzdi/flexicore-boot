/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.service.impl;

import com.flexicore.annotations.IOperation;
import com.flexicore.data.BaselinkRepository;
import com.flexicore.data.FileResourceRepository;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.AnalyzerPlugin;
import com.flexicore.model.*;
import com.flexicore.request.*;
import com.flexicore.response.FinalizeFileResourceResponse;
import com.flexicore.rest.DownloadRESTService;
import com.flexicore.security.SecurityContext;
import com.wizzdi.flexicore.file.model.FileResource;
import com.wizzdi.flexicore.file.model.FileResource_;
import com.wizzdi.flexicore.file.model.ZipFile;
import com.wizzdi.flexicore.file.model.ZipFileToFileResource;
import com.wizzdi.flexicore.file.request.ZipFileToFileResourceCreate;
import com.wizzdi.flexicore.file.service.MD5Service;
import com.wizzdi.flexicore.file.service.ZipFileService;
import com.wizzdi.flexicore.file.service.ZipFileToFileResourceService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.input.BoundedInputStream;
import org.jboss.resteasy.spi.HttpResponseCodes;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import jakarta.activation.MimetypesFileTypeMap;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.core.Response;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Primary
@Component("fileResourceServiceOld")
@Extension
public class FileResourceService implements com.flexicore.service.FileResourceService {
	/**
	 *
	 */
	private static final long serialVersionUID = -2975140337610352781L;
	private static final int MAX_FILE_PART_SIZE = 2 * 1024 * 1024;

	//private String folder = "c:/temp/";

	private static final Logger logger = LoggerFactory.getLogger(FileResourceService.class);

	@Autowired
	private FileResourceRepository fileResourceRepository;

	@Autowired
	private com.wizzdi.flexicore.file.service.FileResourceService fileResourceService;
	@Autowired
	private MD5Service md5Service;
	@Autowired
	private ZipFileService zipFileService;
	@Autowired
	private ZipFileToFileResourceService zipFileToFileResourceService;

	@Autowired
	private BaselinkRepository baselinkRepository;

	@Autowired(required = false)
	@Lazy
	private JobService jobService;

	@Value("${flexicore.upload:/home/flexicore/upload}")
	private String uploadPath;

	public FileResource getExistingFileResource(String md5, SecurityContext securityContext) {
		List<FileResource> existing = fileResourceRepository.listAllFileResources(new FileResourceFilter().setMd5s(Collections.singleton(md5)), securityContext);
		return existing.isEmpty() ? null : existing.get(0);
	}

	public FileResource uploadFileResource(String filename, SecurityContext securityContext, String md5, String chunkMd5, boolean lastChunk, InputStream fileInputStream) {
		try {
			FileResource fileResource = fileResourceService.uploadFileResource(filename, securityContext, md5, chunkMd5, lastChunk, fileInputStream);
			return fileResource;
		}
		catch (ResponseStatusException responseStatusException){
			throw new ClientErrorException(responseStatusException.getReason(),Response.Status.fromStatusCode(responseStatusException.getStatusCode().value()));
		}


	}


	public List<FileResource> getFileResourceScheduledForDelete(OffsetDateTime date) {
		return fileResourceRepository.getFileResourceScheduledForDelete(date);
	}

	/**
	 * finalize upload, starts a new FC Job which invokes a BatchRuntime
	 * Job. The created FC Job allows for a client tracking on {@link Job}
	 * progress.
	 *
	 * @param md5             md5 of the file to be finizalized
	 * @param securityContext security context
	 * @param hint            hint for finalizing
	 * @return job that was finalized
	 */

	public Job finalizeUpload(String md5, SecurityContext securityContext, String hint) {
		return finalizeUpload(md5, securityContext, hint, null);
	}

	private void createSecurityLinkForFileResource(SecurityContext securityContext, FileResource fileResource) {
		try {
			UserToBaseclass utb = new UserToBaseclass("userToBaseclass", securityContext);
			utb.setLeftside(securityContext.getUser());
			utb.setBaseclass(fileResource.getSecurity());
			String download = Baseclass.generateUUIDFromString
					(DownloadRESTService.class.getMethod("download", String.class, long.class, long.class, String.class, HttpServletRequest.class, SecurityContext.class).toString());
			Operation op = baselinkRepository.findById(Operation.class, download);

			utb.setValue(op);
			utb.setSimplevalue(IOperation.Access.allow.name());
			merge(utb);
		} catch (NoSuchMethodException | SecurityException e) {
			logger.error("unable to create security link for fileResource: " + fileResource, e);
		}
	}


	public FinalizeFileResourceResponse finalize(FinallizeFileResource finallizeFileResource, SecurityContext securityContext) {
		FileResource fileResource = finallizeFileResource.getFileResource();
		String md5 = fileResource.getMd5();
		if (!fileResource.getSecurity().getCreator().getId().equals(securityContext.getUser().getId())) {
			createSecurityLinkForFileResource(securityContext, fileResource);

		}
		File file = new File(fileResource.getFullPath());
		String actualMd5 = generateMD5(file);
		if (!md5.equals(actualMd5)) {
			file.delete();
			return new FinalizeFileResourceResponse().setExpectedMd5(md5).setMd5(actualMd5).setFileResource(fileResource).setFinalized(false);
		} else {
			fileResource.setDone(true);
		}
		return new FinalizeFileResourceResponse().setExpectedMd5(md5).setMd5(md5).setFileResource(fileResource).setFinalized(true);
	}

	public Job finalizeUpload(String md5, SecurityContext securityContext, String hint, Properties prop) {
		Job job = null;

		FileResource fileResource = getExistingFileResource(md5, securityContext);


		if (fileResource != null) {
			if (!fileResource.getSecurity().getCreator().getId().equals(securityContext.getUser().getId())) {
				createSecurityLinkForFileResource(securityContext, fileResource);

			}
			File file = new File(fileResource.getFullPath());
			String actualMd5 = generateMD5(file);
			if (!md5.equals(actualMd5)) {
				file.delete();
				throw new BadRequestException("file " + file.getAbsolutePath() + " failed md5 check , actual md5: " + actualMd5 + " expected: " + md5 + ", file has been deleted , please upload again");
			}
			fileResource.setDone(true);
			if (prop != null && Boolean.parseBoolean(prop.getProperty("dontProcess", "false"))) {
				return null;
			}

			JobInformation info = new JobInformation();
			info.setJobInfo(fileResource);
			info.setHandle(true); // tells the PI system to read the next
			// Cycle.
			info.setHandler(AnalyzerPlugin.class); // the first PI to run
			// (or multiple of) will// be an Analyzer PI
			info.setJobProperties(prop);
			if (prop == null) {
				prop = new Properties();
			}

			if (hint != null && !hint.isEmpty()) {
				prop.setProperty("hint", hint);
			}
			job = jobService.startJob(fileResource, AnalyzerPlugin.class, prop, null, securityContext);


			merge(fileResource);


		} else {
			logger.error("No file resource found for MD5:  " + md5);
			throw new ClientErrorException("the MD5 on finalize was not found in the database", Response.Status.BAD_REQUEST);

		}
		return job;
	}

	@Override
	public void saveFile(InputStream is, FileResource file) {
		saveFile(is, null, file);
	}

	@Override
	public void saveFile(InputStream is, String chunkMd5, FileResource file) {
		fileResourceService.saveFile(is, chunkMd5, file);
	}


	@Override
	public boolean saveFile(byte[] data, long offsetInFile, FileResource file) {
		return fileResourceService.saveFile(data, offsetInFile, file);
	}


	@Override
	public FileResource create(String pathToFileResource, SecurityContext securityContext) {
		FileResource fileResource = null;
		File file = new File(pathToFileResource);
		if (file.exists()) {
			String md5 = file.isFile() ? generateMD5(file) : null;
			String fileName = file.getName();
			String ext = fileName.endsWith("tar.gz") ? "tar.gz" : FilenameUtils.getExtension(fileName);
			String actualFilename = !ext.isEmpty() ? UUID.randomUUID().toString() + "." + ext : UUID.randomUUID().toString();
			FileResourceCreate fileResourceCreate = new FileResourceCreate()
					.setFullPath(pathToFileResource)
					.setMd5(md5)
					.setOffset(0L)
					.setActualFilename(actualFilename)
					.setOriginalFilename(fileName)
					.setName(fileName);
			fileResource = createFileResource(fileResourceCreate, securityContext);
		}
		return fileResource;

	}

	@Override
	public FileResource createFileResource(FileResourceCreate fileResourceCreate, SecurityContext securityContext) {
		FileResource fileResource = createNoMerge(fileResourceCreate, securityContext);
		merge(fileResource);
		return fileResource;
	}

	@Override
	public void persist(Object o) {
		fileResourceRepository.persist(o);
	}

	@Override
	public FileResource createDontPersist(String pathToFileResource, SecurityContext securityContext) {
		File file = new File(pathToFileResource);
		String filename = file.getName();
		String ext = filename.endsWith("tar.gz") ? "tar.gz" : FilenameUtils.getExtension(filename);
		String md5 = generateMD5(pathToFileResource);

		FileResourceCreate fileResourceCreate = new FileResourceCreate()
				.setFullPath(pathToFileResource)
				.setMd5(md5)
				.setOffset(0L)
				.setActualFilename(UUID.randomUUID().toString() + "." + ext)
				.setOriginalFilename(filename)
				.setName(filename);
		return createNoMerge(fileResourceCreate, securityContext);
	}


	@Override
	public FileResource createNoMerge(FileResourceCreate fileResourceCreate, SecurityContext securityContext) {
		return fileResourceService.createFileResourceNoMerge(getCompatible(fileResourceCreate), securityContext);
	}

	@Override
	public boolean updateFileResourceNoMerge(FileResourceCreate fileResourceCreate, FileResource fileResource) {


		return fileResourceService.updateFileResourceNoMerge(getCompatible(fileResourceCreate), fileResource);

	}

	private com.wizzdi.flexicore.file.request.FileResourceCreate getCompatible(FileResourceCreate fileResourceCreate) {
		return new com.wizzdi.flexicore.file.request.FileResourceCreate()
				.setFullPath(fileResourceCreate.getFullPath())
				.setMd5(fileResourceCreate.getMd5())
				.setOffset(fileResourceCreate.getOffset())
				.setOriginalFilename(fileResourceCreate.getOriginalFilename())
				.setActualFilename(fileResourceCreate.getActualFilename())
				.setName(fileResourceCreate.getName())
				.setDescription(fileResourceCreate.getDescription())
				.setSoftDelete(fileResourceCreate.getSoftDelete());
	}


	@Override
	public String generateMD5(InputStream is) {
		return md5Service.generateMD5(is);
	}

	@Override
	public String generateMD5(String filePath) {
		return md5Service.generateMD5(filePath);
	}


	@Override
	public String generateMD5(File file) {
		return md5Service.generateMD5(file);

	}

	@Override
	public void merge(Object o) {
		fileResourceRepository.merge(o);
	}


	public FileResource getFileResource(String id, SecurityContext securityContext) {
		return fileResourceRepository.getByIdOrNull(id, FileResource.class, FileResource_.security, securityContext);
	}


	@Override
	public PaginationResponse<FileResource> getAllFileResources(FileResourceFilter fileResourceFilter, SecurityContext securityContext) {
		List<FileResource> fileResources = listAllFileResources(fileResourceFilter, securityContext);
		long count = fileResourceRepository.countAllFileResources(fileResourceFilter, securityContext);
		return new PaginationResponse<>(fileResources, fileResourceFilter, count);
	}

	@Override
	public List<FileResource> listAllFileResources(FileResourceFilter fileResourceFilter, SecurityContext securityContext) {
		return fileResourceRepository.listAllFileResources(fileResourceFilter, securityContext);
	}

	public void deleteFileResource(FileResource fr, User user, List<Tenant> tenant) {
		// TODO Auto-generated method stub

	}

	public List<FileResource> validate(SecurityContext securityContext) {
		List<FileResource> nonValidFiles = new ArrayList<>();
		List<FileResource> list = fileResourceRepository
				.listAllFileResources(new FileResourceFilter(), securityContext);
		for (FileResource fileResource : list) {
			String path = fileResource.getFullPath();
			if (path != null && !path.isEmpty() && !new File(path).exists()) {
				nonValidFiles.add(fileResource);
			}

		}
		return nonValidFiles;
	}

	@Override
	public FileResource registerFile(String path, boolean calculateMd5, SecurityContext securityContext) throws FileNotFoundException {
		return registerFile(path, -1, calculateMd5, securityContext);
	}


	public FileResource registerFile(String path, long dateTaken, boolean calculateMd5, SecurityContext securityContext) throws FileNotFoundException {
		File file = new File(path);
		if (!file.exists()) {
			throw new FileNotFoundException("file at: " + path + "was not found");
		} else {
			String name = file.getName();
			String md5 = null;
			if (calculateMd5) {
				md5 = generateMD5(file);
			}
			FileResource fileResource = null;
			if (md5 != null && !md5.isEmpty()) {
				fileResource = getExistingFileResource(md5, securityContext);

			}


			if (fileResource == null) {
				fileResource = createNoMerge(new FileResourceCreate().setName("name"), securityContext);
			} else {
				if (!fileResource.getSecurity().getCreator().getId().equals(securityContext.getUser().getId())) {
					createSecurityLinkForFileResource(securityContext, fileResource);
				}

			}

			fileResource.setName(name);
			fileResource.setActualFilename(name);
			fileResource.setFullPath(path);
			fileResource.setDone(true);
			fileResource.setMd5(md5);
			if (dateTaken > 0) {

				fileResource.setDateTaken(OffsetDateTime.ofInstant(Instant.ofEpochMilli(dateTaken), ZoneId.of("UTC")));
			}

			fileResourceRepository.merge(fileResource);


			return fileResource;

		}

	}


	@Override
	public void massMerge(List<?> resources) {
		fileResourceRepository.massMerge(resources);
	}

	@Override
	public void refrehEntityManager() {

		baselinkRepository.refrehEntityManager();
	}

	@Override
	public void validate(FinallizeFileResource finallizeFileResource, SecurityContext securityContext) {
		FileResource fileResource = finallizeFileResource.getFileResourceId() != null ? fileResourceRepository.getByIdOrNull(finallizeFileResource.getFileResourceId(), FileResource.class, FileResource_.security, securityContext) : null;
		if (fileResource == null) {
			throw new BadRequestException("No FileResource with id " + finallizeFileResource.getFileResourceId());
		}
		finallizeFileResource.setFileResource(fileResource);

	}

	@Override
	public void validate(ZipAndDownloadRequest zipAndDownloadRequest, SecurityContext securityContext) {
		Set<String> fileResourceIds = zipAndDownloadRequest.getFileResourceIds();
		Map<String, FileResource> fileResourceMap = fileResourceIds.isEmpty() ? new HashMap<>() : fileResourceRepository.listByIds(FileResource.class, fileResourceIds, FileResource_.security, securityContext).parallelStream().collect(Collectors.toMap(f -> f.getId(), f -> f));
		fileResourceIds.removeAll(fileResourceMap.keySet());
		if (!fileResourceIds.isEmpty()) {
			String message = "No FileResources With ids " + fileResourceIds;
			if (zipAndDownloadRequest.isFailOnMissing() || fileResourceMap.isEmpty()) {
				throw new BadRequestException(message);
			} else {
				logger.warn(message);
			}
		}
		zipAndDownloadRequest.setFileResources(new ArrayList<>(fileResourceMap.values()));

	}

	@Override
	public byte[] readFilePart(File file, long offset) {
		return fileResourceService.readFilePart(file, offset);
	}

	@Override
	public List<ZipFile> listAllZipFiles(ZipFileFilter zipFileFilter, SecurityContext securityContext) {
		return zipFileService.listAllZipFiles(getCompatible(zipFileFilter), securityContext);
	}

	private com.wizzdi.flexicore.file.request.ZipFileFilter getCompatible(ZipFileFilter zipFileFilter) {
		return new com.wizzdi.flexicore.file.request.ZipFileFilter()
				.setMd5s(zipFileFilter.getMd5s())
				.setBasicPropertiesFilter(BaseclassNewService.getCompatible(zipFileFilter))
				.setCurrentPage(zipFileFilter.getCurrentPage())
				.setPageSize(zipFileFilter.getPageSize());
	}


	@Override
	public ZipFile zipAndDownload(ZipAndDownloadRequest zipAndDownload, SecurityContext securityContext) {

		return zipFileService.zipAndDownload(getCompatible(zipAndDownload), securityContext);

	}

	private com.wizzdi.flexicore.file.request.ZipAndDownloadRequest getCompatible(ZipAndDownloadRequest zipAndDownload) {
		return new com.wizzdi.flexicore.file.request.ZipAndDownloadRequest()
				.setFileResources(zipAndDownload.getFileResources())
				.setOffset(zipAndDownload.getOffset())
				.setFailOnMissing(zipAndDownload.isFailOnMissing())
				.setFileResourceIds(zipAndDownload.getFileResourceIds());
	}

	private ZipFile getExistingZipFile(List<FileResource> fileResources, SecurityContext securityContext) {
		Set<String> requiredIds = fileResources.parallelStream().map(f -> f.getId()).collect(Collectors.toSet());
		List<ZipFileToFileResource> links = fileResourceRepository.listZipFileToFileResource(new ZipFileToFileResourceFilter().setFileResources(fileResources), securityContext);
		Map<String, ZipFile> zipFileMap = links.parallelStream().map(f -> f.getZipFile()).filter(f -> f.getFullPath() != null && new File(f.getFullPath()).exists()).collect(Collectors.toMap(f -> f.getId(), f -> f, (a, b) -> a));
		Map<String, Set<String>> zipFileToFileResource = links.parallelStream().collect(Collectors.groupingBy(f -> f.getZipFile().getId(), Collectors.mapping(f -> f.getZippedFile().getId(), Collectors.toSet())));
		for (Map.Entry<String, Set<String>> stringSetEntry : zipFileToFileResource.entrySet()) {
			if (stringSetEntry.getValue().containsAll(requiredIds) && requiredIds.containsAll(stringSetEntry.getValue())) {
				return zipFileMap.get(stringSetEntry.getKey());
			}
		}
		return null;
	}

	private ZipFileToFileResource createZipFileToFileResourceNoMerge(ZipFile zipFile, FileResource f, SecurityContext securityContext) {
		return zipFileToFileResourceService.createZipFileToFileResourceNoMerge(new ZipFileToFileResourceCreate().setZipFile(zipFile).setFileResource(f), securityContext);
	}

	public Response download(long offset, long size, String id, String remoteIp, SecurityContext securityContext) {
		FileResource fileResource = getFileResource(id, securityContext);
		if (fileResource == null) {
			throw new BadRequestException("No File resource with id " + id);
		}
		if (fileResource.isNonDownloadable()) {
			throw new ClientErrorException("file resource with id: " + id +
					"is not available for download", Response.Status.BAD_REQUEST);
		}
		if (fileResource.getOnlyFrom() != null) {
			Set<String> allowedIps = Stream.of(fileResource.getOnlyFrom().split(",")).collect(Collectors.toSet());
			if (!allowedIps.contains(remoteIp)) {
				throw new BadRequestException("File is not allowed to be downloaded from " + remoteIp);
			}


		}

		return prepareFileResourceForDownload(fileResource, offset, size);
	}

	public Response prepareFileResourceForDownload(FileResource fileResource, long offset, long size) {
		if (fileResource != null) {
			MimetypesFileTypeMap map = new MimetypesFileTypeMap();
			File file = new File(fileResource.getFullPath());
			String mimeType = map.getContentType(file);
			String name = fileResource.getOriginalFilename();
			if (name == null) {
				name = file.getName();
			}
			if (file.exists()) {
				long fileLength = file.length();
				if (offset >= fileLength) {
					throw new BadRequestException("received offset(" + offset + ") >= length(" + fileLength);
				}

				try {
					InputStream inputStream = new FileInputStream(file);
					inputStream.skip(offset);
					if (size > 0) {
						inputStream = new BoundedInputStream(inputStream, size);
					}
					Response.ResponseBuilder response = Response.ok(inputStream, mimeType);
					int available = inputStream.available();
					long contentLength = size > 0 ? Math.min(available, size) : available;
					response.header("Content-Length", contentLength);
					if (mimeType == null) {
						response.header("Content-Disposition", "attachment; filename=\"" + name + "\"");
					}
					response.header("fileName", name);

					return response.build();
				} catch (Exception e) {
					logger.error("failed opening file", e);
				}

			}
		}
		throw new ClientErrorException(HttpResponseCodes.SC_BAD_REQUEST);
	}
}
