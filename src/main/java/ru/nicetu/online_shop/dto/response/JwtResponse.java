package ru.nicetu.online_shop.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private int id;
    private String email;
    private List<String> roles;

    public JwtResponse(String accessToken, int id,  String email, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.email = email;
        this.roles = roles;
    }

}
