package ru.nicetu.online_shop.services;

import org.springframework.web.multipart.MultipartFile;
import ru.nicetu.online_shop.dto.request.CommentRequest;
import ru.nicetu.online_shop.models.Comment;

import java.util.List;

public interface CommentService {

    void save(Comment comment);

    void delete(int id);

    Comment get(int id);

    Comment addComment(int productId, CommentRequest request, List<MultipartFile> files);

}
