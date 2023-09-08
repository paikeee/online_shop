package ru.nicetu.online_shop.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.nicetu.online_shop.dto.request.CommentRequest;
import ru.nicetu.online_shop.models.Comment;
import ru.nicetu.online_shop.repository.CommentRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@AllArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PictureService pictureService;
    private final ProductServiceImpl productService;
    private final PersonDetailsService personDetailsService;

    @Override
    @Transactional
    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void delete(int id) {
        get(id).getPictures()
                .forEach(it -> pictureService.delete(it.getPictureId()));
        commentRepository.deleteById(id);
    }

    @Override
    public Comment get(int id) {
        return commentRepository.findByCommentId(id)
                .orElseThrow(() -> new NoSuchElementException("Comment with id " + id + " was not found"));
    }

    @Override
    @Transactional
    public Comment addComment(int productId, CommentRequest request, List<MultipartFile> files) {
        Comment comment = new Comment(
                request.getRating(),
                request.getText(),
                productService.findById(productId),
                personDetailsService.currentUser()
        );
        save(comment);
        if (files.size() == 1 && Objects.equals(files.get(0).getOriginalFilename(), "")) {
            comment.setPictures(pictureService.save(files, comment));
        }
        return comment;
    }
}
