package com.example.kmong_assignment.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "PRODUCT")
public class Product {
    @Id
    @GeneratedValue
    @Column(name = "PRODUCT_ID")
    private Long id;

    @Column(name = "PRODUCT_NAME")
    private String name;

    @Column(name = "PRODUCT_PRICE")
    private Integer price;
}
