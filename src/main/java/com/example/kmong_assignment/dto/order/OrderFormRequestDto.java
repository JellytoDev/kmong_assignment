package com.example.kmong_assignment.dto.order;

import lombok.Data;

import java.util.ArrayList;

@Data
public class OrderFormRequestDto {
    private ArrayList<OrderProductRequestDto> orderProductDtoList;
}
