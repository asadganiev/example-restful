package uz.paynet.rest.controllers.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.paynet.rest.error.ResponseModel;
import uz.paynet.rest.services.Users;
import uz.paynet.rest.users.PaynetUser;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1")
public class UsersController {

    private Users users;
    private BCryptPasswordEncoder encoder;

    @Autowired
    public UsersController(Users users, BCryptPasswordEncoder encoder) {

        this.users = users;
        this.encoder = encoder;
    }

    @PostMapping(value = "/users/signup",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> signUp(@Valid @RequestBody PaynetUser user,
                                         BindingResult result) {

        if (result.hasErrors()) {

            String error = result.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getDefaultMessage() + ",")
                    .collect(Collectors.joining());

            return new ResponseEntity<>(
                    new ResponseModel(HttpStatus.BAD_REQUEST, error),
                    HttpStatus.BAD_REQUEST);

        } else if (users.findByUsername(user.getUsername()) == null) {

            user.setPassword(encoder.encode(user.getPassword()));

            HttpHeaders headers = new HttpHeaders();

            PaynetUser newUser = users.save(user);

            headers.add("Location", "/v1/users/" + newUser.getId());

            return new ResponseEntity<>(
                    new ResponseModel(HttpStatus.CREATED, "User Created"),
                    headers, HttpStatus.CREATED);

        } else {

            return new ResponseEntity<>(
                    new ResponseModel(HttpStatus.CONFLICT,
                            "User with such username already exists"),
                    HttpStatus.CONFLICT);
        }
    }
}
