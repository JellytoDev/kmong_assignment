package com.example.kmong_assignment.service;

import com.example.kmong_assignment.config.security.JwtTokenProvider;
import com.example.kmong_assignment.domain.User;
import com.example.kmong_assignment.repository.UserRepository;
import com.example.kmong_assignment.service.user.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;

import java.util.Optional;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("회원가입 잘 되는지 확인")
    public void 회원가입() {
        // given
        User user = User.builder().email("join_test").password("1234").build();

        // when
        User joinUser = userService.join(user.getEmail(), user.getPassword());
        Optional<User> userById = userRepository.findById(joinUser.getId());

        // then
        Assertions.assertThat(userById.isEmpty()).isFalse();
        Assertions.assertThat(user.getEmail()).isEqualTo(userById.get().getEmail());

    }

    @Test
    @DisplayName("회원가입 중복검사 : 메일 같은지 중복 체크")
    public void 회원가입_중복검사() {
        // given
        User originalUser = User.builder().email("join_check").password("1234").build();
        User sameUser = User.builder().email("join_check").password("5678").build();
        User diffUser = User.builder().email("join_check_123").password("1234").build();

        // when true : 중복없음, false : 중복있음
        User joinUser = userService.join(originalUser.getEmail(), originalUser.getPassword());
        Boolean joinCheckFalse = userService.joinCheck(sameUser.getEmail());
        Boolean joinCheckTrue = userService.joinCheck(diffUser.getEmail());

        // then
        Assertions.assertThat(joinCheckFalse).isFalse();
        Assertions.assertThat(joinCheckTrue).isTrue();

    }

    @Test
    @DisplayName("로그인 잘 되는지 확인")
    public void 로그인() {
        // given
        User user = User.builder().email("login_test").password("1234").build();
        userService.join(user.getEmail(),user.getPassword());

        // when
        Boolean loginCheckTrue = userService.loginCheck("login_test", "1234");
        Boolean loginCheckFalse = userService.loginCheck("login_test", "3431");

        // then
        Assertions.assertThat(loginCheckFalse).isFalse();
        Assertions.assertThat(loginCheckTrue).isTrue();
    }
}