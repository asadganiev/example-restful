package uz.paynet.rest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import uz.paynet.rest.repositories.UserRepository;
import uz.paynet.rest.services.PasswordEncoderImpl;
import uz.paynet.rest.services.Users;
import uz.paynet.rest.services.v1.UsersImpl;

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
