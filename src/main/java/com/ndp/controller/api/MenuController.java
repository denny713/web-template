package com.ndp.controller.api;

import com.ndp.model.dto.response.ResponseDto;
import com.ndp.service.MenuService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/menu")
public class MenuController {

    private final MenuService menuService;

    @PostMapping("/options")
    public ResponseEntity<ResponseDto> roleOptions() {
        return ResponseEntity.ok(menuService.getAllMenusToOptions());
    }
}
