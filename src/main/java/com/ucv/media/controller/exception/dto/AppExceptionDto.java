package com.ucv.media.controller.exception.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Setter
@Getter
public class AppExceptionDto {

    private static final String SIMPLE_DATE_TIME_FORMATTER = "yyyy-MM-dd hh:mm:ss";
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(SIMPLE_DATE_TIME_FORMATTER);
    private int httpCode;
    private String message;
    private String thrownTime;

    public void setThrownTime(LocalDateTime localDateTime) {
        this.thrownTime = localDateTime.format(dateTimeFormatter);
    }
}
