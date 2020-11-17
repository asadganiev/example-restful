package uz.paynet.rest.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ResponseModel {

    private HttpStatus status;
    private String description;
}
