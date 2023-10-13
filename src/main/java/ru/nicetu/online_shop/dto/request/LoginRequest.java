package ru.nicetu.online_shop.dto.request;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
public class LoginRequest {

    @Email(message = "Wrong email format")
    @NotEmpty(message = "Insert email")
    private final String email;

    @NotEmpty(message = "Insert password")
    private final String password;
}
