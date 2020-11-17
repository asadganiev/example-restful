package uz.paynet.rest.controllers.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.paynet.rest.dto.UserDto;
import uz.paynet.rest.error.ResponseModel;
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
    public ResponseEntity<Object> signUp(@Valid @RequestBody UserDto user,
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

            PaynetUser newUser = users.save(dtoToUser(user));

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

    private PaynetUser dtoToUser(UserDto userDto) {

        try {
            PaynetUser user = new PaynetUser();

            user.setUsername(userDto.getUsername());
            user.setPassword(userDto.getPassword());
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setStreet(userDto.getStreet());
            user.setRegion(userDto.getRegion());
            user.setCity(userDto.getCity());
            user.setZipCode(userDto.getZipCode());

            formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            user.setBirthday(LocalDate.parse(userDto.getBirthday(), formatter));

            return user;

        } catch (Exception e) {
            return null;
        }
    }
}
