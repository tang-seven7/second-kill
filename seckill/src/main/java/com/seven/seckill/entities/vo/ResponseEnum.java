package com.seven.seckill.entities.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum ResponseEnum {
    SUCCESS(200,"成功"),
    FAIL(444,"失败"),
    ERROR(500,"系统错误"),
    MOBILE_ERROR(412,"手机格式错误或不存在"),
    NULL_ERROR(414,"库存为空"),
    REPEAT_ERROR(414,"用户限制"),
    SYSTEM_ERROR(416,"服务器限制");

    private final Integer code;
    private final String message;

}
