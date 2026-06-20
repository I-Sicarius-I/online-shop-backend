package dev.shop.backend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDTO {

    private Long id;

    private String text;

    private Integer rating;

    private ProductDTO product;

    private UserDTO reviewer;
}
