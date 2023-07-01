package com.wizzdi.flexicore.security.validation;

public class Violation {

  private String fieldName;
  private String message;

  public Violation() {
  }

  public Violation(String fieldName, String message) {
    this.fieldName = fieldName;
    this.message = message;
  }

  public String getFieldName() {
    return fieldName;
  }

  public String getMessage() {
    return message;
  }

  public <T extends Violation> T setFieldName(String fieldName) {
    this.fieldName = fieldName;
    return (T) this;
  }

  public <T extends Violation> T setMessage(String message) {
    this.message = message;
    return (T) this;
  }


  @Override
  public String toString() {
    return "Violation{" +
            "fieldName='" + fieldName + '\'' +
            ", message='" + message + '\'' +
            '}';
  }
}