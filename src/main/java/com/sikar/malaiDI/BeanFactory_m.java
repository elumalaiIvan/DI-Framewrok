package com.sikar.malaiDI;


import com.sikar.malaiDI.annotations.Bean_m;
import com.sikar.malaiDI.annotations.Component_m;
import com.sikar.malaiDI.annotations.Configuration_m;
import com.sikar.malaiDI.annotations.Repository_m;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

// Bean Factory
public class BeanFactory_m {
    public static Class[] injectionCapableAnnotations = {
            Component_m.class,
            Configuration_m.class,
            Repository_m.class,
            RestController.class
    };

    private final Map<String, Object> singletonBeans;
    private final Map<String, BeanDefinition_m> beanDefinitions;


    public BeanFactory_m() {
        this.singletonBeans = new HashMap<>();
        this.beanDefinitions = new HashMap<>();
    }

    public void registerBeanDefinition(BeanDefinition_m beanDefinitionM) {
        var beanName = beanDefinitionM.getBeanClass().getSimpleName();
        // if precedence given, we should alter this should update to true
        var shouldUpdate = false;
        if (shouldUpdate || !beanDefinitions.containsKey(beanName)) {
            System.out.println("bean registered with the key: " + beanDefinitionM.getBeanClass().getSimpleName());
            storeTheBean(beanName, beanDefinitionM);
        }
        Object instance = null;
        try {
            instance = getBean(beanDefinitionM.getBeanClass());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        verifyAutoWireCapableFields(instance, beanDefinitionM);
        registerBeansFromTheMethodsOfTheBean(instance, beanDefinitionM);
    }

    private void storeTheBean(String beanName, BeanDefinition_m beanDefinitionM) {
        beanDefinitions.put(beanName, beanDefinitionM);
    }

    private void verifyAutoWireCapableFields(Object instance, BeanDefinition_m beanDefinitionM) {
        beanDefinitionM.getAutowiredFields().forEach(field -> {
            try {
                System.out.println("autowired field value is set: " + field.getName());
                field.setAccessible(true);
                field.set(instance, getBean(field.getType()));
            } catch (Exception e) {
                // Autowiring exception
                throw new RuntimeException(e);
            }
        });
    }

    private void registerBeansFromTheMethodsOfTheBean(Object instance, BeanDefinition_m beanDefinitionM) {
        try {
            beanDefinitionM.beanMethods().stream()
                    .filter(method -> method.isAnnotationPresent(Bean_m.class))
                    .forEach(method -> {
                        var args = resolveDependencies(method);
                        try {
                           var returndObject = method.invoke(instance, args);
                           //@Order will be considered while getting object our of singleton
                           singletonBeans.put(method.getReturnType().getSimpleName(), returndObject);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            // ApplicationFactory is implementation of beanFactory
                            // Arguments are not registered with applicationContext/beanFactory exception
                            throw new RuntimeException(e);
                        }
                    });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T> boolean isInjectionCapable(Class<T> clazz) {
        return Arrays.stream(injectionCapableAnnotations)
                .anyMatch(clazz::isAnnotationPresent);
    }

    public <T> T getBean(Class<T> beanClass) throws Exception {
        var beanName = beanClass.getSimpleName();

        // dependency not resolved exception
        if (!beanDefinitions.containsKey(beanName)) {
            if (!isInjectionCapable(beanClass))
                throw new IllegalArgumentException("Bean '" + beanName + "' not found");
            // so we don't wait for the order of bean registration
            registerBeanDefinition(new BeanDefinition_m(beanClass));
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

    private Object[] resolveDependencies(Method method) {
        return Arrays.stream(method.getParameterTypes())
                .map(this::getBeansSafely)
                .toArray();
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
