package com.example.rental.exceptions;

import ch.qos.logback.core.status.ErrorStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiResponse
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

    private ErrorStatus status;
    private final LocalDateTime timestamp;
    private String message;


    private ApiError() {
        timestamp = LocalDateTime.now();
    }


    public ApiError(ErrorStatus status, String message) {
        this();
        this.status = status;
        this.message = message;

    }
}
