package com.seven.seckill.entities.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseResult<T> {
    private Integer code;
    private String message;
    private T data;

    public static ResponseResult<?> success(){
        return new ResponseResult<>(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMessage(), null);
    }
    public static ResponseResult<?> fail(){
        return new ResponseResult<>(ResponseEnum.FAIL.getCode(), ResponseEnum.FAIL.getMessage(), null);
    }
    public static ResponseResult<?> error(Integer code){
        return new ResponseResult<>(code, null, null);
    }

    public ResponseResult(Integer code,String message){
        this(code, message, null);
    }

    public ResponseResult(ResponseEnum responseEnum){
        this(responseEnum.getCode(), responseEnum.getMessage(), null);
    }
}
