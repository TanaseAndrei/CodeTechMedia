package com.ucv.media.controller.exception.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class ValidationExceptionDto extends AppExceptionDto {

    private Map<String, String> fieldsWithErrors;
}
