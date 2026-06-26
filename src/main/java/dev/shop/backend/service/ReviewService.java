package dev.shop.backend.service;

import dev.shop.backend.domain.entities.ReviewEntity;

import java.util.List;
import java.util.Optional;

public interface ReviewService {

    ReviewEntity save(ReviewEntity reviewEntity);

    List<ReviewEntity> findAll();

    List<ReviewEntity> findReviewsFromUser(String email);

    List<ReviewEntity> findReviewsOfProduct(Long id);

    Optional<ReviewEntity> findOne(Long id);

    boolean isExists(Long id);

    ReviewEntity partialUpdate(Long id, ReviewEntity reviewEntity);

    void delete(Long id);
}
