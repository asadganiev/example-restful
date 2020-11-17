package uz.paynet.rest.services;

import uz.paynet.rest.users.PaynetUser;

public interface Users {

    PaynetUser save(PaynetUser user);

    PaynetUser findByUsername(String username);
}
