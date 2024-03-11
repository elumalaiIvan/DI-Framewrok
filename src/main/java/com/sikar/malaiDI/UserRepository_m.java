package com.sikar.malaiDI;

import com.sikar.malaiDI.annotations.Component_m;
import com.sikar.malaiDI.annotations.Repository_m;

// Example UserRepository class
@Repository_m
public class UserRepository_m {
    public void save(String username) {
        System.out.println("Saving user: " + username);
    }
}
