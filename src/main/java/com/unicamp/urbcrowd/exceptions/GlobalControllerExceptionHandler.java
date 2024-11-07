package com.unicamp.urbcrowd.exceptions;

import com.unicamp.urbcrowd.exceptions.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorDTO> handleBadCredentialsException(BadCredentialsException e) {
        return new ResponseEntity<>(new ErrorDTO(HttpStatus.UNAUTHORIZED, e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    public ResponseEntity<ErrorDTO> handleBusinessException(BusinessException e) {
        return new ResponseEntity<>(new ErrorDTO(e.getHttpStatus(), e.getMessage()), HttpStatus.PRECONDITION_FAILED);
    }
}
