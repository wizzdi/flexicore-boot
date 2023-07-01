package com.flexicore.service;

import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.FlexiCoreService;
import com.flexicore.model.Baseclass;
import com.wizzdi.flexicore.file.model.FileResource;
import com.flexicore.model.User;
import com.flexicore.request.*;
import com.flexicore.security.SecurityContext;
import com.wizzdi.flexicore.file.model.ZipFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public interface FileResourceService extends FlexiCoreService {
    /**
     * generates new path for file resource inside the user directory
     * @param relatedBaseclass related baseclass , prefix will be taken from the baseclass name
     * @param creatingUser user
     * @return new path ( no actual file will be created)
     */
    static String generateNewPathForFileResource(Baseclass relatedBaseclass, User creatingUser) {
        return generateNewPathForFileResource(relatedBaseclass.getName(), creatingUser);
    }


    boolean saveFile(byte[] data, long offsetInFile, FileResource file);
    void saveFile(InputStream is,String chunkMd5, FileResource file);

    void saveFile(InputStream is, FileResource file);
    byte[] readFilePart(File file, long offset);
    /**
     * generates new path for file resource inside the user directory
     * @param prefix file prefix
     * @param creatingUser user
     * @return new path ( no actual file will be created)
     */
    static String generateNewPathForFileResource(String prefix, User creatingUser) {
        File home = new File(creatingUser.getHomeDir());
        if (!home.exists()) {
            home.mkdirs();
        }
        return creatingUser.getHomeDir() + "/" + prefix + "--" + UUID.randomUUID().toString();

    }

    /**
     * creates file resource
     * @param pathToFileResource file path
     * @param securityContext security context of the user to execute the action
     * @return
     */
    FileResource create(String pathToFileResource, SecurityContext securityContext);

    /**
     * creates file resource
     * @param fileResourceCreate create object
     * @param securityContext security context of the user to execute the action
     * @return the created file resource
     */
    FileResource createFileResource(FileResourceCreate fileResourceCreate, SecurityContext securityContext);

    void persist(Object o);

    /**
     * creates file resource
     * @param pathToFileResource file path
     * @param securityContext security context of the user to execute the action
     * @return the created file resource
     */
    FileResource createDontPersist(String pathToFileResource, SecurityContext securityContext);

    /**
     * creates file resource
     * @param fileResourceCreate object used to create file resource
     * @param securityContext security context of the user to execute the action
     * @return the created file resource
     */
    FileResource createNoMerge(FileResourceCreate fileResourceCreate, SecurityContext securityContext);

    /**
     * updates file resource
     * @param fileResourceCreate object used to update file resource
     * @param fileResource file resource to update
     * @return true if there was an update , false otherwise
     */
    boolean updateFileResourceNoMerge(FileResourceCreate fileResourceCreate, FileResource fileResource);
    /**
     * generates MD5 hash
     * @param is inputStream
     * @return md5 string
     */
    String generateMD5(InputStream is);

    /**
     * generates MD5 hash
     * @param filePath path to file
     * @return md5 string
     */
    String generateMD5(String filePath);

    /**
     * generates MD5 hash
     * @param file file
     * @return md5 string
     */
    String generateMD5(File file);

    void merge(Object o);

    /**
     * lists all file resources
     * @param fileResourceFilter object used to filter file resources
     * @param securityContext security context of the user to execute the action
     * @return list of file resources including count and pagination info
     */
    PaginationResponse<FileResource> getAllFileResources(FileResourceFilter fileResourceFilter, SecurityContext securityContext);

    /**
     * lists all file resources
     * @param fileResourceFilter object used to filter file resources
     * @param securityContext security context of the user to execute the action
     * @return list of file resources
     */
    List<FileResource> listAllFileResources(FileResourceFilter fileResourceFilter, SecurityContext securityContext);

    void massMerge(List<?> resources);

    void refrehEntityManager();
    FileResource registerFile(String path, boolean calculateMd5, SecurityContext securityContext) throws FileNotFoundException;

    /**
     * validates #FinallizeFileResource
     *
     * @param finallizeFileResource object to validate
     * @param securityContext  security context of the user to execute the action
     */
    void validate(FinallizeFileResource finallizeFileResource, SecurityContext securityContext);

    /**
     * validates #ZipAndDownloadRequest
     *
     * @param zipAndDownloadRequest object to validate
     * @param securityContext       security context of the user to execute the action
     */
    void validate(ZipAndDownloadRequest zipAndDownloadRequest, SecurityContext securityContext);


    /**
     * lists all zip files
     *
     * @param zipFileFilter   object used to filter the zip files result
     * @param securityContext security context of the user to execute the action
     * @return list of zip files
     */
    List<ZipFile> listAllZipFiles(ZipFileFilter zipFileFilter, SecurityContext securityContext);

    /**
     * zips files and creates a zipFile , will reuse an existing one if there is already zip file with these files
     *
     * @param zipAndDownload  object used to describe the files to zip
     * @param securityContext security context of the user to execute the action
     * @return created/existing zip file
     */
    ZipFile zipAndDownload(ZipAndDownloadRequest zipAndDownload, SecurityContext securityContext);
}
