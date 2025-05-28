package com.example.project.userdetail;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {
    @Getter
    private int id;
    private String tel;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(int id, String tel, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.tel = tel;
        this.password = password;
        this.authorities = authorities;
    }

    @Override
    public String getUsername() { return tel; }

    @Override
    public String getPassword() { return password; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
