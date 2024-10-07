package com.ndp.service.impl;

import com.ndp.model.dto.response.ResponseDto;
import com.ndp.model.entity.Menu;
import com.ndp.repository.MenuRepository;
import com.ndp.service.MenuService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    @Override
    @Transactional
    public ResponseDto getAllMenusToOptions() {
        List<Menu> menus = menuRepository.findAll();

        List<Map<String, String>> menuOptions = new ArrayList<>();
        menus.forEach(x -> {
            Map<String, String> menuOption = new HashMap<>();
            menuOption.put("key", x.getId().toString());
            menuOption.put("value", x.getName());
            menuOptions.add(menuOption);
        });

        return new ResponseDto(200, "Success", menuOptions);
    }

    @Override
    @Transactional
    public ResponseDto getMenusToOptions() {
        List<Menu> menus = menuRepository.findAll();

        List<Map<String, String>> menuOptions = new ArrayList<>();
        menus.forEach(x -> {
            Map<String, String> menuOption = new HashMap<>();
            menuOption.put("key", x.getId().toString());
            menuOption.put("value", x.getUrl());
            menuOptions.add(menuOption);
        });

        return new ResponseDto(200, "Success", menuOptions);
    }
}
