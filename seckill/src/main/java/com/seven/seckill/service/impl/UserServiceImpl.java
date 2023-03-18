package com.seven.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seven.seckill.entities.dto.RegisterDTO;
import com.seven.seckill.entities.pojo.User;
import com.seven.seckill.entities.vo.ResponseEnum;
import com.seven.seckill.exception.GlobalException;
import com.seven.seckill.service.UserService;
import com.seven.seckill.mapper.UserMapper;
import com.seven.seckill.entities.vo.LoginVo;
import com.seven.seckill.utils.MD5Util;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

/**
* @author User
* @description 针对表【t_user】的数据库操作Service实现
* @createDate 2023-03-14 17:43:25
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper;

    @Override
    public int register(RegisterDTO registerDTO) {
        //生成盐，对密码进行加密
        String salt = UUID.randomUUID().toString().substring(0,10);
        registerDTO.setSalt(salt);
        registerDTO.setPassword(MD5Util.inputPassToDBPass(registerDTO.getPassword(),salt));
        //根据当前时间生成注册时间
        registerDTO.setRegisterDate(new Date());
        //DTO转换为PO，调用dao进行插入
        User user = new User();
        BeanUtils.copyProperties(registerDTO,user);
        //验证手机号是否已注册
        if(userMapper.selectById(user.getId())!=null){
            throw new GlobalException(ResponseEnum.REPEAT_ERROR);
        }

        return userMapper.insert(user);

    }

    @Override
    public boolean login(LoginVo loginVo) {
        //获取id，即用户手机号
        User user = userMapper.selectById(Long.parseLong(loginVo.getMobile()));
        if(user==null){
            throw new GlobalException(ResponseEnum.MOBILE_ERROR);
        }
        //验证密码
        String inputPass = MD5Util.inputPassToDBPass(loginVo.getPassword(),user.getSalt());
        return inputPass.equals(user.getPassword());
    }
}




