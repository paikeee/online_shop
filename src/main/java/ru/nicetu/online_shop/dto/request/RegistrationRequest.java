package ru.nicetu.online_shop.dto.request;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
public class RegistrationRequest {

    @NotEmpty(message = "Insert email")
    @Email(message = "Wrong format of email")
    private final String email;

    @NotEmpty(message = "Insert password")
    @Size(min = 6, message = "Min size of password is 6")
    private final String password;

    @NotEmpty(message = "Insert name")
    @Size(min = 2, max = 50, message = "Name size is 2 to 50")
    private final String name;

    @NotEmpty(message = "Insert surname")
    @Size(min = 2, max = 50, message = "Surname size is 2 to 50")
    private final String surname;

}