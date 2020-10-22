package com.flexicore.model;

import com.flexicore.data.jsoncontainers.PluginType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.HashSet;
import java.util.UUID;

/**
 * Created by Asaf on 25/02/2017.
 */
public class ModuleManifest {
    private String uuid;
    private String version;
    private String loadedFrom;
    private String originalModuleLocation;
    private HashSet<String> requires;
    private HashSet<String> provides;
    private PluginType pluginType;

    public ModuleManifest() {
        this.requires = new HashSet<>();
        this.provides = new HashSet<>();
    }

    public ModuleManifest(String loadedFrom, String originalModuleLocation, PluginType pluginType) {
        this.loadedFrom = loadedFrom;
        this.originalModuleLocation = originalModuleLocation;
        this.pluginType=pluginType;
        this.requires = new HashSet<>();
        this.provides = new HashSet<>();
        uuid="unknown"+ UUID.randomUUID().toString();
        version="unknown"+ UUID.randomUUID().toString();
    }

    public ModuleManifest(String uuid, String version, String loadedFrom, String originalModuleLocation, HashSet<String> requires, HashSet<String> provides, PluginType pluginType) {
        this.uuid = uuid;
        this.version = version;
        this.loadedFrom = loadedFrom;
        this.requires = requires;
        this.provides = provides;
        this.pluginType = pluginType;
        this.originalModuleLocation=originalModuleLocation;
    }

    public void set(String uuid, String version, String loadedFrom,String originalModuleLocation, HashSet<String> requires, HashSet<String> provides, PluginType pluginType) {
        this.uuid = uuid;
        this.version = version;
        this.loadedFrom = loadedFrom;
        this.requires = requires;
        this.provides = provides;
        this.pluginType = pluginType;
        this.originalModuleLocation=originalModuleLocation;
    }

    public HashSet<String> getRequires() {
        return requires;
    }

    public void setRequires(HashSet<String> requires) {
        this.requires = requires;
    }

    public HashSet<String> getProvides() {
        return provides;
    }

    public void setProvides(HashSet<String> provides) {
        this.provides = provides;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLoadedFrom() {
        return loadedFrom;
    }

    public void setLoadedFrom(String loadedFrom) {
        this.loadedFrom = loadedFrom;
    }

    public PluginType getPluginType() {
        return pluginType;
    }

    public void setPluginType(PluginType pluginType) {
        this.pluginType = pluginType;
    }

    public String getOriginalModuleLocation() {
        return originalModuleLocation;
    }

    public void setOriginalModuleLocation(String originalModuleLocation) {
        this.originalModuleLocation = originalModuleLocation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ModuleManifest that = (ModuleManifest) o;

        return new EqualsBuilder()
                .append(uuid, that.uuid)
                .append(version, that.version)
                .append(loadedFrom, that.loadedFrom)
                .append(originalModuleLocation, that.originalModuleLocation)
                .append(requires, that.requires)
                .append(provides, that.provides)
                .append(pluginType, that.pluginType)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(loadedFrom)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "ModuleManifest{" +
                "uuid='" + uuid + '\'' +
                ", version='" + version + '\'' +
                ", loadedFrom='" + loadedFrom + '\'' +
                ", originalModuleLocation='" + originalModuleLocation + '\'' +
                ", requires=" + requires +
                ", provides=" + provides +
                ", pluginType=" + pluginType +
                '}';
    }
}
