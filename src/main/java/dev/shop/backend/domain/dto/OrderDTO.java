package dev.shop.backend.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO {
    private Long id;

    private Long quantity;

    private Date dateOrdered;

    private Date dateShipped;

    private Date dateReceived;

    private UserDTO buyer;

    private ProductDTO product;
}
