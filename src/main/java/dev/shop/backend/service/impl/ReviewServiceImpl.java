package dev.shop.backend.service.impl;

import dev.shop.backend.domain.entities.ReviewEntity;
import dev.shop.backend.domain.repositories.ReviewRepository;
import dev.shop.backend.service.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository){
        this.reviewRepository = reviewRepository;
    }

    @Override
    public ReviewEntity save(ReviewEntity reviewEntity){
        return reviewRepository.save(reviewEntity);
    }

    @Override
    public List<ReviewEntity> findAll(){
        return StreamSupport.stream(
                reviewRepository
                        .findAll()
                        .spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ReviewEntity> findOne(Long id){
        return reviewRepository.findById(id);
    }

    @Override
    public boolean isExists(Long id){
        return reviewRepository.existsById(id);
    }

    @Override
    public ReviewEntity partialUpdate(Long id, ReviewEntity reviewEntity){
        return reviewRepository.findById(id).map(
                existingReview -> {
                    Optional.ofNullable(reviewEntity.getText()).ifPresent(existingReview::setText);
                    Optional.ofNullable(reviewEntity.getRating()).ifPresent(existingReview::setRating);
                    return reviewRepository.save(existingReview);
                }
        ).orElseThrow(() -> new RuntimeException("Review not found"));
    }

    @Override
    public void delete(Long id){
        reviewRepository.deleteById(id);
    }
}
