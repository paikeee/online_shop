package ru.nicetu.online_shop.services;

import ru.nicetu.online_shop.models.Comment;

public interface CommentService {

    void save(Comment comment);

    void delete(int id);

    Comment get(int id);

}
