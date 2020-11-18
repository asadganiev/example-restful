package uz.paynet.rest.services.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.paynet.rest.repositories.UserRepository;
import uz.paynet.rest.services.Users;
import uz.paynet.rest.users.PaynetUser;

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
    public PaynetUser save(PaynetUser user) {

        return userRepository.save(user);
    }

    @Override
    public PaynetUser findByUsername(String username) {

        return userRepository.findByUsername(username);
    }

    @Override
    public PaynetUser findByUsernameAndPassword(String username, String password) {

        return userRepository.findByUsernameAndPassword(username, password);
    }

    @Override
    public PaynetUser update(PaynetUser userUpdate) {

        final PaynetUser user = userRepository.findByUsername(userUpdate.getUsername());

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
    public Optional<PaynetUser> getUserById(Long userId) {

        return userRepository.findById(userId);
    }
}
