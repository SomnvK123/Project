package com.example.project.dto;

import com.example.project.model.Users;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO for {@link Users}
 */

@Setter
@Getter
public class AuthDto implements Serializable {
    private String token;
    String tel;
    String password;
    String name;
    String address;
    boolean status;
    String role;

    public AuthDto(String token, String tel, String password, String name, String address, boolean status, String role) {
        this.token = token;
        this.tel = tel;
        this.password = password;
        this.name = name;
        this.address = address;
        this.status = status;
        this.role = role;
    }

}