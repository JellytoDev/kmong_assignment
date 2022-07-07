package com.example.kmong_assignment.dto.order;

import com.example.kmong_assignment.domain.Order;
import lombok.Data;

import java.util.ArrayList;

@Data
public class OrderDto {
    private Integer totalPrice;
    private Integer totalCount;
    private ArrayList<OrderProductDto> orderProductList = new ArrayList<>();

    public void addOrderProductList(OrderProductDto orderProductDto) {
        orderProductList.add(orderProductDto);
    }

    public static OrderDto entityToDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setTotalCount(order.getCount());
        orderDto.setTotalPrice(order.getPrice());
        return orderDto;
    }
}
