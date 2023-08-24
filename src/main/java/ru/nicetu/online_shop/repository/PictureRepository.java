package ru.nicetu.online_shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.nicetu.online_shop.models.Picture;

import java.util.Optional;

@Repository
@Transactional
public interface PictureRepository extends JpaRepository<Picture, Integer> {

    Optional<Picture> findByPictureId(int id);

}
