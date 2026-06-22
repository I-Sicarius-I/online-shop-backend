package dev.shop.backend.service.impl;

import dev.shop.backend.domain.entities.ProductEntity;
import dev.shop.backend.domain.repositories.ProductRepository;
import dev.shop.backend.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @Override
    public ProductEntity save(ProductEntity productEntity){
        return productRepository.save(productEntity);
    }

    @Override
    public List<ProductEntity> findAll(){
        return StreamSupport.stream(
                productRepository
                        .findAll()
                        .spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductEntity> findOne(Long id){
        return productRepository.findById(id);
    }

    @Override
    public boolean isExists(Long id)
    {
        return productRepository.existsById(id);
    }

    @Override
    public ProductEntity partialUpdate(Long id, ProductEntity productEntity){
        productEntity.setId(id);

        return productRepository.findById(id).map(
                existingProduct -> {
                    Optional.ofNullable(productEntity.getName()).ifPresent(existingProduct::setName);
                    Optional.ofNullable(productEntity.getType()).ifPresent(existingProduct::setType);
                    Optional.ofNullable(productEntity.getState()).ifPresent(existingProduct::setState);
                    Optional.ofNullable(productEntity.getQuantity()).ifPresent(existingProduct::setQuantity);
                    Optional.ofNullable(productEntity.getDescription()).ifPresent(existingProduct::setDescription);
                    Optional.ofNullable(productEntity.getPrice()).ifPresent(existingProduct::setPrice);
                    Optional.ofNullable(productEntity.getRating()).ifPresent(existingProduct::setRating);
                    return productRepository.save(existingProduct);
                }
        ).orElseThrow(() -> new RuntimeException("Product does not exist"));
    }

    @Override
    public void delete(Long id)
    {
        productRepository.deleteById(id);
    }
}
