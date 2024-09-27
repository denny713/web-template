package com.ndp.service;

import com.ndp.model.dto.request.RegisterUserDto;
import com.ndp.model.dto.request.SearchUserDto;
import com.ndp.model.dto.request.UpdatePassUserDto;
import com.ndp.model.dto.response.PageResponseDto;
import com.ndp.model.dto.response.ResponseDto;
import jakarta.transaction.Transactional;

@Transactional
public interface UserService {

    ResponseDto registerUser(RegisterUserDto dto);

    ResponseDto resetPass(UpdatePassUserDto dto);

    ResponseDto updatePass(UpdatePassUserDto dto);

    ResponseDto deleteUser(UpdatePassUserDto dto);

    PageResponseDto searchUser(SearchUserDto dto);
}
