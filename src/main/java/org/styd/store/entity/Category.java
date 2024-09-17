package org.styd.store.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Please enter a name for the category.")
    private String name;

    private Boolean isDeleted;

    //FIXME eager or lazy? Hashset needed? TEST
    @OneToMany(mappedBy = "prodCategory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Product> products = new HashSet<>();
}
