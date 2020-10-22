package com.flexicore.request;

import org.apache.commons.csv.CSVFormat;

import java.util.Map;

public class ExportDynamicExecution extends ExecuteDynamicExecution{



    private CSVFormat csvFormat;
    private Map<String, String> fieldToName;

    public Map<String, String> getFieldToName() {
        return fieldToName;
    }

    public <T extends ExportDynamicExecution> T setFieldToName(Map<String, String> fieldToName) {
        this.fieldToName = fieldToName;
        return (T) this;
    }

    public CSVFormat getCsvFormat() {
        return csvFormat;
    }

    public <T extends ExportDynamicExecution> T setCsvFormat(CSVFormat csvFormat) {
        this.csvFormat = csvFormat;
        return (T) this;
    }
}
