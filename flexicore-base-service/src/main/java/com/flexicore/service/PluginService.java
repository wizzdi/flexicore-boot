package com.flexicore.service;

import com.flexicore.data.jsoncontainers.PluginType;
import com.flexicore.interfaces.FlexiCoreService;
import com.flexicore.model.ModuleManifest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;


public interface PluginService extends FlexiCoreService {
    static Queue<ModuleManifest> externalModules=new ConcurrentLinkedQueue<>();


    /**
     * used to get the loaded entities (jars , files found in the entitiesFolder)
     * @return a list of objects describing the loaded entities jars
     */
    List<ModuleManifest> getModelListing();

    /**
     * used to get the loaded plugins (jars , files found in the pluginFolder)
     * @return a list of objects describing the loaded plugin jars
     */
    Set<ModuleManifest> getPluginModuleListing();

    /**
     * reads manifest into structured #ModuleManifest Object
     * @param jar jar containing the manifest
     * @param logger logger used to log status
     * @return module manifest
     */
    ModuleManifest readModule(File jar, Logger logger);

    /**
     * registers as an external Module ( this will return on health checks etc)
     * @param moduleManifest module manifest object
     */
    static void registerExternalModule(ModuleManifest moduleManifest){
        externalModules.add(moduleManifest);
    }

    /**
     * reads manifest and registers as an external Module ( this will return on health checks etc)
     * @param manifestFile path to manifest file
     * @return manifest
     * @throws IOException if manifest cannot be loaded
     */
    static ModuleManifest registerExternalModule(File manifestFile) throws IOException {
        ModuleManifest moduleManifest=loadExternalModule(manifestFile);
        registerExternalModule(moduleManifest);
        return moduleManifest;
    }

    /**
     * reads manifest into structured #ModuleManifest Object
     * @param manifestFile path to manifest file
     * @return module manigest
     * @throws IOException if manifest cnnot be loaded
     */
    static ModuleManifest loadExternalModule(File manifestFile) throws IOException {
        try(FileInputStream fileInputStream=new FileInputStream(manifestFile)){
            return loadExternalModule(manifestFile, fileInputStream);
        }

    }

    /**
     * reads manifest into structured #ModuleManifest Object
     * @param manifestFile path to manifest file
     * @param inputStream input stream with manifest data
     * @return manifest
     * @throws IOException if manifest cannot be loaded
     */
    static ModuleManifest loadExternalModule(File manifestFile, InputStream inputStream) throws IOException {
        Properties properties=new Properties();
        properties.load(inputStream);
        String uuid=properties.getProperty("uuid","unknown-"+ UUID.randomUUID());
        String version=properties.getProperty("version","unknown-"+UUID.randomUUID());

        return new ModuleManifest(uuid,version,manifestFile.getAbsolutePath(),manifestFile.getAbsolutePath(),new HashSet<>(),new HashSet<>(), PluginType.External);
    }
}
