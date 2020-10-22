package com.flexicore.converters;

import com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer;

import java.time.format.DateTimeFormatter;


public class CustomOffsetDateTimeSerializer extends OffsetDateTimeSerializer {

    public CustomOffsetDateTimeSerializer(DateTimeFormatter dateTimeFormatter) {
      super(OffsetDateTimeSerializer.INSTANCE,false,dateTimeFormatter);
    }


}
