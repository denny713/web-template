package com.ndp.model.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UpdatePassUserDto {

    private UUID userId;
    private String oldPassword;
    private String newPassword;
}
