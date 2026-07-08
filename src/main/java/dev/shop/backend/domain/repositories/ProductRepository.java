package dev.shop.backend.domain.repositories;

import dev.shop.backend.domain.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    @Query(value = "SELECT p FROM ProductEntity p WHERE p.sellerId = :seller_id")
    public List<ProductEntity> findProductsBySellerId(@Param("seller_id") String seller_id);

    @Query(value = "SELECT EXISTS(SELECT p FROM ProductEntity p WHERE p.sellerId = :seller_id AND p.id = :id)")
    public boolean existsBySellerId(@Param("seller_id") String seller_id, @Param("id") Long id);
}
