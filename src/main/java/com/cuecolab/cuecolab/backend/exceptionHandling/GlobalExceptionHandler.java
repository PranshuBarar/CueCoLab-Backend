package com.cuecolab.cuecolab.backend.exceptionHandling;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<String> handleRateLimitExceededException(RequestNotPermitted ex) {
        System.out.println("Rate limit exceeded");
        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body("Rate limit exceeded. Please try again later");
    }
}
