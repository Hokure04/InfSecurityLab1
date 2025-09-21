package com.example.infsecuritylab1.dto;

import com.example.infsecuritylab1.model.Role;
import com.example.infsecuritylab1.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.text.StringEscapeUtils;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String firstName;
    private String lastName;
    private String email;
    private Role role;

    public static UserDto fromEntity(User user){
        return UserDto.builder()
                .firstName(StringEscapeUtils.escapeHtml4(user.getFirstName()))
                .lastName(StringEscapeUtils.escapeHtml4(user.getLastName()))
                .email(StringEscapeUtils.escapeHtml4(user.getEmail()))
                .role(user.getRole())
                .build();
    }
}