package com.ndp.controller.api;

import com.ndp.model.dto.request.LoginDto;
import com.ndp.model.dto.response.ResponseDto;
import com.ndp.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ResponseDto> doLogin(@Valid @RequestBody LoginDto dto, HttpServletResponse response) {
        return ResponseEntity.ok(authService.doLogin(dto, response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseDto> doLogout(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(authService.doLogout(request, response));
    }
}
