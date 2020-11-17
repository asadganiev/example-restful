package uz.paynet.rest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import uz.paynet.rest.repositories.UserRepository;
import uz.paynet.rest.services.Users;
import uz.paynet.rest.services.v1.UsersImpl;

@Configuration
public class BeansConfig {

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Users users(UserRepository repository) {

        return new UsersImpl(repository);
    }
}
