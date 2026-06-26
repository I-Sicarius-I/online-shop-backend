package dev.shop.backend.controllers;

import dev.shop.backend.domain.dto.ReviewDTO;
import dev.shop.backend.domain.entities.ReviewEntity;
import dev.shop.backend.mappers.impl.ReviewMapper;
import dev.shop.backend.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;

    public ReviewController(ReviewService reviewService, ReviewMapper reviewMapper){
        this.reviewService = reviewService;
        this.reviewMapper = reviewMapper;
    }

    @PostMapping("/reviews")
    public ResponseEntity<ReviewDTO> createReview(@RequestBody ReviewDTO reviewDTO){

        ReviewEntity reviewEntity = reviewMapper.mapFrom(reviewDTO);
        ReviewEntity savedReview = reviewService.save(reviewEntity);

        return new ResponseEntity<>(reviewMapper.mapTo(savedReview), HttpStatus.CREATED);
    }

    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewDTO>> listReviews(){

        return new ResponseEntity<>(reviewService.findAll()
                .stream()
                .map(reviewMapper::mapTo)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping(value = "/reviews", params = "email")
    public ResponseEntity<List<ReviewDTO>> listReviewsByUser(@RequestParam("email") String email){

        List<ReviewEntity> reviewEntities = reviewService.findReviewsFromUser(email);

        return new ResponseEntity<>(
                reviewEntities
                        .stream()
                        .map(reviewMapper::mapTo)
                        .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping(value = "/reviews", params = "product_id")
    public ResponseEntity<List<ReviewDTO>> listReviewsOfProduct(@RequestParam("product_id") Long id){

        List<ReviewEntity> reviewEntities = reviewService.findReviewsOfProduct(id);

        return new ResponseEntity<>(
                reviewEntities
                        .stream()
                        .map(reviewMapper::mapTo)
                        .collect(Collectors.toList()), HttpStatus.OK
        );
    }

    @GetMapping("/reviews/{id}")
    public ResponseEntity<ReviewDTO> getReview(@PathVariable Long id){

        Optional<ReviewEntity> foundReview = reviewService.findOne(id);

        return foundReview.map( reviewEntity -> {
                    ReviewDTO reviewDTO = reviewMapper.mapTo(reviewEntity);
                    return new ResponseEntity<>(reviewDTO, HttpStatus.OK);
                }
        ).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/reviews/{id}")
    public ResponseEntity<ReviewDTO> partialUpdateReview(@PathVariable Long id, @RequestBody ReviewDTO reviewDTO){

        if(!reviewService.isExists(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ReviewEntity reviewEntity = reviewMapper.mapFrom(reviewDTO);
        ReviewEntity updatedEntity = reviewService.partialUpdate(id, reviewEntity);

        return new ResponseEntity<>(reviewMapper.mapTo(updatedEntity), HttpStatus.OK);
    }

    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<ReviewDTO> deleteReview(@PathVariable Long id){

        reviewService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
