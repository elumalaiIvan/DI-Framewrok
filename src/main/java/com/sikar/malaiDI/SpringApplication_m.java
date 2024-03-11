package com.sikar.malaiDI;

import org.springframework.boot.SpringApplication;

import java.util.Arrays;

public class SpringApplication_m {

    private static BeanFactory_m beanFactoryM = new BeanFactory_m();
    public static void run(Class<?> primarySource, String... args) throws Exception {
         SpringApplication.run(primarySource, args);
        // Register bean definitions in custom DI framework
        scanAndRegisterComponents(primarySource.getPackageName());

        var userBean = beanFactoryM.getBean(UserService_m.class);
        userBean.getUser();
    }

    private static void scanAndRegisterComponents(String basePackage) {

        // Add service, configuration etc
        ClassPathScanner_m.scan(basePackage).stream()
                .filter(clazz -> Arrays.stream(BeanFactory_m.injectionCapableAnnotations)
                        .anyMatch(annotationClazz -> clazz.isAnnotationPresent(annotationClazz)))
                .forEach(SpringApplication_m::registerBeans);
    }

    private static void registerBeans(Class<?> clazz) {
        beanFactoryM.registerBeanDefinition(new BeanDefinition_m(clazz));
    }

}
