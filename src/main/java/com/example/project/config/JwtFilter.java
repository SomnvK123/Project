package com.example.project.config;

import com.example.project.model.Users;
import com.example.project.repository.UsersRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends org.springframework.web.filter.OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsersRepository usersRepository;

    private final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String path = request.getServletPath();

        if (shouldNotFilter(path)) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7).trim();

            if (token.isEmpty() || token.split("\\.").length != 3) {
                logger.error("Token không hợp lệ (rỗng hoặc sai định dạng): '{}'", token);
                chain.doFilter(request, response);
                return;
            }

            try {
                String username = jwtUtil.extractUsername(token);
                if (username != null && jwtUtil.validateToken(token, username)) {
                    Users user = usersRepository.findByName(username);
                    if (user != null) {
                        List<SimpleGrantedAuthority> authorities =
                                List.of(new SimpleGrantedAuthority(user.getRole()));
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(user, null, authorities);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        request.setAttribute("user", user);
                        logger.info("User '{}' đã xác thực thành công", username);
                    }
                }
            } catch (Exception e) {
                logger.error("Lỗi xác thực token: ", e);
            }
        } else {
            logger.warn("Không tìm thấy header Authorization hoặc thiếu Bearer");
        }


        chain.doFilter(request, response);
    }

    private boolean shouldNotFilter(String path) {
        return path.startsWith("/users/login") || path.startsWith("/users/register");
    }
}