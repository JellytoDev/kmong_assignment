package com.example.kmong_assignment.dto.user;

import com.example.kmong_assignment.domain.User;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UserSignUpRequestDto {

    @NotBlank(message = "이메일은 필수 값입니다")
    private String email;

    @NotBlank(message = "비밀번호는 필수값입니다")
    private String password;
}
