package dev.shop.backend.domain.repositories;

import dev.shop.backend.domain.entities.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    @Query("SELECT r FROM ReviewEntity r WHERE r.reviewer.email = :email")
    public List<ReviewEntity> findReviewsByReviewerId(@Param("email") String email);

    @Query("SELECT r FROM ReviewEntity r WHERE r.product.id = :id")
    public List<ReviewEntity> findReviewsByProductId(@Param("id") Long id);
}
