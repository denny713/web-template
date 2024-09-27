package com.ndp.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

    private String username;
    private String name;
    private String email;
    private String role;
    private String roleDescription;
    private boolean active;
    private Date createdDate;
    private Date updatedDate;
}
