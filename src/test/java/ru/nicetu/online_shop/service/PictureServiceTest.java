package ru.nicetu.online_shop.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.nicetu.online_shop.models.Picture;
import ru.nicetu.online_shop.repository.PictureRepository;
import ru.nicetu.online_shop.services.PictureServiceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class PictureServiceTest {

    @Mock
    private PictureRepository pictureRepository;

    @InjectMocks
    private PictureServiceImpl pictureService;

    @Test
    public void testSave() {
        List<MultipartFile> files = new ArrayList<>();
        files.add(new MockMultipartFile("1", "", "", new byte[10]));
        assertEquals(new ArrayList<>(), pictureService.save(files));

        files.add(new MockMultipartFile("1", new byte[1]));
        assertThrows(RuntimeException.class, () -> pictureService.save(files));

        List<MultipartFile> files1 = Collections.singletonList(
                new MockMultipartFile("image", "image", "image", new byte[10])
        );
        Picture picture = new Picture(new byte[10]);
        assertEquals(Collections.singletonList(picture), pictureService.save(files1));
        verify(pictureRepository).save(picture);
    }

    @Test
    public void testDeletePicture() {
        pictureService.delete(0);
        verify(pictureRepository).deleteById(0);
    }
}
