package com.example.kmong_assignment.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
public class UserSignUpResponseDto {
    private Long id;
    private String email;
    private String msg;
    private Integer code;

    @Builder
    public UserSignUpResponseDto(Long id, String email, String msg, Integer code) {
        this.id = id;
        this.email = email;
        this.msg = msg;
        this.code = code;
    }
}
