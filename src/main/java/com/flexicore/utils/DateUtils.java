package com.flexicore.utils;

import com.flexicore.interfaces.Syncable;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoField;

public class DateUtils {
    public static OffsetDateTime millisFloor(OffsetDateTime OffsetDateTime){
        return OffsetDateTime!=null?OffsetDateTime.with(ChronoField.MILLI_OF_SECOND,OffsetDateTime.get(ChronoField.MILLI_OF_SECOND)):null;
    }
    public static OffsetDateTime getBaseclassDate(Syncable baseclass) {
        OffsetDateTime OffsetDateTime = baseclass.getUpdateDate() != null ? baseclass.getUpdateDate() : baseclass.getCreationDate();
        return millisFloor(OffsetDateTime);
    }
}
