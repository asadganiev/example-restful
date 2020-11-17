package uz.paynet.rest.services.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.paynet.rest.repositories.UserRepository;
import uz.paynet.rest.services.Users;
import uz.paynet.rest.users.PaynetUser;

@Service
public class UsersImpl implements Users {

    private final UserRepository userRepository;

    @Autowired
    public UsersImpl(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public PaynetUser save(PaynetUser user) {

        return userRepository.save(user);
    }

    @Override
    public PaynetUser findByUsername(String username) {

        return null;
    }
}
