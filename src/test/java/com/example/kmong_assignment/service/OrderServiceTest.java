package com.example.kmong_assignment.service;

import com.example.kmong_assignment.domain.Order;
import com.example.kmong_assignment.domain.Product;
import com.example.kmong_assignment.domain.User;
import com.example.kmong_assignment.dto.order.OrderProductRequestDto;
import com.example.kmong_assignment.dto.order.ProductDto;
import com.example.kmong_assignment.repository.OrderRepository;
import com.example.kmong_assignment.repository.ProductRepository;
import com.example.kmong_assignment.service.order.OrderService;
import com.example.kmong_assignment.service.user.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class OrderServiceTest {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    @Test
    public void 상품조회() {
        //given
        Product product1 = Product.builder().name("배게").price(12000).build();
        Product product2 = Product.builder().name("이불").price(20000).build();
        productRepository.save(product1);
        productRepository.save(product2);

        //when
        List<ProductDto> productList = orderService.getProductList();

        //then
        Assertions.assertThat(productList.size()).isEqualTo(2);
    }

    @Test
    public void 상품주문() {
        //given
        User orderUser = userService.join("orderUser", "1234");
        Product product1 =  Product.builder().name("프라이펜").price(8000).build();
        Product product2 =  Product.builder().name("냄비").price(6000).build();
        productRepository.save(product1);
        productRepository.save(product2);

        //when
        List<OrderProductRequestDto> orderProductDtoList = new ArrayList<>();

        orderProductDtoList.add(OrderProductRequestDto.builder()
                .id(product1.getId()).count(2).build());

        orderProductDtoList.add(OrderProductRequestDto.builder()
                .id(product2.getId()).count(3).build());

        Order order = orderService.createOrder(orderUser, orderProductDtoList);

        //then
        Assertions.assertThat(order.getCount()).isEqualTo(5);
        Assertions.assertThat(order.getPrice()).isEqualTo(2 * 8000 + 3 * 6000);

    }

    @Test
    public void 회원주문_내역조회() {
        //given
        User orderUser1 = userService.join("orderUser1", "1234");
        User orderUser2 = userService.join("orderUser2", "1234");

        Product product1 =  Product.builder().name("프라이펜").price(8000).build();
        Product product2 =  Product.builder().name("냄비").price(6000).build();
        productRepository.save(product1);
        productRepository.save(product2);

        //when
        List<OrderProductRequestDto> orderProductDtoList = new ArrayList<>();

        orderProductDtoList.add(OrderProductRequestDto.builder()
                .id(product1.getId()).count(2).build());

        Order order1 = orderService.createOrder(orderUser1, orderProductDtoList);

        orderProductDtoList.add(OrderProductRequestDto.builder()
                .id(product2.getId()).count(3).build());

        Order order2 = orderService.createOrder(orderUser2, orderProductDtoList);

        //then
        Assertions.assertThat(order1.getCount()).isEqualTo(2);
        Assertions.assertThat(order2.getCount()).isEqualTo(5);
    }
}