package com.sikar.malaiDI;

// Example UserRepository class
@Component_m
public class UserRepository_m {
    public void save(String username) {
        System.out.println("Saving user: " + username);
    }
}
