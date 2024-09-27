package com.ndp.model.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchUserDto extends PageRequestDto {

    private String username;
    private String name;
    private String email;
    private String role;
    private boolean active;
}
