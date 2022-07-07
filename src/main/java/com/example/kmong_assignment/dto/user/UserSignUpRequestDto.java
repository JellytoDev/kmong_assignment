package com.example.kmong_assignment.dto.user;

import com.example.kmong_assignment.domain.User;
import lombok.Data;

@Data
public class UserSignUpRequestDto {
    private String email;
    private String password;

    //public static UserSignUpResponseDto entityToDto(User user){
    //    UserSignUpResponseDto userSignUpResponseDto = new UserSignUpResponseDto();
    //    userSignUpResponseDto.setEmail(user.getEmail());
    //
    //    userSignUpResponseDto.setId(user.getId());
    //    return userSignUpResponseDto;
    //};
}
