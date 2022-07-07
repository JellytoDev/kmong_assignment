package com.example.kmong_assignment.controller;

import com.example.kmong_assignment.domain.Order;
import com.example.kmong_assignment.domain.OrderProduct;
import com.example.kmong_assignment.domain.Product;
import com.example.kmong_assignment.domain.User;
import com.example.kmong_assignment.dto.order.*;
import com.example.kmong_assignment.repository.OrderProductRepository;
import com.example.kmong_assignment.repository.OrderRepository;
import com.example.kmong_assignment.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;

    @PostMapping("/list")
    @ResponseBody
    public List<ProductDto> list() {
        List<ProductDto> productDtoList = new ArrayList<>();
        List<Product> products = productRepository.findAll();

        for (Product product : products) {
            productDtoList.add(ProductDto.entityToDto(product));
        }

        return productDtoList;
    }

    @PostMapping("/request")
    @ResponseBody
    public OrderResponseDto order(@RequestBody List<OrderProductRequestDto> orderProductDtoList,
                                  @AuthenticationPrincipal User user) {

        Order order = new Order();
        order.setUser(user);
        orderRepository.save(order);

        // orderProduct
        for (OrderProductRequestDto orderProductDto : orderProductDtoList) {
            Product product = productRepository.findById(orderProductDto.getId()).get();
            OrderProduct orderProduct = Product.toOrderProduct(order,product,orderProductDto.getCount());

            order.addOrderProduct(orderProduct);
            order.addPriceAndCount(orderProduct.getOrderPrice(),orderProduct.getCount());

            orderProductRepository.save(orderProduct);
        }

        return OrderResponseDto.builder()
                .orderId(order.getId())
                .totalCount(order.getCount())
                .totalPrice(order.getPrice())
                .build();
    }

    @PostMapping("/search")
    @ResponseBody
    public List<OrderDto> search(@AuthenticationPrincipal User user) {
        ArrayList<OrderDto> list = new ArrayList<>();

        List<Order> orderList = orderRepository.findAllByUser(user);

        for (Order order : orderList) {
            OrderDto orderDto = OrderDto.entityToDto(order);
            for (OrderProduct orderProduct : order.getOrderProductList()) {
                orderDto.addOrderProductList(OrderProductDto.entityToDto(orderProduct));
            }
            list.add(orderDto);
        }

        return list;
    }

    @PostConstruct
    public void init() {
        Product product = Product.builder().name("cake").price(10000).build();
        Product product1 = Product.builder().name("drink").price(4000).build();

        productRepository.save(product);
        productRepository.save(product1);
    }
}
