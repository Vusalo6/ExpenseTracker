package com.vusal.ExpenseTracker.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@AllArgsConstructor
@Getter
@Setter
public class ErrorResponse {
    int status;
    String message;
    LocalDateTime dateTime;
}
