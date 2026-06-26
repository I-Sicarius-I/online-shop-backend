package dev.shop.backend.domain.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_id_seq")
    @Column(name = "id")
    private Long id;

    private String name;

    private String type;

    private String state;

    private Long quantity;

    private String description;

    private Double price;

    private Double rating;

    @ManyToOne
    @JoinColumn(name = "seller_id", referencedColumnName = "email")
    private UserEntity seller;

}
