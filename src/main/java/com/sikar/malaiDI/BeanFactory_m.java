package com.sikar.malaiDI;


import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

// Bean Factory
public class BeanFactory_m {
    private final Map<String, Object> singletonBeans;
    private final Map<String, BeanDefinition_m> beanDefinitions;

    public BeanFactory_m() {
        this.singletonBeans = new HashMap<>();
        this.beanDefinitions = new HashMap<>();
    }

    public void registerBeanDefinition(BeanDefinition_m beanDefinitionM) {
        System.out.println("bean registered with the key: " + beanDefinitionM.getBeanClass().getSimpleName());
        beanDefinitions.put(beanDefinitionM.getBeanClass().getSimpleName(), beanDefinitionM);
    }

    public <T> T getBean(Class<?> beanClass) throws Exception {
        var beanName = beanClass.getSimpleName();
        // dependency not resolved exception
        if (!beanDefinitions.containsKey(beanName)) {
            throw new IllegalArgumentException("Bean '" + beanName + "' not found");
        }

        BeanDefinition_m beanDefinitionM = beanDefinitions.get(beanName);

        // Check if the bean is singleton
        //Assume all beans are singletons, we can extend this to support scope logics
        if (singletonBeans.containsKey(beanName)) {
            return (T) singletonBeans.get(beanName);
        }

        // Create bean instance
        Constructor<?> constructor = beanDefinitionM.getBeanClass().getDeclaredConstructors()[0];
        Object[] args = constructor.getParameterTypes().length > 0 ? resolveDependencies(constructor) : null;
        T bean = (T) constructor.newInstance(args);

        // Add bean to singleton beans map if it's singleton
        if (isSingleton(beanDefinitionM)) {
            singletonBeans.put(beanName, bean);
        }

        return bean;
    }

    private boolean isSingleton(BeanDefinition_m beanDefinitionM) {
        return true;
    }

    private Object[] resolveDependencies(Constructor<?> constructor) throws Exception {
        return Arrays.stream(constructor.getParameterTypes())
                .map(this::getBeansSafely)
                .toArray();
    }

    private Object getBeansSafely(Class<?> paramType) {
        try {
            return getBean(paramType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
