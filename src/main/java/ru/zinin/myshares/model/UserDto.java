package ru.zinin.myshares.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    public UserDto(String username, String token) {
        this.username = username;
        this.token = token;
    }

    private String username;
    private String token;
    private String email;
}
