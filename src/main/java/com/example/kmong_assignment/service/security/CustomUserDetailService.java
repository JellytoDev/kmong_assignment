package com.example.kmong_assignment.service.security;

import com.example.kmong_assignment.domain.User;
import com.example.kmong_assignment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username){
        //System.out.println("username = " + username);

        User user = userRepository.findById(Long.valueOf(username)).get();
        //System.out.println("user.toString() = " + user.toString());
        return user;
    }

}
