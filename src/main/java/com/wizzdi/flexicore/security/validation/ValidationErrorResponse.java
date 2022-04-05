package com.wizzdi.flexicore.security.validation;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrorResponse {

  private List<Violation> violations = new ArrayList<>();

  public List<Violation> getViolations() {
    return violations;
  }

  public <T extends ValidationErrorResponse> T setViolations(List<Violation> violations) {
    this.violations = violations;
    return (T) this;
  }

  @Override
  public String toString() {
    return "ValidationErrorResponse{" +
            "violations=" + violations +
            '}';
  }
}

