package com.flexicore.interfaces;

import java.time.OffsetDateTime;

public interface Syncable {

    String getId();
    OffsetDateTime getUpdateDate();
    OffsetDateTime getCreationDate();
    boolean isNoSQL();
}
