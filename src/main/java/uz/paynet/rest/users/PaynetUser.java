package uz.paynet.rest.users;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "user")
public class PaynetUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private String street;
    private String region;
    private String city;
    private int zipCode;
}