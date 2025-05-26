package com.example.project.dto;

import com.example.project.model.Users;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Users}
 */
@Value
@Getter
@Setter

public class UsersDto implements Serializable {
    String tel;
    String password;
    String name;
    String address;
    boolean status;
    String role;
    public UsersDto(String tel, String password, String name, String address, boolean status, String role) {

        this.tel = tel;
        this.password = password;
        this.name = name;
        this.address = address;
        this.status = status;
        this.role = role;
    }

    public String getTel() {
        return tel;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public boolean isStatus() {
        return status;
    }

    public String getRole() {
        return role;
    }
}