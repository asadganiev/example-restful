package uz.paynet.rest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.paynet.rest.users.PaynetUser;

public interface UserRepository extends JpaRepository<PaynetUser, Long> {

    PaynetUser findByUsername(String username);
}
