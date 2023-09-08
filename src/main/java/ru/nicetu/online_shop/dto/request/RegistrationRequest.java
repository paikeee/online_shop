package ru.nicetu.online_shop.dto.request;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class RegistrationRequest {

    @Email(message = "Неверный формат EMail")
    private final String email;

    @NotEmpty(message = "Поле не может быть пустым")
    @Size(min = 6, message = "Пароль должен состоять минимум из 6 символов")
    private final String password;

    @NotEmpty(message = "Поле не может быть пустым")
    @Size(min=2, max=50, message = "Длина имени от 2 до 50 символов")
    private final String name;

    @NotEmpty(message = "Поле не может быть пустым")
    @Size(min=2, max=50, message = "Длина фамилии от 2 до 50 символов")
    private final String surname;

    private Set<String> role;
}