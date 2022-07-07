package com.example.kmong_assignment.service.order;

import com.example.kmong_assignment.domain.Order;
import com.example.kmong_assignment.domain.User;
import com.example.kmong_assignment.dto.order.OrderDto;
import com.example.kmong_assignment.dto.order.OrderProductRequestDto;
import com.example.kmong_assignment.dto.order.ProductDto;

import java.util.ArrayList;
import java.util.List;

public interface OrderService {
    List<ProductDto> getProductList();

    Order createOrder(User user, List<OrderProductRequestDto> orderProductDtoList);

    ArrayList<OrderDto> getOrderListByUser(User user);
}
