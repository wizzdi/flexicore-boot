package com.flexicore.request;

import com.wizzdi.flexicore.boot.dynamic.invokers.request.ExecuteDynamicExecution;
import org.apache.commons.csv.CSVFormat;

import java.util.Map;

public class ExportDynamicExecution extends ExecuteDynamicExecution {



    private CSVFormat csvFormat;
    private Map<String, FieldProperties> fieldToName;

    public Map<String, FieldProperties> getFieldToName() {
        return fieldToName;
    }

    public <T extends ExportDynamicExecution> T setFieldToName(Map<String, FieldProperties> fieldToName) {
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
