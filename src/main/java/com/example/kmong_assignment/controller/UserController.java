package com.example.kmong_assignment.controller;

import com.example.kmong_assignment.config.security.JwtTokenProvider;
import com.example.kmong_assignment.domain.User;
import com.example.kmong_assignment.dto.UserLoginRequestDto;
import com.example.kmong_assignment.dto.UserSignUpRequestDto;
import com.example.kmong_assignment.dto.UserSignUpResponseDto;
import com.example.kmong_assignment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.attribute.UserPrincipalNotFoundException;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/join")
    @ResponseBody
    public UserSignUpResponseDto join(@RequestBody UserSignUpRequestDto userSignUpRequestDto) {
        User joinUser = User.builder().
                email(userSignUpRequestDto.getEmail()).
                password(passwordEncoder.encode(userSignUpRequestDto.getPassword())).
                build();

        userRepository.save(joinUser);

        return UserSignUpRequestDto.entityToDto(joinUser);
    }

    @PostMapping("/login")
    @ResponseBody
    public String login(@RequestBody UserLoginRequestDto userLoginRequestDto) throws UserPrincipalNotFoundException {
        User user = userRepository.findByEmail(userLoginRequestDto.getEmail()).get();

        if (!passwordEncoder.matches(userLoginRequestDto.getPassword(), user.getPassword())) {
            throw new UserPrincipalNotFoundException("password 맞지 않음");
        }
        return jwtTokenProvider.createToken(String.valueOf(user.getId()),user.getRoles());
    }

}
