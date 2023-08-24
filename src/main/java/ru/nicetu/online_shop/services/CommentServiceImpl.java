package ru.nicetu.online_shop.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nicetu.online_shop.models.Comment;
import ru.nicetu.online_shop.repository.CommentRepository;

import java.util.NoSuchElementException;

@AllArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PictureService pictureService;

    @Override
    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
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
}
