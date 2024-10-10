package com.ndp.model.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePassUserDto {

    private long userId;
    private String oldPassword;
    private String newPassword;
}
