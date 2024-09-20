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


    @NotBlank
    @Size(min = 4, max = 255, message="Product name must be between {min} and {max} characters")
    private String name;

    @NotBlank
    @Size(min = 10, max = 255, message="Product description must be between {min} and {max} characters")
    private String description;

    //@NotBlank(message = "Price is required.")
    private double price;

    @Column(name = "stock_amount")
    private int stockAmount;

    // FIXIT @Default here?
    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category prodCategory;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "seller_id", nullable=false)
    private User seller;

    // TODO maybe don't want this here, since orderItems is a snapshot.
    // @OneToMany(...)
    // private Set<OrderItems> orderItems;
}
