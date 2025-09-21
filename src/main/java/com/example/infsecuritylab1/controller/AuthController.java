package com.example.infsecuritylab1.controller;

import com.example.infsecuritylab1.service.AuthService;
import com.example.infsecuritylab1.dto.ApplicationResponseDto;
import com.example.infsecuritylab1.dto.LoginDto;
import com.example.infsecuritylab1.dto.RegistrationDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/sign-up")
    public ApplicationResponseDto signUp(@RequestBody RegistrationDto request){
        request.setFirstName(StringEscapeUtils.escapeHtml4(request.getFirstName()));
        request.setLastName(StringEscapeUtils.escapeHtml4(request.getLastName()));
        return authService.signUp(request);
    }

    @PostMapping("/login")
    public ApplicationResponseDto login(@RequestBody LoginDto request){
        return authService.login(request);
    }
}
