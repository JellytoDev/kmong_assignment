package com.example.kmong_assignment.controller;

import com.example.kmong_assignment.config.security.JwtTokenProvider;
import com.example.kmong_assignment.domain.User;
import com.example.kmong_assignment.dto.user.UserLoginRequestDto;
import com.example.kmong_assignment.dto.user.UserLoginResponseDto;
import com.example.kmong_assignment.dto.user.UserSignUpRequestDto;
import com.example.kmong_assignment.dto.user.UserSignUpResponseDto;
import com.example.kmong_assignment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipalNotFoundException;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signup")
    @ResponseBody
    public UserSignUpResponseDto join(@RequestBody UserSignUpRequestDto userSignUpRequestDto) {

        // 중복 체크

        User joinUser = User.builder().
                email(userSignUpRequestDto.getEmail()).
                password(passwordEncoder.encode(userSignUpRequestDto.getPassword())).
                build();

        userRepository.save(joinUser);

        return UserSignUpResponseDto.builder()
                .email(joinUser.getEmail())
                .id(joinUser.getId())
                .msg("success join")
                .code(0).build();
    }

    @PostMapping("/login")
    @ResponseBody
    public UserLoginResponseDto login(@RequestBody UserLoginRequestDto userLoginRequestDto) throws UserPrincipalNotFoundException {
        User user = userRepository.findByEmail(userLoginRequestDto.getEmail()).get();

        if (!passwordEncoder.matches(userLoginRequestDto.getPassword(), user.getPassword())) {
            throw new UserPrincipalNotFoundException("password 맞지 않음");
        }

        return UserLoginResponseDto.builder()
                .token(jwtTokenProvider.createToken(String.valueOf(user.getId()),user.getRoles()))
                .success(true)
                .code(0)
                .msg("login success")
                .build();
    }

    @GetMapping("/test")
    @ResponseBody
    public String test(@AuthenticationPrincipal User user) {
        //jwtTokenProvider.getAuthentication()
        System.out.println("user.toString() = " + user.getEmail());
        return "hello";
    }

}
