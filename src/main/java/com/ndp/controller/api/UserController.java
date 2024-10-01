package com.ndp.controller.api;

import com.ndp.model.dto.request.RegisterUserDto;
import com.ndp.model.dto.request.SearchUserDto;
import com.ndp.model.dto.request.UpdatePassUserDto;
import com.ndp.model.dto.response.PageResponseDto;
import com.ndp.model.dto.response.ResponseDto;
import com.ndp.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> registerUser(@Valid @RequestBody RegisterUserDto dto) {
        return ResponseEntity.ok(userService.registerUser(dto));
    }

    @PostMapping("/password/reset")
    public ResponseEntity<ResponseDto> resetPass(@Valid @RequestBody UpdatePassUserDto dto) {
        return ResponseEntity.ok(userService.resetPass(dto));
    }

    @PostMapping("/password/update")
    public ResponseEntity<ResponseDto> updatePass(@Valid @RequestBody UpdatePassUserDto dto) {
        return ResponseEntity.ok(userService.updatePass(dto));
    }

    @PostMapping("/delete")
    public ResponseEntity<ResponseDto> deleteUser(@Valid @RequestBody UpdatePassUserDto dto) {
        return ResponseEntity.ok(userService.deleteUser(dto));
    }

    @PostMapping("/reactivate")
    public ResponseEntity<ResponseDto> reactivateUser(@Valid @RequestBody UpdatePassUserDto dto) {
        return ResponseEntity.ok(userService.statusUpdate(dto, false));
    }

    @PostMapping("/deactivate")
    public ResponseEntity<ResponseDto> deactivateUser(@Valid @RequestBody UpdatePassUserDto dto) {
        return ResponseEntity.ok(userService.statusUpdate(dto, true));
    }

    @PostMapping("/list")
    public ResponseEntity<PageResponseDto> searchUser(@Valid @RequestBody SearchUserDto dto) {
        return ResponseEntity.ok(userService.searchUser(dto));
    }
}
