package com.ndp.service;

import com.ndp.model.dto.request.LoginDto;
import com.ndp.model.dto.request.UpdatePassUserDto;
import com.ndp.model.dto.response.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

@Transactional
public interface AuthService {

    ResponseDto doLogin(LoginDto dto, HttpServletResponse response);

    ResponseDto doLogout(HttpServletRequest request, HttpServletResponse response);

    ResponseDto doChangePass(UpdatePassUserDto dto, HttpServletRequest request);
}
