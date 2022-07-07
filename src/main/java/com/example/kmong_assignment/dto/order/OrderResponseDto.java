package com.example.kmong_assignment.dto.order;

import lombok.Builder;
import lombok.Data;

@Data
public class OrderResponseDto {
    private Long orderId;
    private Integer totalPrice = 0;
    private Integer totalCount = 0;

    @Builder
    public OrderResponseDto(Long orderId, Integer totalPrice, Integer totalCount) {
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.totalCount = totalCount;
    }
}
