package uz.paynet.rest.controllers.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import uz.paynet.rest.error.ResponseModel;
import uz.paynet.rest.forms.UserRegistrationForm;
import uz.paynet.rest.forms.UserUpdateForm;
import uz.paynet.rest.services.PasswordEncoderImpl;
import uz.paynet.rest.services.Users;
import uz.paynet.rest.users.PaynetUser;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
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

    @GetMapping(value = "/users/{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getUser(@PathVariable("userId") Long userId) {

        final Optional<PaynetUser> user = users.getUserById(userId);

        return user.<ResponseEntity<Object>>map(
                paynetUser ->
                        new ResponseEntity<>(paynetUser, HttpStatus.CONFLICT))
                .orElseGet(()
                        -> new ResponseEntity<>(new ResponseModel(HttpStatus.NOT_FOUND,
                        "User with such id doesn't exist."), HttpStatus.NOT_FOUND));
    }

    @PutMapping(value = "/users/update",
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

            final String username = getUsername();

            if (users.update(toUserEntity(username, userData)) != null) {

                return new ResponseEntity<>(HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private String getUsername() {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }

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

    private PaynetUser toUserEntity(String username, UserUpdateForm updateForm) {

        try {
            PaynetUser user = new PaynetUser();

            user.setUsername(username);
            user.setPassword(updateForm.getPassword());
            user.setFirstName(updateForm.getFirstName());
            user.setLastName(updateForm.getLastName());
            user.setStreet(updateForm.getStreet());
            user.setRegion(updateForm.getRegion());
            user.setCity(updateForm.getCity());
            user.setZipCode(updateForm.getZipCode());

            formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            user.setBirthday(LocalDate.parse(updateForm.getBirthday(), formatter));

            return user;

        } catch (Exception e) {
            return null;
        }
    }
}
