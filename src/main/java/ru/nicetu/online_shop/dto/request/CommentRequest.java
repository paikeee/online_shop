package ru.nicetu.online_shop.dto.request;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Getter
public class CommentRequest {

    @Min(value = 1)
    @Max(value = 5)
    private int rating;

    @Size(max = 255, message = "Max comment length is 255")
    private String text;

}
