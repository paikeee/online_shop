package ru.nicetu.online_shop.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.nicetu.online_shop.dto.request.CommentRequest;
import ru.nicetu.online_shop.models.Comment;
import ru.nicetu.online_shop.models.Person;
import ru.nicetu.online_shop.models.Picture;
import ru.nicetu.online_shop.models.Product;
import ru.nicetu.online_shop.repository.CommentRepository;
import ru.nicetu.online_shop.services.CommentServiceImpl;
import ru.nicetu.online_shop.services.PersonDetailsService;
import ru.nicetu.online_shop.services.PictureServiceImpl;
import ru.nicetu.online_shop.services.ProductServiceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PictureServiceImpl pictureService;

    @Mock
    private ProductServiceImpl productService;

    @Mock
    private PersonDetailsService personDetailsService;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    public void testSave() {
        Comment comment = new Comment();
        commentService.save(comment);
        verify(commentRepository).save(comment);
    }

    @Test
    public void testDelete() {
        Comment comment = new Comment();
        Picture picture = new Picture();
        comment.setPictures(Collections.singletonList(picture));

        when(commentRepository.findByCommentId(0)).thenReturn(Optional.of(comment));
        commentService.delete(0);
        verify(commentRepository).deleteById(0);
        verify(pictureService).delete(0);
    }

    @Test
    public void testGet() {
        Comment comment = new Comment();
        when(commentRepository.findByCommentId(0)).thenReturn(Optional.of(comment));
        assertEquals(comment, commentService.get(0));
    }

    @Test
    public void testAddComment() {
        CommentRequest request = new CommentRequest(5, "text");
        Product product = new Product();
        Person person = new Person();
        Comment expected = new Comment(5, "text", product, person);

        when(pictureService.save(new ArrayList<>())).thenReturn(new ArrayList<>());
        when(productService.getProduct(0)).thenReturn(product);
        when(personDetailsService.currentUser()).thenReturn(person);
        assertEquals(expected, commentService.addComment(0, request, new ArrayList<>()));
        verify(commentRepository).save(expected);
    }
}
