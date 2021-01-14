package com.example.rest.services;

import com.example.rest.users.User;

import java.util.Optional;

public interface Users {

    User save(User user);

    User findByUsername(String username);

    User findByUsernameAndPassword(String username, String password);

    User update(User user);

    Optional<User> getUserById(Long userId);
}
