package ru.nicetu.online_shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nicetu.online_shop.models.Picture;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Integer> {

}
