package com.example.kmong_assignment.dto.order;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class OrderProductRequestDto {
    @NotBlank(message = "주문하는 물품 Id를 입력해주세요")
    private Long id;

    @Min(value = 1,message = "1개 이상 주문해야 합니다")
    @Positive
    @NotBlank(message = "주문 수량을 입력해주세요")
    private Integer count;
}
