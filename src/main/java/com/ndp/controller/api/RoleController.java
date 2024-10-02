package com.ndp.controller.api;

import com.ndp.model.dto.request.RegisterRoleDto;
import com.ndp.model.dto.request.SearchRoleDto;
import com.ndp.model.dto.response.PageResponseDto;
import com.ndp.model.dto.response.ResponseDto;
import com.ndp.service.RoleService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/role")
public class RoleController {

    private final RoleService roleService;

    @PostMapping("/list")
    public ResponseEntity<PageResponseDto> searchRoles(@RequestBody @Valid SearchRoleDto dto) {
        return ResponseEntity.ok(roleService.searchRole(dto));
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> registerRole(@Valid @RequestBody RegisterRoleDto dto) {
        return ResponseEntity.ok(roleService.registerRole(dto));
    }

    @PostMapping("/options")
    public ResponseEntity<ResponseDto> roleOptions() {
        return ResponseEntity.ok(roleService.getAllRolesToOptions());
    }
}
