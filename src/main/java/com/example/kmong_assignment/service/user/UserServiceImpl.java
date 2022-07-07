package com.example.kmong_assignment.service.user;

import com.example.kmong_assignment.config.security.JwtTokenProvider;
import com.example.kmong_assignment.domain.User;
import com.example.kmong_assignment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public User join(String email,String password) {
        User joinUser = User.builder().
                email(email).
                password(passwordEncoder.encode(password))
                .build();

        userRepository.save(joinUser);
        return joinUser;
    }

    @Override
    public Boolean loginCheck(String email, String password) {
        User user = userRepository.findByEmail(email).get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            return false;
        }else{
            return true;
        }
    }

    @Override
    public User login(String email) {

        return userRepository.findByEmail(email).get();
    }

    @Override
    public Boolean joinCheck(String email) {

        System.out.println("email = " + email);
        Optional<User> emailExist = userRepository.findByEmail(email);

        if (emailExist.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
}
