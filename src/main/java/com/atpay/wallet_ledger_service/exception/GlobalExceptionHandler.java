package com.atpay.wallet_ledger_service.exception;

import com.atpay.wallet_ledger_service.DTO.ApiResponse;
import dev.buianhai1205.ATLogger.ATLogger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleIllegalArgument(IllegalArgumentException ex) {
        ATLogger.warn("Bad request")
                .param("error", ex.getMessage())
                .log();
        return new ApiResponse<>(400, ex.getMessage(), null);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleRuntimeException(RuntimeException ex) {
        String message = ex.getMessage();
        int status = switch (message != null ? message : "") {
            case "WALLET_NOT_FOUND", "TRANSACTION_NOT_FOUND" -> 404;
            case "INSUFFICIENT_BALANCE" -> 422;
            default -> 400;
        };
        if (status >= 500) {
            ATLogger.error("Unexpected server error", ex)
                    .param("error", message)
                    .param("status", status)
                    .log();
        } else {
            ATLogger.warn("Business error")
                    .param("status", status)
                    .param("error", message)
                    .log();
        }
        return new ApiResponse<>(status, message, null);
    }
}
