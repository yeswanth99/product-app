package com.product.datajpa.exception;

import com.product.datajpa.model.Product.ErrorVo;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorVo> handleException(Exception e) {
        // Log and handle exception
        ErrorVo errorVo = new ErrorVo();
        errorVo.setCode(0);
        errorVo.setSuccess(false);
        errorVo.setMessage("Internal Server Error");
        return new ResponseEntity<>(errorVo, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity handleDataIntegrityException(DataIntegrityViolationException e){
        ErrorVo error = ErrorVo.builder()
                .success(false)
                .message("Invalid Data").build();
        return ResponseEntity.badRequest().body(error);
    }
}
