package dev.shop.backend.domain.repositories;

import dev.shop.backend.domain.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    @Query("SELECT o FROM OrderEntity o WHERE o.buyerId = :email")
    public List<OrderEntity> findOrdersByBuyerId(@Param("email") String email);

    @Query("SELECT o FROM OrderEntity o WHERE o.productId = :id")
    public List<OrderEntity> findOrdersByProductId(@Param("id") Long id);

    @Query("SELECT EXISTS(SELECT o FROM OrderEntity o WHERE o.id = :id AND o.buyerId = :buyer_id)")
    public boolean existsByBuyerId(@Param("id") Long id, @Param("buyer_id") String buyer_id);

    @Query("SELECT EXISTS (SELECT o FROM OrderEntity o WHERE o.buyerId = :b_id AND o.productId = :p_id)")
    public boolean existsByBuyerIdAndProductId(@Param("b_id") String buyerId, @Param("p_id") Long productId);
}
