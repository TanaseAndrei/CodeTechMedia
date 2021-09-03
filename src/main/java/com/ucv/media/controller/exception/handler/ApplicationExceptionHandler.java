package com.ucv.media.controller.exception.handler;

import com.ucv.media.controller.exception.AppException;
import com.ucv.media.controller.exception.converter.AppExceptionConverter;
import com.ucv.media.controller.exception.dto.AppExceptionDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@AllArgsConstructor
public class ApplicationExceptionHandler {

    private final AppExceptionConverter appExceptionConverter;

    @ExceptionHandler(value = {AppException.class})
    public AppExceptionDto handleAppException(AppException appException) {
        return appExceptionConverter.exceptionToDto(appException);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public AppExceptionDto handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) {
        return appExceptionConverter.validationExceptionToDto(methodArgumentNotValidException);
    }
}
