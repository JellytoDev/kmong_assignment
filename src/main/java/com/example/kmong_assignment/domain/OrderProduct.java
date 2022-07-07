package com.example.kmong_assignment.domain;

import lombok.Builder;
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

    @Column(name = "ORDER_PRODUCT_NAME")
    private String name;

    @Column(name = "ORDER_PRODUCT_COUNT")
    private Integer count;

    @Column(name = "ORDER_PRODUCT_PRICE")
    private Integer orderPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    public void setOrder(Order order) {
        this.order = order;
    }

    @Builder
    public OrderProduct(Long id, String name, Integer count, Integer orderPrice, Order order) {
        this.id = id;
        this.name = name;
        this.count = count;
        this.orderPrice = orderPrice;
        this.order = order;
    }
}
