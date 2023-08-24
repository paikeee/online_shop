package ru.nicetu.online_shop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PictureDTO {

    private int pictureId;
    private String url;
    private int productId;

}
