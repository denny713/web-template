package com.ndp.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageResponseDto {

    private int code;
    private String status;
    private Object data;
    private int draw;
    private long recordsTotal;
    private long recordsFiltered;
}
