package com.ndp.service.impl;

import com.ndp.exception.ForbiddenException;
import com.ndp.exception.NotFoundException;
import com.ndp.model.dto.request.LoginDto;
import com.ndp.model.dto.response.AuthDto;
import com.ndp.model.dto.response.ResponseDto;
import com.ndp.model.entity.User;
import com.ndp.repository.UserRepository;
import com.ndp.service.AuthService;
import com.ndp.token.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public ResponseDto doLogin(LoginDto dto, HttpServletResponse response) {
        User user = userRepository.findByUsername(dto.getUsername()).orElse(null);
        if (user == null) {
            throw new NotFoundException("User not found");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );

        if (!authentication.isAuthenticated()) {
            throw new ForbiddenException("Username or password not valid");
        }

        AuthDto result = new AuthDto(
                jwtService.generateAccessToken(user),
                "Bearer",
                3600,
                jwtService.generateRefreshToken(user)
        );

        Cookie cookie = new Cookie("access-token", result.getAccessToken());
        cookie.setMaxAge(60 * 60 * 24);
        cookie.setPath("/");
        response.addCookie(cookie);

        return new ResponseDto(200, "Success", result);
    }
}
