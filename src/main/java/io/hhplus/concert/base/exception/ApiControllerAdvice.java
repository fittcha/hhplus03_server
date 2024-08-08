package io.hhplus.concert.base.exception;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
class ApiControllerAdvice {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    // 내가 정의한 Exception 이 발생했을 때 에러 응답
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResult<?>> handleCustomException(CustomException e) {
        switch (e.getLogLevel()) {
            case ERROR -> log.error("ApiException : {}", e.getMessage(), e);
            case WARN -> log.warn("ApiException : {}", e.getMessage(), e);
            default -> log.info("ApiException : {}", e.getMessage(), e);
        }
        return new ResponseEntity<>(ApiResult.error(e.getErrorCode(), e.getMessage(), e.getData()), HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResult<?>> handleException(Exception e) {
        log.error("UnhandledException : {}", e.getMessage(), e);
        return new ResponseEntity<>(ApiResult.error(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
