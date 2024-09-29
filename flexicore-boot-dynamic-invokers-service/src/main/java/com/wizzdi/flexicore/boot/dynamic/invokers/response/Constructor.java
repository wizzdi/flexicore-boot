package com.wizzdi.flexicore.boot.dynamic.invokers.response;

import java.util.List;

public record Constructor(String name, List<ConstructorParameter> parameters) {
}
