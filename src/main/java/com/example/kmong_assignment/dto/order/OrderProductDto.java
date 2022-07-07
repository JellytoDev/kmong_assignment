package com.example.kmong_assignment.dto.order;

import com.example.kmong_assignment.domain.OrderProduct;
import lombok.Data;


@Data
public class OrderProductDto {
    private String name;
    private Integer count;
    private Integer orderPrice;

    public static OrderProductDto entityToDto(OrderProduct orderProduct) {
        OrderProductDto orderProductDto = new OrderProductDto();
        orderProductDto.setName(orderProduct.getName());
        orderProductDto.setCount(orderProduct.getCount());
        orderProductDto.setOrderPrice(orderProduct.getOrderPrice());
        return orderProductDto;
    }
}
