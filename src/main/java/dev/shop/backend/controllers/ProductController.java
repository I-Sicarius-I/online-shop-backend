package dev.shop.backend.controllers;

import dev.shop.backend.domain.dto.ProductDTO;
import dev.shop.backend.domain.dto.UserDTO;
import dev.shop.backend.domain.entities.ProductEntity;
import dev.shop.backend.domain.entities.UserEntity;
import dev.shop.backend.exceptions.InvalidProductOwnerException;
import dev.shop.backend.mappers.impl.ProductMapper;
import dev.shop.backend.service.ProductService;
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
public class ProductController {

    private final ProductService productService;

    private final ProductMapper productMapper;


    @PostMapping("/products")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        UserDTO userDTO = UserDTO.builder()
                .email(email)
                .build();

        productDTO.setSeller(userDTO);

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

    @GetMapping(value = "/products", params = "email")
    public ResponseEntity<List<ProductDTO>> findAllProductsByUser(@RequestParam("email") String email){

        List<ProductEntity> productEntities = productService.findProductsByUser(email);

        return new ResponseEntity<>(
                productEntities
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


       Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(!productService.existsBySellerId(id, auth.getName())){
            throw new InvalidProductOwnerException("Product owner does not match current user.");
        }

        UserDTO userDTO = UserDTO.builder()
                .email(auth.getName())
                .build();

        productDTO.setSeller(userDTO);
        ProductEntity productToModify = productMapper.mapFrom(productDTO);
        ProductEntity updatedEntity = productService.partialUpdate(id, productToModify);

        return new ResponseEntity<>(productMapper.mapTo(updatedEntity), HttpStatus.OK);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long id){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        if(!productService.existsBySellerId(id, email) && productService.isExists(id)){
           throw new InvalidProductOwnerException("Product owner does not match current user.");
        }
        productService.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
