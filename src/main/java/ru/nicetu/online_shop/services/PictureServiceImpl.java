package ru.nicetu.online_shop.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.nicetu.online_shop.models.Picture;
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
    public List<Picture> save(List<MultipartFile> files) {
        if (files.size() == 1 && Objects.equals(files.get(0).getOriginalFilename(), "")) {
            return new ArrayList<>();
        }
        List<Picture> pictures = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.getContentType() != null && !file.getContentType().startsWith("image")) {
                throw new RuntimeException("Detached file is not an image");
            }
        }
        for (MultipartFile file : files) {
            try {
                Picture picture = new Picture(file.getBytes());
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
