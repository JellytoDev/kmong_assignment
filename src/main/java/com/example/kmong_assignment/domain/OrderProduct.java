package com.example.kmong_assignment.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "ORDER_PRODUCT")
public class OrderProduct {
    @Id
    @GeneratedValue
    @Column(name = "ORDER_PRODUCT_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    @Column(name = "ORDER_PRODUCT_COUNT")
    private Integer count;

    @Column(name = "ORDER_PRODUCT_PRICE")
    private Integer orderPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    private Order order;
}
