package ru.nicetu.online_shop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@AllArgsConstructor
@Getter
public class OrderRequest {

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    private List<OrderProductRequest> orderProducts;

}
