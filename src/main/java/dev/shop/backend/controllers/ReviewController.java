package dev.shop.backend.controllers;

import dev.shop.backend.domain.dto.ReviewDTO;
import dev.shop.backend.domain.dto.UserDTO;
import dev.shop.backend.domain.entities.OrderEntity;
import dev.shop.backend.domain.entities.ProductEntity;
import dev.shop.backend.domain.entities.ReviewEntity;
import dev.shop.backend.exceptions.InvalidReviewOwnerException;
import dev.shop.backend.exceptions.InvalidReviewerException;
import dev.shop.backend.mappers.impl.ReviewMapper;
import dev.shop.backend.service.OrderService;
import dev.shop.backend.service.ProductService;
import dev.shop.backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final OrderService orderService;
    private final ProductService productService;
    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;

    @PostMapping("/reviews")
    public ResponseEntity<ReviewDTO> createReview(@RequestBody ReviewDTO reviewDTO){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        reviewDTO.setReviewerId(email);

        if(!orderService.existsByBuyerIdAndProductId(email, reviewDTO.getProductId())){
            throw new InvalidReviewerException("User cannot make a review without buying a product.");
        }

        ReviewEntity reviewEntity = reviewMapper.mapFrom(reviewDTO);
        ReviewEntity savedReview = reviewService.save(reviewEntity);

        Integer length = reviewService.findReviewsOfProduct(savedReview.getProductId()).size();

        productService.updateRating(savedReview.getProductId(), savedReview.getRating(), length);

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

    @GetMapping(value = "/reviews", params = "productId")
    public ResponseEntity<List<ReviewDTO>> listReviewsOfProduct(@RequestParam("productId") Long id){

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

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(!reviewService.existsByReviewerId(id, auth.getName()))
        {
            throw new InvalidReviewOwnerException("Reviewer does not match current user.");
        }

        reviewDTO.setReviewerId(auth.getName());

        ReviewEntity reviewEntity = reviewMapper.mapFrom(reviewDTO);
        ReviewEntity updatedEntity = reviewService.partialUpdate(id, reviewEntity);

        Integer length = reviewService.findReviewsOfProduct(updatedEntity.getProductId()).size();
        productService.updateRating(updatedEntity.getProductId(), updatedEntity.getRating(), length);

        return new ResponseEntity<>(reviewMapper.mapTo(updatedEntity), HttpStatus.OK);
    }

    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<ReviewDTO> deleteReview(@PathVariable Long id){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(reviewService.isExists(id) && !reviewService.existsByReviewerId(id, auth.getName()))
        {
            throw new InvalidReviewOwnerException("Reviewer does not match current user.");
        }

        Optional<ReviewEntity> review = reviewService.findOne(id);


        review.ifPresent(reviewEntity -> {
            Integer length = reviewService.findReviewsOfProduct(reviewEntity.getProductId()).size();
            productService.updateRating(reviewEntity.getProductId(), -1 * reviewEntity.getRating(), length);
        });

        reviewService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
