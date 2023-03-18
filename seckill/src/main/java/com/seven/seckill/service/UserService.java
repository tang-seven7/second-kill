package com.seven.seckill.service;

import com.seven.seckill.entities.dto.RegisterDTO;
import com.seven.seckill.entities.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seven.seckill.entities.vo.LoginVo;

/**
* @author User
* @description 针对表【t_user】的数据库操作Service
* @createDate 2023-03-14 17:43:25
*/
public interface UserService extends IService<User> {
    int register(RegisterDTO registerDTO);
    boolean login(LoginVo loginVo);

}
