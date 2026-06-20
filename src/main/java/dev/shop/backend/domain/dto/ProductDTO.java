package dev.shop.backend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
    private Long id;

    private String name;

    private String type;

    private String state;

    private Long quantity;

    private String description;

    private Double price;

    private Double rating;

    private UserDTO seller;
}
