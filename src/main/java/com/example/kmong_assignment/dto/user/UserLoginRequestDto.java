package com.example.kmong_assignment.dto.user;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserLoginRequestDto {
    @NotBlank(message = "email을 입력해주세요")
    @Length(min = 1,max = 50,message = "사이즈를 확인해주세요")
    private String email;

    @NotBlank(message = "password를 입력해주세요")
    private String password;
}
