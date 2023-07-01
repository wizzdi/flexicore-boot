package com.wizzdi.flexicore.security.request;

public class SortParameter {

    private String name;
    private OrderDirection orderDirection;

    public SortParameter(String name, OrderDirection orderDirection) {
        this.name = name;
        this.orderDirection = orderDirection;
    }

    public SortParameter() {
    }

    public String getName() {
        return name;
    }

    public <T extends SortParameter> T setName(String name) {
        this.name = name;
        return (T) this;
    }

    public OrderDirection getOrderDirection() {
        return orderDirection;
    }

    public <T extends SortParameter> T setOrderDirection(OrderDirection orderDirection) {
        this.orderDirection = orderDirection;
        return (T) this;
    }
}
