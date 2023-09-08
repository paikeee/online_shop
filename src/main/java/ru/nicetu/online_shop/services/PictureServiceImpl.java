package ru.nicetu.online_shop.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.nicetu.online_shop.models.Comment;
import ru.nicetu.online_shop.models.Picture;
import ru.nicetu.online_shop.models.Product;
import ru.nicetu.online_shop.repository.PictureRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class PictureServiceImpl implements PictureService {

    private final PictureRepository pictureRepository;

    @Override
    @Transactional
    public void save(List<MultipartFile> files, Product product) {
        if (files.size() == 1 && Objects.equals(files.get(0).getOriginalFilename(), "")) {
            return;
        }
        for (MultipartFile file : files) {
            try {
                Picture picture = new Picture(file.getBytes(), product);
                pictureRepository.save(picture);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    @Override
    @Transactional
    public List<Picture> save(List<MultipartFile> files, Comment comment) {
        List<Picture> pictures = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                Picture picture = new Picture(file.getBytes(), comment);
                pictures.add(picture);
                pictureRepository.save(picture);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return pictures;
    }


    @Override
    @Transactional
    public void delete(int id) {
        pictureRepository.deleteById(id);
    }
}
