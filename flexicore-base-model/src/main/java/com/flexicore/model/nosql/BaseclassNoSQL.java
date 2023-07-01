package com.flexicore.model.nosql;

import com.flexicore.interfaces.Syncable;
import com.flexicore.model.Baseclass;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Date;

@BsonDiscriminator
public class BaseclassNoSQL implements Syncable {

    @BsonId
    private String id;
    @BsonProperty(useDiscriminator = true)
    private String type;
    private Date dateCreated;

    private String name;

    public BaseclassNoSQL() {
        this.id= Baseclass.getBase64ID();
        this.dateCreated=Date.from(Instant.now());
        this.type=getClass().getCanonicalName();
    }



    public String getId() {
        return id;
    }

    public <T extends BaseclassNoSQL> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    public String getType() {
        return type;
    }

    public <T extends BaseclassNoSQL> T setType(String type) {
        this.type = type;
        return (T) this;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public <T extends BaseclassNoSQL> T setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
        return (T) this;
    }

    @Override
    @BsonIgnore
    public OffsetDateTime getCreationDate() {
        return dateCreated!=null?dateCreated.toInstant().atZone(ZoneId.systemDefault()).toOffsetDateTime():null;
    }

    @Override
    @BsonIgnore
    public OffsetDateTime getUpdateDate() {
        return getCreationDate();
    }

    @BsonIgnore
    public void setCreationDate(OffsetDateTime offsetDateTime) {

    }

    @BsonIgnore
    public void setUpdateDate(OffsetDateTime offsetDateTime) {

    }

    @Override
    @BsonIgnore
    public boolean isNoSQL() {
        return true;
    }

    public String getName() {
        return name;
    }

    public <T extends BaseclassNoSQL> T setName(String name) {
        this.name = name;
        return (T) this;
    }
}
