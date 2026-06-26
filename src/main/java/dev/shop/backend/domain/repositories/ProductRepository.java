package dev.shop.backend.domain.repositories;

import dev.shop.backend.domain.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    @Query(value = "SELECT p FROM ProductEntity p WHERE p.seller.email = :seller_id")
    public List<ProductEntity> findProductsBySellerId(@Param("seller_id") String seller_id);
}
