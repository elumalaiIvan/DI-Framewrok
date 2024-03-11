package com.sikar.malaiDI;

class UserService_m {
    private final com.sikar.malaiDI.UserRepository_m userRepository;

    public UserService_m(com.sikar.malaiDI.UserRepository_m userRepository) {
        this.userRepository = userRepository;
    }

    public void addUser(String username) {
        userRepository.save(username);
    }
}

