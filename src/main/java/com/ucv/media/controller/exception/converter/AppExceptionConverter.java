package com.ucv.media.controller.exception.converter;

import com.ucv.media.controller.exception.AppException;
import com.ucv.media.controller.exception.dto.AppExceptionDto;
import com.ucv.media.controller.exception.dto.ValidationExceptionDto;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
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

    public ValidationExceptionDto validationExceptionToDto(MethodArgumentNotValidException methodArgumentNotValidException) {
        ValidationExceptionDto validationExceptionDto = new ValidationExceptionDto();
        validationExceptionDto.setHttpCode(HttpStatus.BAD_REQUEST.value());
        validationExceptionDto.setMessage(VALIDATION_EXCEPTION_MESSAGE);
        Map<String, String> fieldsWithErrors = new LinkedHashMap<>();
        methodArgumentNotValidException.getBindingResult().getAllErrors().forEach(objectError -> {
            populateMapWithError(fieldsWithErrors, objectError);
        });
        validationExceptionDto.setFieldsWithErrors(fieldsWithErrors);
        validationExceptionDto.setThrownTime(LocalDateTime.now());
        return validationExceptionDto;
    }

    private void populateMapWithError(Map<String, String> fieldsWithErrors, ObjectError objectError) {
        String fieldName = ((FieldError) objectError).getField();
        String errorMessage = objectError.getDefaultMessage();
        fieldsWithErrors.put(fieldName, errorMessage);
    }
}
