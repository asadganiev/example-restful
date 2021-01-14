package com.example.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

@SpringBootApplication(
        exclude = {ErrorMvcAutoConfiguration.class}
)
public class RestfulApplication {

    public static void main(String[] args) {

        SpringApplication.run(RestfulApplication.class, args);
    }
}
