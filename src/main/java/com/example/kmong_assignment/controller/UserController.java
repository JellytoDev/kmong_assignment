package com.example.kmong_assignment.controller;

import com.example.kmong_assignment.config.security.JwtTokenProvider;
import com.example.kmong_assignment.domain.User;
import com.example.kmong_assignment.dto.user.UserLoginRequestDto;
import com.example.kmong_assignment.dto.user.UserLoginResponseDto;
import com.example.kmong_assignment.dto.user.UserSignUpRequestDto;
import com.example.kmong_assignment.dto.user.UserSignUpResponseDto;
import com.example.kmong_assignment.repository.UserRepository;
import com.example.kmong_assignment.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @PostMapping("/signup")
    @ResponseBody
    public UserSignUpResponseDto join(@Valid @RequestBody UserSignUpRequestDto userSignUpRequestDto) {

        // 중복 체크
        // true : 중복없음, false : 중복있음
        Boolean isExist = userService.joinCheck(userSignUpRequestDto.getEmail());

        // 회원가입
        if(!isExist){
            return UserSignUpResponseDto.builder()
                    .msg("email exist")
                    .code(1).build();
        }

        User joinUser = userService.join(userSignUpRequestDto.getEmail(), userSignUpRequestDto.getPassword());

        return UserSignUpResponseDto.builder()
                .email(joinUser.getEmail())
                .id(joinUser.getId())
                .msg("success join")
                .code(0).build();
    }

    @PostMapping("/login")
    @ResponseBody
    public UserLoginResponseDto login(@Valid @RequestBody UserLoginRequestDto userLoginRequestDto){

        // 로그인 체크
        if (!userService.loginCheck(userLoginRequestDto.getEmail(),userLoginRequestDto.getPassword())) {
            return UserLoginResponseDto.builder()
                    .success(false)
                    .code(1)
                    .msg("login fail")
                    .build();
        }

        // 로그인
        User user = userService.login(userLoginRequestDto.getEmail());

        return UserLoginResponseDto.builder()
                .token(jwtTokenProvider.createToken(String.valueOf(user.getId()),user.getRoles()))
                .success(true)
                .code(0)
                .msg("login success")
                .build();
    }

    @PostMapping("/login/check")
    @ResponseBody
    public Boolean loginCheck(@RequestBody Map<String,String> req) {
        return userService.joinCheck(req.get("email"));
    }

}
