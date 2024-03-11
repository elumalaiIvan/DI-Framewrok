package com.sikar.malaiDI;

import com.sikar.malaiDI.annotations.Autowired_m;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/di")
public class UserController_m {

    // will not work as RestController registration is done by actual beanFactory
    @Autowired_m
    UserService_m userServiceM;

    @GetMapping("/user")
    public String userName() {
        return userServiceM.getUser();
    }

}
