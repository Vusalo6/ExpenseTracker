package com.vusal.ExpenseTracker.exception;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> illegalArgumentExceptionHandler(IllegalArgumentException e)
    {
    ErrorResponse errorResponse =new ErrorResponse
            (400,e.getMessage(), LocalDateTime.now());

    return ResponseEntity
            .status(HttpStatus.BAD_REQUEST).body(errorResponse);

}
@ExceptionHandler(ResourceNotFoundException.class)
public ResponseEntity<ErrorResponse> resourceNotFoundException(ResourceNotFoundException e){
    ErrorResponse errorResponse =new ErrorResponse(404,e.getMessage(),LocalDateTime.now());
   return ResponseEntity.status(HttpStatus.NOT_FOUND)
           .body(errorResponse);

}
@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException e){
    String message= e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
    ErrorResponse errorResponse = new ErrorResponse(400
            ,message
            ,LocalDateTime.now());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

}


}
