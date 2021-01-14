package com.example.rest.config;

import com.example.rest.repositories.UserRepository;
import com.example.rest.services.PasswordEncoderImpl;
import com.example.rest.services.Users;
import com.example.rest.services.v1.UsersImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeansConfig {

    @Bean
    public Users users(UserRepository repository) {

        return new UsersImpl(repository, encoder());
    }

    @Bean
    public PasswordEncoder encoder() {

        return new PasswordEncoderImpl();
    }
}
