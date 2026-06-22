package dev.shop.backend.controllers;

import dev.shop.backend.domain.dto.ProductDTO;
import dev.shop.backend.domain.entities.ProductEntity;
import dev.shop.backend.mappers.impl.ProductMapper;
import dev.shop.backend.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class ProductController {

    private final ProductService productService;

    private final ProductMapper productMapper;

    public ProductController(ProductService productService, ProductMapper productMapper){
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @PostMapping("/products")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO){

        ProductEntity productEntity = productMapper.mapFrom(productDTO);
        ProductEntity savedProduct = productService.save(productEntity);

        return new ResponseEntity<>(productMapper.mapTo(savedProduct), HttpStatus.CREATED);
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> listProducts(){

        List<ProductEntity> productEntities = productService.findAll();

        return new ResponseEntity<>(productEntities
                .stream()
                .map(productMapper::mapTo)
                .collect(Collectors.toList()), HttpStatus.OK
        );
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id){

        Optional<ProductEntity> foundProduct = productService.findOne(id);

        return foundProduct.map(
                productEntity -> {
                    ProductDTO productDTO = productMapper.mapTo(productEntity);
                    return new ResponseEntity<>(productDTO, HttpStatus.OK);
                }
        ).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/products/{id}")
    public ResponseEntity<ProductDTO> partialUpdateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO){

        if(!productService.isExists(id))
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        productDTO.setId(id);

        ProductEntity productEntity = productMapper.mapFrom(productDTO);
        ProductEntity updatedEntity = productService.partialUpdate(id, productEntity);

        return new ResponseEntity<>(productMapper.mapTo(productEntity), HttpStatus.OK);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long id){

        productService.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
