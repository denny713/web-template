package com.ndp.model.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class PageRequestDto {

    protected Integer page;
    protected Integer size;
    protected String sortBy;
    protected Sort.Direction sort;
}
