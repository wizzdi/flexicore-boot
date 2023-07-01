package com.flexicore.response;

import org.springframework.boot.actuate.health.HealthComponent;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.actuate.health.SystemHealth;

import java.util.List;
import java.util.stream.Collectors;

public class HealthStatusResponse {

    private HealthStatus outcome;
    private List<HealthCheck> checks;

    public HealthStatusResponse() {
    }

    public HealthStatusResponse(HealthComponent healthComponent) {
        this.outcome=healthComponent.getStatus()== Status.UP?HealthStatus.UP:HealthStatus.DOWN;
        if(healthComponent instanceof SystemHealth){
            SystemHealth systemHealth= (SystemHealth) healthComponent;
            this.checks=systemHealth.getComponents().entrySet().stream().map(f->new HealthCheck(f)).collect(Collectors.toList());
        }
    }

    public HealthStatus getOutcome() {
        return outcome;
    }

    public <T extends HealthStatusResponse> T setOutcome(HealthStatus outcome) {
        this.outcome = outcome;
        return (T) this;
    }

    public List<HealthCheck> getChecks() {
        return checks;
    }

    public <T extends HealthStatusResponse> T setChecks(List<HealthCheck> checks) {
        this.checks = checks;
        return (T) this;
    }

    @Override
    public String toString() {
        return "HealthStatusResponse{" +
                "outcome=" + outcome +
                ", checks=" + checks +
                '}';
    }
}
