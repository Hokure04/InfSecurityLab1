package com.example.infsecuritylab1.controller;

import com.example.infsecuritylab1.dto.UserDto;
import com.example.infsecuritylab1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DataController {

    @Autowired
    private UserService userService;

    @GetMapping("/data")
    public List<UserDto> getAllUsers(){
        return userService.getAllUsers()
                .stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
    }
}
