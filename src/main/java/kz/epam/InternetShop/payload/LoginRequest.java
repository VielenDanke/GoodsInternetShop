package kz.epam.InternetShop.payload;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class LoginRequest {

    @Email
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
