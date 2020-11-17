package uz.paynet.rest.users;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "user")
public class PaynetUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "username should not be blank")
    @Size(min = 2, max = 50, message = "username length should be 2-50")
    private String username;
    @NotBlank(message = "password should not be blank")
    @Size(min = 8, max = 50, message = "password length should be 8-50")
    private String password;
    @NotBlank(message = "firstname should not be blank")
    @Size(min= 2, max = 50, message = "firstname length should be 2-50")
    private String firstName;
    @NotBlank(message = "lastname should not be blank")
    @Size(min= 2, max = 50, message = "lastname length should be 2-50")
    private String lastName;
    @NotBlank(message = "street should not be blank")
    @Size(min= 2, max = 100, message = "street length should be 2-100")
    private String street;
    @NotBlank(message = "region should not be blank")
    @Size(min= 2, max = 100, message = "region length should be 2-100")
    private String region;
    @NotBlank(message = "city should not be blank")
    @Size(min= 2, max = 100, message = "city length should be 2-100")
    private String city;
    private int zipCode;
}