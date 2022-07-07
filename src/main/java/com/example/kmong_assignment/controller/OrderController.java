package com.example.kmong_assignment.controller;

import com.example.kmong_assignment.domain.Order;
import com.example.kmong_assignment.domain.Product;
import com.example.kmong_assignment.domain.User;
import com.example.kmong_assignment.dto.order.*;
import com.example.kmong_assignment.repository.OrderProductRepository;
import com.example.kmong_assignment.repository.OrderRepository;
import com.example.kmong_assignment.repository.ProductRepository;
import com.example.kmong_assignment.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;

    private final OrderService orderService;

    @PostMapping("/list")
    @ResponseBody
    public List<ProductDto> list() {
        // db상 존재하는 판매물품 조회
        List<ProductDto> productDtoList = orderService.getProductList();

        return productDtoList;
    }

    @PostMapping("/request")
    @ResponseBody
    public OrderResponseDto order(@Valid @RequestBody List<OrderProductRequestDto> orderProductDtoList,
                                  @AuthenticationPrincipal User user) {

        // 주문 생성
        //Order order = orderService.createOrder(user);

        Order order = orderService.createOrder(user,orderProductDtoList);
        //orderService.addProductToOrder(order, orderProductDto);

        return OrderResponseDto.builder()
                .orderId(order.getId())
                .totalCount(order.getCount())
                .totalPrice(order.getPrice())
                .build();
    }

    @PostMapping("/search")
    @ResponseBody
    public List<OrderDto> search(@AuthenticationPrincipal User user) {
        ArrayList<OrderDto> list = orderService.getOrderListByUser(user);

        return list;
    }

    @PostConstruct
    public void init() {
        Product product = Product.builder().name("cake").price(10000).build();
        Product product1 = Product.builder().name("drink").price(4000).build();
        Product product2 = Product.builder().name("apple").price(1500).build();

        productRepository.save(product);
        productRepository.save(product1);
        productRepository.save(product2);
    }
}
