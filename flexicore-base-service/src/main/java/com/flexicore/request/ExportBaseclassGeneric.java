package com.flexicore.request;

import com.flexicore.model.FilteringInformationHolder;
import org.apache.commons.csv.CSVFormat;

import java.util.Map;

public class ExportBaseclassGeneric<E extends FilteringInformationHolder> {

    private E filter;
    private Map<String, FieldProperties> fieldToName;
    private CSVFormat csvFormat;

    public E getFilter() {
        return filter;
    }

    public <T extends ExportBaseclassGeneric<E>> T setFilter(E filter) {
        this.filter = filter;
        return (T) this;
    }

    public Map<String, FieldProperties> getFieldToName() {
        return fieldToName;
    }

    public <T extends ExportBaseclassGeneric<E>> T setFieldToName(Map<String, FieldProperties> fieldToName) {
        this.fieldToName = fieldToName;
        return (T) this;
    }

    public CSVFormat getCsvFormat() {
        return csvFormat;
    }

    public <T extends ExportBaseclassGeneric<E>> T setCsvFormat(CSVFormat csvFormat) {
        this.csvFormat = csvFormat;
        return (T) this;
    }
}
