package com.bigtree.user.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.UUID;

@RestControllerAdvice
@Slf4j
public class ApiErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleSystemError(Exception e){
      log.error("System Exception occurred. {}", e.getMessage());
      ApiError error = ApiError.builder()
              .reference(UUID.randomUUID().toString())
              .title("Unexpected error occurred")
              .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
              .detail(e.getMessage())
              .build();
      return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiError> handleApiError(ApiException e){
        log.error("API Exception occurred. {}", e.getMessage());
        ApiError error = ApiError.builder()
                .reference(UUID.randomUUID().toString())
                .title(e.status.getReasonPhrase())
                .status(e.getStatus().value())
                .detail(e.getMessage()).build();
        return new ResponseEntity<>(error, e.status);
    }
}
