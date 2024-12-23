package com.wizzdi.flexicore.file.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Basic;
import com.flexicore.model.SecurityUser;
import com.wizzdi.flexicore.security.configuration.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.file.data.FileResourceRepository;
import com.wizzdi.flexicore.file.interfaces.HomeDirProvider;
import com.wizzdi.flexicore.file.model.FileResource;
import com.wizzdi.flexicore.file.request.FileResourceCreate;
import com.wizzdi.flexicore.file.request.FileResourceFilter;
import com.wizzdi.flexicore.file.request.FileResourceUpdate;
import com.wizzdi.flexicore.security.request.BasicPropertiesFilter;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.BaseclassService;
import com.wizzdi.flexicore.security.service.BasicService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BoundedInputStream;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;
import org.springframework.web.server.ResponseStatusException;

import jakarta.activation.MimetypesFileTypeMap;
import jakarta.persistence.metamodel.SingularAttribute;
import java.io.*;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Extension
@Component
public class FileResourceService implements Plugin {

	private static final Logger logger= LoggerFactory.getLogger(FileResourceService.class);
	private static final int MAX_FILE_PART_SIZE = 2 * 1024 * 1024;

	@Value("${flexicore.upload:/home/flexicore/upload}")
	private String uploadPath;
	@Autowired
	private FileResourceRepository fileResourceRepository;
	@Autowired
	private BasicService basicService;
	@Autowired
	private ObjectProvider<HomeDirProvider> homeDirProviders;
	@Autowired
	private MD5Service md5Service;


	public FileResource createFileResource(FileResourceCreate fileResourceCreate, SecurityContext securityContext) {
		FileResource fileResource = createFileResourceNoMerge(fileResourceCreate, securityContext);
		fileResourceRepository.merge(fileResource);
		return fileResource;
	}

	public void merge(Object o) {
		fileResourceRepository.merge(o);
	}

	public void massMerge(List<Object> list) {
		fileResourceRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
		return fileResourceRepository.listByIds(c, ids, securityContext);
	}




	public String generateNewPathForFileResource(String prefix, SecurityUser creatingUser) {
		File file = generateNewFileForResource(prefix, creatingUser);
		return file.getAbsolutePath();

	}

	private File generateNewFileForResource(String prefix, SecurityUser creatingUser) {
		File home=new File(uploadPath);
		HomeDirProvider homeDirProvider = homeDirProviders.stream().filter(f -> f.getType().isAssignableFrom(creatingUser.getClass())).findFirst().orElse(null);
		if(homeDirProvider!=null){
			home= homeDirProvider.getHomeDir(creatingUser);
		}
		if (!home.exists()) {
			if(!home.mkdirs()){
				logger.warn("failed creating home dir "+home);
			}
		}
		return new File(home, prefix + "--" + UUID.randomUUID());
	}

	public FileResource createFileResourceNoMerge(FileResourceCreate fileResourceCreate, SecurityContext securityContext) {
		FileResource fileResource = new FileResource();
		fileResource.setId(UUID.randomUUID().toString());
		updateFileResourceNoMerge(fileResourceCreate, fileResource);
		BaseclassService.createSecurityObjectNoMerge(fileResource, securityContext);

		return fileResource;
	}

	public boolean updateFileResourceNoMerge(FileResourceCreate fileResourceCreate, FileResource fileResource) {
		boolean update = basicService.updateBasicNoMerge(fileResourceCreate, fileResource);
		if(fileResourceCreate.getOffset()!=null&&!fileResourceCreate.getOffset().equals(fileResource.getOffset())){
			fileResource.setOffset(fileResourceCreate.getOffset());
			update=true;
		}

		if(fileResourceCreate.getFullPath()!=null&&!fileResourceCreate.getFullPath().equals(fileResource.getFullPath())){
			fileResource.setFullPath(fileResourceCreate.getFullPath());
			update=true;
		}

		if(fileResourceCreate.getActualFilename()!=null&&!fileResourceCreate.getActualFilename().equals(fileResource.getActualFilename())){
			fileResource.setActualFilename(fileResourceCreate.getActualFilename());
			update=true;
		}
		if(fileResourceCreate.getOriginalFilename()!=null&&!fileResourceCreate.getOriginalFilename().equals(fileResource.getOriginalFilename())){
			fileResource.setOriginalFilename(fileResourceCreate.getOriginalFilename());
			update=true;
		}
		if(fileResourceCreate.getMd5()!=null&&!fileResourceCreate.getMd5().equals(fileResource.getMd5())){
			fileResource.setMd5(fileResourceCreate.getMd5());
			update=true;
		}

		return update;
	}

	public FileResource updateFileResource(FileResourceUpdate fileResourceUpdate, SecurityContext securityContext) {
		FileResource FileResource = fileResourceUpdate.getFileResource();
		if (updateFileResourceNoMerge(fileResourceUpdate, FileResource)) {
			fileResourceRepository.merge(FileResource);
		}
		return FileResource;
	}



	public void validate(FileResourceCreate fileResourceCreate, SecurityContext securityContext) {

	}



	public void validate(FileResourceFilter fileResourceFilter, SecurityContext securityContext) {


	}

	public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContext securityContext) {
		return fileResourceRepository.getByIdOrNull(id, c, baseclassAttribute, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContext securityContext) {
		return fileResourceRepository.listByIds(c, ids, baseclassAttribute, securityContext);
	}

	public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
		return fileResourceRepository.findByIds(c, ids, idAttribute);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return fileResourceRepository.findByIdOrNull(type, id);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
		return fileResourceRepository.getByIdOrNull(id, c, securityContext);
	}

	public PaginationResponse<FileResource> getAllFileResources(FileResourceFilter FileResourceFilter, SecurityContext securityContext) {
		List<FileResource> list = listAllFileResources(FileResourceFilter, securityContext);
		long count = fileResourceRepository.countAllFileResources(FileResourceFilter, securityContext);
		return new PaginationResponse<>(list, FileResourceFilter, count);
	}

	public List<FileResource> listAllFileResources(FileResourceFilter FileResourceFilter, SecurityContext securityContext) {
		return fileResourceRepository.listAllFileResources(FileResourceFilter, securityContext);
	}

	public <T extends Baseclass> List<T> findByIds(Class<T> c, Set<String> requested) {
		return fileResourceRepository.findByIds(c, requested);
	}

	public ResponseEntity<Resource> download(long offset, long size, String id, String remoteIp, SecurityContext securityContext) {
		FileResource fileResource = getFileResourceById(id, securityContext);
		if (fileResource == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No File resource with id " + id);
		}
		if (fileResource.isNonDownloadable()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"file resource with id: " + id +
					"is not available for download");
		}
		if (fileResource.getOnlyFrom() != null) {
			Set<String> allowedIps = Stream.of(fileResource.getOnlyFrom().split(",")).collect(Collectors.toSet());
			if (!allowedIps.contains(remoteIp)) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"File is not allowed to be downloaded from " + remoteIp);
			}


		}

		return prepareFileResourceForDownload(fileResource, offset, size);
	}


	public ResponseEntity<Resource> prepareFileResourceForDownload(FileResource fileResource, long offset, long size) {
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
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"received offset(" + offset + ") >= length(" + fileLength);
				}

				try {
					InputStream inputStream = new FileInputStream(file);
					inputStream.skip(offset);
					if (size > 0) {
						inputStream = BoundedInputStream.builder().setInputStream(inputStream).setMaxCount(size).get();
					}
					int available = inputStream.available();
					long contentLength = size > 0 ? Math.min(available, size) : available;
					ContentDisposition contentDisposition = ContentDisposition.builder("inline")
							.filename(name)
							.build();
					HttpHeaders httpHeaders=new HttpHeaders();
					httpHeaders.setContentLength(contentLength);
					httpHeaders.add("fileName",name);
					httpHeaders.setContentDisposition(contentDisposition);
					httpHeaders.setContentType(MediaType.asMediaType(MimeType.valueOf(mimeType)));

					return ResponseEntity.ok()
							.headers(httpHeaders)
							.body(new InputStreamResource(inputStream,name));

				} catch (IOException e) {
					logger.error("failed opening file", e);
				}

			}
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
	}

	public boolean saveFile(byte[] data, long offsetInFile, FileResource file) {
		return saveFile(data,offsetInFile,file,true);
	}

	public boolean saveFile(byte[] data, long offsetInFile, FileResource file,boolean updateOffset) {
		File f = new File(file.getFullPath());
		File parentFile = f.getParentFile();
		if (parentFile == null) {
			logger.error("unable to save file at " + file.getFullPath());
			return false;
		}
		if (!parentFile.exists()) {
			if (!parentFile.mkdirs()) {
				logger.warn("Failed Creating dir " + parentFile);
				return false;
			}
		}
		long written;

		try (RandomAccessFile fos = new RandomAccessFile(f, "rw")) {
			fos.seek(offsetInFile);
			fos.write(data);
			written = data.length;
			if(updateOffset){
				file.setOffset(offsetInFile + written);

			}


		} catch (IOException e) {
			logger.error("unable to upload , truncating file to last known good location", e);
			long orig = file.getOffset();
			long fileOffset = trimToSize(f, orig);
			if(updateOffset){
				file.setOffset(fileOffset);
			}



		} finally {
			if(updateOffset){
				fileResourceRepository.merge(file);
			}
		}
		return true;

	}

	private long trimToSize(File file, long orig) {
		try (FileChannel fc = new FileOutputStream(file, true).getChannel()) {
			fc.truncate(orig);
			return orig;
		} catch (IOException e) {
			logger.error("failed truncating file to orig", e);
		}
		return file.length();
	}

	public void saveFile(InputStream is, FileResource file) {
		saveFile(is, null, file);
	}


	public void saveFile(InputStream is, String chunkMd5, FileResource file) {
		try {
			byte[] data = IOUtils.toByteArray(is);
		if (chunkMd5 != null) {
			String calculatedChunkMd5 = md5Service.generateMD5(new ByteArrayInputStream(data));
			if (!chunkMd5.equals(calculatedChunkMd5)) {
				throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED,"Chunk MD5 was " + calculatedChunkMd5 + " expected " + chunkMd5);
			}
		}

		saveFile(data, file.getOffset(), file);

		} catch (IOException e) {
			logger.error("unable to read data from received input stream", e);
		}
	}

	public FileResource uploadFileResource(String filename, SecurityContext securityContext, String md5, String chunkMd5, boolean lastChunk, InputStream fileInputStream) {

		FileResource fileResource = listAllFileResources(new FileResourceFilter().setMd5s(Collections.singleton(md5)),securityContext).stream().findFirst().orElse(null);
		if (fileResource == null) {
			String ext = filename.endsWith("tar.gz") ? "tar.gz" : FilenameUtils.getExtension(filename);
			String actualFilename = !ext.isEmpty() ? UUID.randomUUID() + "." + ext : UUID.randomUUID().toString();
			String fullPath = new File(uploadPath,actualFilename).getAbsolutePath();
			FileResourceCreate fileResourceCreate = new FileResourceCreate()
					.setActualFilename(actualFilename)
					.setFullPath(fullPath)
					.setMd5(md5)
					.setOffset(0L)
					.setOriginalFilename(filename)
					.setName(filename);
			fileResource=createFileResource(fileResourceCreate, securityContext);

		}
		if (!fileResource.isDone()) {
			saveFile(fileInputStream, chunkMd5, fileResource);
			if (lastChunk) {
				File file = new File(fileResource.getFullPath());
				String calculatedFileMd5 = md5Service.generateMD5(file);
				if (!md5.equals(calculatedFileMd5)) {
					if (file.delete()) {
						fileResource.setOffset(0L);
						fileResourceRepository.merge(fileResource);
					} else {
						logger.warn("Could not delete bad md5 file " + file);

					}
					throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED,"File Total MD5 is " + calculatedFileMd5 + " expected " + md5);
				}
				fileResource.setDone(true);
				fileResourceRepository.merge(fileResource);
			}

		}

		return fileResource;

	}


	public byte[] readFilePart(File file, long offset) {
		byte[] data = new byte[MAX_FILE_PART_SIZE];
		try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
			randomAccessFile.seek(offset);
			int read = randomAccessFile.read(data, 0, MAX_FILE_PART_SIZE);
			if (read < MAX_FILE_PART_SIZE) {
				data = Arrays.copyOf(data, read);
			}
		} catch (IOException e) {
			logger.error("unable to read file part", e);
		}
		return data;
	}

	public FileResource getFileResourceById(String id, SecurityContext securityContext) {
		Optional<FileResource> fileResourceOptional = id != null ? listAllFileResources(new FileResourceFilter().setBasicPropertiesFilter(new BasicPropertiesFilter().setOnlyIds(Collections.singleton(id))), securityContext).stream().findFirst() : Optional.empty();
		FileResource fileResource = fileResourceOptional.orElse(null);
		if (fileResource != null) {
			File file = new File(fileResource.getFullPath());
			if (!file.exists()) {
				fileResource.setDone(false);
				fileResource.setOffset(0);
				merge(fileResource);
			}
		}
		return fileResource;
	}

	public FileResource getFileResourceByMd5(String md5, SecurityContext securityContext) {
		Optional<FileResource> fileResourceOptional = md5 != null ? listAllFileResources(new FileResourceFilter().setMd5s(Collections.singleton(md5)), securityContext).stream().findFirst() : Optional.empty();
		FileResource fileResource = fileResourceOptional.orElse(null);
		if (fileResource != null) {
			File file = new File(fileResource.getFullPath());
			if (!file.exists()) {
				fileResource.setDone(false);
				fileResource.setOffset(0);
				merge(fileResource);
			}
		}
		return fileResource;
	}
}
