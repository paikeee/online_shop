package ru.nicetu.online_shop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentResponse {

    private int commentId;
    private int productId;
    private int rating;
    private String text;

}
