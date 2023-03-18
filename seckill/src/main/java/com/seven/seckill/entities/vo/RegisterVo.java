package com.seven.seckill.entities.vo;

import com.seven.seckill.entities.validator.IsMobile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegisterVo {
    @IsMobile
    private String mobile;
    @NotBlank(message = "密码不可为空")
    private String password;
    private String nickname;
}
