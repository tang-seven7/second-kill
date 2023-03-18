package com.seven.seckill.controller;

import com.seven.seckill.entities.dto.RegisterDTO;
import com.seven.seckill.service.UserService;
import com.seven.seckill.entities.vo.LoginVo;
import com.seven.seckill.entities.vo.RegisterVo;
import com.seven.seckill.entities.vo.ResponseResult;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/register")
    public ResponseResult<?> register(@RequestBody @Valid RegisterVo registerVo){
        RegisterDTO registerDTO = new RegisterDTO();
        BeanUtils.copyProperties(registerVo,registerDTO);
        registerDTO.setId(Long.parseLong(registerVo.getMobile()));
        if(userService.register(registerDTO)!=0){
            return ResponseResult.success();
        }
        return ResponseResult.fail();
    }

    @PostMapping("/login")
    public ResponseResult<?> login(@RequestBody @Valid LoginVo loginVo){
        if(userService.login(loginVo)){
            return ResponseResult.success();
        }
        return ResponseResult.fail();
    }
}
