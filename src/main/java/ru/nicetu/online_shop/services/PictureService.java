package ru.nicetu.online_shop.services;

import org.springframework.web.multipart.MultipartFile;
import ru.nicetu.online_shop.models.Picture;

import java.util.List;

public interface PictureService {

    List<Picture> save(List<MultipartFile> files);


    void delete(int id);

}
