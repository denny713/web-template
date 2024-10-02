package com.ndp.service;

import com.ndp.model.dto.request.RegisterUserDto;
import com.ndp.model.dto.request.SearchUserDto;
import com.ndp.model.dto.request.UpdatePassUserDto;
import com.ndp.model.dto.request.UpdateUserDto;
import com.ndp.model.dto.response.PageResponseDto;
import com.ndp.model.dto.response.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Transactional
public interface UserService {

    ResponseDto getProfile(HttpServletRequest request);

    ResponseDto registerUser(RegisterUserDto dto);

    ResponseDto updateUser(UpdateUserDto dto);

    ResponseDto resetPass(UpdatePassUserDto dto);

    ResponseDto updatePass(UpdatePassUserDto dto);

    ResponseDto deleteUser(UpdatePassUserDto dto);

    ResponseDto statusUpdate(UpdatePassUserDto dto, boolean isActive);

    PageResponseDto searchUser(SearchUserDto dto);
}
