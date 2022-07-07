package com.example.kmong_assignment.service.user;

import com.example.kmong_assignment.domain.User;

public interface UserService {

    Boolean joinCheck(String email);
    User join(String email, String password);

    Boolean loginCheck(String email, String password);
    User login(String email);
}
