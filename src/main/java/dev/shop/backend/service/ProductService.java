package dev.shop.backend.service;

import dev.shop.backend.domain.entities.ProductEntity;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    ProductEntity save(ProductEntity productEntity);

    List<ProductEntity> findAll();

    List<ProductEntity> findProductsByUser(String email);

    Optional<ProductEntity> findOne(Long id);

    boolean isExists(Long id);

    boolean existsBySellerId(Long prod_id, String email);

    ProductEntity partialUpdate(Long id, ProductEntity productEntity);

    void delete(Long id);
}
