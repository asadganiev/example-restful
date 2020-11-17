package uz.paynet.rest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.paynet.rest.users.PaynetUser;

import static java.util.Collections.emptyList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private Users users;

    @Autowired
    public UserDetailsServiceImpl(Users users) {

        this.users = users;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        PaynetUser user = users.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        
        return new User(user.getUsername(), user.getPassword(), emptyList());
    }
}