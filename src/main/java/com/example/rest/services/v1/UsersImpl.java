package com.example.rest.services.v1;

import com.example.rest.repositories.UserRepository;
import com.example.rest.services.Users;
import com.example.rest.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsersImpl implements Users {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public UsersImpl(UserRepository userRepository, PasswordEncoder encoder) {

        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public User save(User user) {

        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {

        return userRepository.findByUsername(username);
    }

    @Override
    public User findByUsernameAndPassword(String username, String password) {

        return userRepository.findByUsernameAndPassword(username, password);
    }

    @Override
    public User update(User userUpdate) {

        final User user = userRepository.findByUsername(userUpdate.getUsername());

        user.setPassword(encoder.encode(userUpdate.getPassword()));
        user.setFirstName(userUpdate.getFirstName());
        user.setLastName(userUpdate.getLastName());
        user.setCity(userUpdate.getCity());
        user.setRegion(userUpdate.getRegion());
        user.setStreet(userUpdate.getStreet());
        user.setBirthday(userUpdate.getBirthday());
        user.setZipCode(userUpdate.getZipCode());

        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(Long userId) {

        return userRepository.findById(userId);
    }
}
