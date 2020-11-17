package uz.paynet.rest.validators;

import uz.paynet.rest.annotations.ValidBirthday;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.SimpleDateFormat;

public class BirthdayConstraintValidator implements ConstraintValidator<ValidBirthday, String> {

    private final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

    public void initialize(ValidBirthday constraint) {

    }

    public boolean isValid(String birthday, ConstraintValidatorContext context) {

        try {
            format.parse(birthday);
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
