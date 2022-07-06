package com.example.kmong_assignment.controller;

import com.example.kmong_assignment.domain.User;
import com.example.kmong_assignment.dto.UserLoginRequestDto;
import com.example.kmong_assignment.dto.UserSignUpRequestDto;
import com.example.kmong_assignment.dto.UserSignUpResponseDto;
import com.example.kmong_assignment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @PostMapping("/join")
    @ResponseBody
    public UserSignUpResponseDto join(@RequestBody UserSignUpRequestDto userSignUpRequestDto) {
        User joinUser = User.builder().
                email(userSignUpRequestDto.getEmail()).
                password(userSignUpRequestDto.getPassword()).
                build();

        userRepository.save(joinUser);

        return UserSignUpRequestDto.entityToDto(joinUser);
    }
    //
    //@PostMapping("/login")
    //@ResponseBody
    //public String login(@RequestBody UserLoginRequestDto userLoginRequestDto) {
    //
    //
    //}

}
