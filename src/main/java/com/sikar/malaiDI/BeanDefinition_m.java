package com.sikar.malaiDI;

import java.util.HashMap;
import java.util.Map;

// Bean Definition
public class BeanDefinition_m {
    private final Class<?> beanClass;
    private final Map<String, Object> dependencies;

    public BeanDefinition_m(Class<?> beanClass) {
        this.beanClass = beanClass;
        this.dependencies = new HashMap<>();
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public void addDependency(String fieldName, Object dependency) {
        dependencies.put(fieldName, dependency);
    }

    public Object getDependency(String fieldName) {
        return dependencies.get(fieldName);
    }
}

