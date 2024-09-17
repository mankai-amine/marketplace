package org.styd.store.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="products")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FIXME @JoinColumn ONETOONE
    @NotBlank
    private Long categoryId;

    // FIXME @JOINCOLUMN
    @NotBlank
    private Long sellerId;

    @NotBlank
    @Size(min = 4, max = 255, message="Product name must be between {min} and {max} characters")
    private String name;

    @NotBlank
    @Size(min = 10, max = 255, message="Product description must be between {min} and {max} characters")
    private String description;

    @NotBlank(message = "Price is required.")
    private Double price;

    @NotBlank(message = "Amount of stock is required.")
    private Integer stockAmount;

    // FIXIT @Default here?
    private String imageUrl;

    private Boolean isDeleted;

    // @ManyToOne(fetch = FetchType.EAGER, optional = false)
    // @JoinColumn(name = "user_id", nullable=false)
    // private User seller;

    // TODO maybe don't want this here, since orderItems is a snapshot.
    // @OneToMany(...)
    // private Set<OrderItems> orderItems;
}
