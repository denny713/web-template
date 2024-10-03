package com.ndp.service.impl;

import com.ndp.exception.BadRequestException;
import com.ndp.exception.ForbiddenException;
import com.ndp.exception.NotFoundException;
import com.ndp.model.dto.request.LoginDto;
import com.ndp.model.dto.request.UpdatePassUserDto;
import com.ndp.model.dto.response.AuthDto;
import com.ndp.model.dto.response.ResponseDto;
import com.ndp.model.entity.User;
import com.ndp.repository.UserRepository;
import com.ndp.repository.dao.UserDao;
import com.ndp.service.AuthService;
import com.ndp.token.JwtService;
import com.ndp.util.EncryptUtil;
import com.ndp.util.TokenUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    @Transactional
    public ResponseDto doLogin(LoginDto dto, HttpServletResponse response) {
        User user = getByUsername(dto.getUsername());
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

    @Override
    @Transactional
    public ResponseDto doLogout(HttpServletRequest request, HttpServletResponse response) {
        try {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return new ResponseDto(200, "Success", null);
    }

    @Override
    @Transactional
    public ResponseDto doChangePass(UpdatePassUserDto dto, HttpServletRequest request) {
        String token = TokenUtil.getToken(request);
        if (StringUtils.isEmpty(token)) {
            throw new BadRequestException("No Access Token was found");
        }

        if (StringUtils.isEmpty(dto.getOldPassword())) {
            throw new BadRequestException("Old password is empty");
        }

        if (StringUtils.isEmpty(dto.getNewPassword())) {
            throw new BadRequestException("New password is empty");
        }

        User user = getByUsername(jwtService.getUsername(token));
        String oldPass = EncryptUtil.encrypt(dto.getOldPassword());
        String newPass = EncryptUtil.encrypt(dto.getNewPassword());
        if (!Objects.equals(oldPass, user.getPassword())) {
            throw new BadRequestException("Old password is invalid");
        }

        if (Objects.equals(oldPass, newPass)) {
            throw new BadRequestException("New password cannot be same with old password");
        }

        user.setPassword(newPass);
        user.setMustChangePassword(false);
        userRepository.save(user);

        return new ResponseDto(200, "Success", user);
    }

    private User getByUsername(String username) {
        UserDao userDao = new UserDao();
        User user = userRepository.findOne(userDao.buildFindByUsername(username)).orElse(null);
        if (user == null) {
            throw new NotFoundException("User not found");
        }

        return user;
    }
}
