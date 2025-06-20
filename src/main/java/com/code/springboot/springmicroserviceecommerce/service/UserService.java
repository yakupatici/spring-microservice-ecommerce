package com.code.springboot.springmicroserviceecommerce.service;

import com.code.springboot.springmicroserviceecommerce.dto.UserDto;
import com.code.springboot.springmicroserviceecommerce.model.User;

public interface UserService {
    void saveUser(UserDto userDto);

    User findByEmail(String email);
} 