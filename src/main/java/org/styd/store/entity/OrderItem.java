package org.styd.store.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="order_items")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "price")
    private double price;

    @Column(name = "amount")
    private int amount;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "order_id", nullable=false)
    private Order order;
}

