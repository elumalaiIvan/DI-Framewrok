package com.sikar.malaiDI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CustomDIApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(CustomDIApplication.class, args);

		// Register bean definitions in custom DI framework
		BeanFactory_m beanFactory = new BeanFactory_m();
		beanFactory.registerBeanDefinition(new BeanDefinition_m(UserService_m.class));
		beanFactory.registerBeanDefinition(new BeanDefinition_m(UserRepository_m.class));

		// Retrieve beans from the custom DI framework

		UserService_m userService = beanFactory.getBean(UserService_m.class);
		UserRepository_m userRepository = beanFactory.getBean(UserRepository_m.class);

		// Use beans
		userService.addUser("John Doe");
	}

}
