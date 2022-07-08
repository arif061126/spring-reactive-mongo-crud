package com.spring.reactive.mongo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "product") //for reactive mongo db
public class Product {
    @Id
    private String id;
    private String name;
    private Integer quantity;
    private Double price;
}
