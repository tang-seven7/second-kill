package com.seven.seckill.exception;

import com.seven.seckill.entities.vo.ResponseEnum;
import com.seven.seckill.entities.vo.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseResult<?> ExceptionHandler(Exception e){
        if (e instanceof GlobalException){
            GlobalException ex = (GlobalException) e;
            return new ResponseResult<>(ex.getResponseEnum());
        }else if (e instanceof BindException){
            log.error("参数校验错误");
            BindException ex = (BindException) e;
            String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
            return new ResponseResult<>(ResponseEnum.MOBILE_ERROR.getCode(), message);
        }
        return new ResponseResult<>(ResponseEnum.FAIL.getCode(),ResponseEnum.FAIL.getMessage(),e.getMessage());
    }
}
