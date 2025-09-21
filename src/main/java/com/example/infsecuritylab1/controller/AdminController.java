package com.example.infsecuritylab1.controller;

import com.example.infsecuritylab1.dto.UserDto;
import com.example.infsecuritylab1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    @Autowired
    private UserService userService;

    @PostMapping("/promote/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDto promoteUser(@PathVariable Long id){
        return UserDto.fromEntity(userService.promoteToAdmin(id));
    }

    @PostMapping("/block/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDto blockUser(@PathVariable Long id){
        return UserDto.fromEntity(userService.blockUser(id));
    }
}
