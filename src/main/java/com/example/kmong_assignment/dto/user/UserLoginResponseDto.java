package com.example.kmong_assignment.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
public class UserLoginResponseDto {
    private String msg;
    private Integer code;
    private Boolean success;
    private String token;

    @Builder
    public UserLoginResponseDto(String msg, Integer code, Boolean success, String token) {
        this.msg = msg;
        this.code = code;
        this.success = success;
        this.token = token;
    }
}
