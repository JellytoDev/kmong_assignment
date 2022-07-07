package com.example.kmong_assignment.service.order;

import com.example.kmong_assignment.domain.Order;
import com.example.kmong_assignment.domain.OrderProduct;
import com.example.kmong_assignment.domain.Product;
import com.example.kmong_assignment.domain.User;
import com.example.kmong_assignment.dto.order.OrderDto;
import com.example.kmong_assignment.dto.order.OrderProductDto;
import com.example.kmong_assignment.dto.order.OrderProductRequestDto;
import com.example.kmong_assignment.dto.order.ProductDto;
import com.example.kmong_assignment.repository.OrderProductRepository;
import com.example.kmong_assignment.repository.OrderRepository;
import com.example.kmong_assignment.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;

    @Override
    public List<ProductDto> getProductList() {
        List<ProductDto> productDtoList = new ArrayList<>();
        List<Product> products = productRepository.findAll();

        for (Product product : products) {
            productDtoList.add(ProductDto.entityToDto(product));
        }
        return productDtoList;
    }

    @Override
    public Order createOrder(User user,List<OrderProductRequestDto> orderProductDtoList) {
        Order order = new Order();
        order.setUser(user);
        orderRepository.save(order);

        // 주문 물품 추가
        for (OrderProductRequestDto orderProductDto : orderProductDtoList) {

            Product product = productRepository.findById(orderProductDto.getId()).get();
            OrderProduct orderProduct = Product.toOrderProduct(order,product, orderProductDto.getCount());

            order.addOrderProduct(orderProduct);
            order.addPriceAndCount(orderProduct.getOrderPrice(),orderProduct.getCount());

            orderProductRepository.save(orderProduct);
        }
        return order;
    }

    @Override
    public ArrayList<OrderDto> getOrderListByUser(User user) {
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
}
