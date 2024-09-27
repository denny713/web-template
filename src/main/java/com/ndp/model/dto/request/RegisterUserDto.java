package com.ndp.model.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RegisterUserDto {

    private String username;
    private String name;
    private String email;
    private UUID roleId;
}
