package uz.paynet.rest.services;

import uz.paynet.rest.users.PaynetUser;

import java.util.Optional;

public interface Users {

    PaynetUser save(PaynetUser user);

    PaynetUser findByUsername(String username);

    PaynetUser findByUsernameAndPassword(String username, String password);

    PaynetUser update(PaynetUser user);

    Optional<PaynetUser> getUserById(Long userId);
}
