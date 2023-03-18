package com.seven.seckill.entities.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegisterDTO {
    private Long id;
    private String nickname;
    private String password;
    private String salt;
    private Date registerDate;
}
