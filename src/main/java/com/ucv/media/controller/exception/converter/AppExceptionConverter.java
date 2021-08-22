package com.ucv.media.controller.exception.converter;

import com.ucv.media.controller.exception.AppException;
import com.ucv.media.controller.exception.dto.AppExceptionDto;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.time.LocalDateTime;
import java.util.Map;

@NoArgsConstructor
@Component
public class AppExceptionConverter {

    private static final String VALIDATION_EXCEPTION_MESSAGE = "There are fields that are not valid";

    public AppExceptionDto exceptionToDto(AppException appException) {
        AppExceptionDto appExceptionDto = new AppExceptionDto();
        appExceptionDto.setHttpCode(appException.getHttpStatus().value());
        appExceptionDto.setMessage(appException.getMessage());
        appExceptionDto.setThrownTime(LocalDateTime.now());
        return appExceptionDto;
    }

    private void populateMapWithError(Map<String, String> fieldsWithErrors, ObjectError objectError) {
        String fieldName = ((FieldError) objectError).getField();
        String errorMessage = objectError.getDefaultMessage();
        fieldsWithErrors.put(fieldName, errorMessage);
    }
}
