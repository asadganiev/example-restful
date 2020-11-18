package uz.paynet.rest.controllers.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import uz.paynet.rest.error.ResponseModel;
import uz.paynet.rest.forms.UserRegistrationForm;
import uz.paynet.rest.services.PasswordEncoderImpl;
import uz.paynet.rest.services.Users;
import uz.paynet.rest.users.PaynetUser;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1")
public class UsersController {

    private Users users;
    private PasswordEncoderImpl encoder;
    private DateTimeFormatter formatter;

    @Autowired
    public UsersController(Users users, PasswordEncoderImpl encoder) {

        this.users = users;
        this.encoder = encoder;
    }

    @PostMapping(value = "/users/signup",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> signUp(@Valid @RequestBody UserRegistrationForm user,
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

            PaynetUser newUser = users.save(toUserEntity(user));

            HttpHeaders headers = new HttpHeaders();
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

    @GetMapping(value = "/users/test",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> test() {

        return new ResponseEntity<>("test", HttpStatus.CONFLICT);
    }

   /* @PutMapping(value = "/users/update",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> update(@Valid @RequestBody UserUpdateForm userData,
                                         BindingResult result) {

        if (result.hasErrors()) {

            String error = result.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getDefaultMessage() + ",")
                    .collect(Collectors.joining());

            return new ResponseEntity<>(
                    new ResponseModel(HttpStatus.BAD_REQUEST, error),
                    HttpStatus.BAD_REQUEST);

        } else {

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            System.out.println(auth.getPrincipal());

            if (users.findByUsername(user.getUsername()) == null) {


                user.setPassword(encoder.encode(user.getPassword()));

                PaynetUser newUser = users.save(toUserEntity(user));

                HttpHeaders headers = new HttpHeaders();
                headers.add("Location", "/v1/users/" + newUser.getId());

                return new ResponseEntity<>(
                        new ResponseModel(HttpStatus.CREATED, "User Created"),
                        headers, HttpStatus.CREATED);

            }
        }
    }*/

    private PaynetUser toUserEntity(UserRegistrationForm userRegistrationForm) {

        try {
            PaynetUser user = new PaynetUser();

            user.setUsername(userRegistrationForm.getUsername());
            user.setPassword(userRegistrationForm.getPassword());
            user.setFirstName(userRegistrationForm.getFirstName());
            user.setLastName(userRegistrationForm.getLastName());
            user.setStreet(userRegistrationForm.getStreet());
            user.setRegion(userRegistrationForm.getRegion());
            user.setCity(userRegistrationForm.getCity());
            user.setZipCode(userRegistrationForm.getZipCode());

            formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            user.setBirthday(LocalDate.parse(userRegistrationForm.getBirthday(), formatter));

            return user;

        } catch (Exception e) {
            return null;
        }
    }
}
