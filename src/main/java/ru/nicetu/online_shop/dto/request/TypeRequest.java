package ru.nicetu.online_shop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@Getter
public class TypeRequest {

    @NotEmpty
    private String typeName;

    @NotNull
    private int parentId;

    private List<Integer> productList;

}
