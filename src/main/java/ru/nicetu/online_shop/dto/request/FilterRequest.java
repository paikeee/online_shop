package ru.nicetu.online_shop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FilterRequest {

    private List<Integer> valuesId;

    @NotNull
    private int min;

    @NotNull
    private int max;

    @NotNull
    private double rating;

}
