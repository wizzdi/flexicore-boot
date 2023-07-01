package com.flexicore.response;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthComponent;
import org.springframework.boot.actuate.health.Status;

import java.util.Map;

public class HealthCheck {
    private String name;
    private HealthStatus state;
    private Map<String,Object> data;

    public HealthCheck() {
    }

    public HealthCheck(Map.Entry<String, HealthComponent> entry) {
        this.name = entry.getKey();
        HealthComponent value = entry.getValue();
        this.state= value.getStatus()== Status.UP?HealthStatus.UP:HealthStatus.DOWN;
        if(value instanceof Health){
            this.data=((Health) value).getDetails();

        }
    }

    public String getName() {
        return name;
    }

    public <T extends HealthCheck> T setName(String name) {
        this.name = name;
        return (T) this;
    }

    public HealthStatus getState() {
        return state;
    }

    public <T extends HealthCheck> T setState(HealthStatus state) {
        this.state = state;
        return (T) this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public <T extends HealthCheck> T setData(Map<String, Object> data) {
        this.data = data;
        return (T) this;
    }

    @Override
    public String toString() {
        return "HealthCheck{" +
                "name='" + name + '\'' +
                ", state=" + state +
                ", data=" + data +
                '}';
    }
}
