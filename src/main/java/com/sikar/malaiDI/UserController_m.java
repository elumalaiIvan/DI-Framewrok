package com.sikar.malaiDI;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/di")
public class UserController_m {

    @Autowired_m
    private UserService_m userServiceM;

    @GetMapping("/user")
    public String userName() {
        return userServiceM.getUser();
    }

}
