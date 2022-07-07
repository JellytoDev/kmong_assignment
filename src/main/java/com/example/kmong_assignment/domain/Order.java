package com.example.kmong_assignment.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name="ORDER_TABLE")
public class Order {
    @Id
    @GeneratedValue
    @Column(name = "ORDER_ID")
    private Long id;

    @Column(name = "ORDER_PRICE")
    private Integer price = 0;

    @Column(name = "ORDER_COUNT")
    private Integer count = 0;

    @JoinColumn(name = "USER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<OrderProduct> orderProductList = new ArrayList<>();

    public void addOrderProduct(OrderProduct orderProduct) {
        orderProductList.add(orderProduct);
    }

    public void addPriceAndCount(Integer price, Integer count) {
        this.price += price;
        this.count += count;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
