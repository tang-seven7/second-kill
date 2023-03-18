package com.seven.seckill.exception;

import com.seven.seckill.entities.vo.ResponseEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlobalException extends RuntimeException{
    private ResponseEnum responseEnum;
}
