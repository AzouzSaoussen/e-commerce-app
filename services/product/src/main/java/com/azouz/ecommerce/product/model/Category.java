package com.azouz.ecommerce.product.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_seq_gen")
    @SequenceGenerator(
            name = "category_seq_gen",
            sequenceName = "category_seq",
            allocationSize = 50
    )
    private Integer id;
    private String name;
    private String description;
    @OneToMany(mappedBy= "category", cascade = CascadeType.REMOVE)
    private List<Product> products;
}
