package com.seven.seckill.entities.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName t_user
 */
@TableName(value ="t_user")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User implements Serializable {
    /**
     * 用户id，使用手机号
     */
    @TableId
    private Long id;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 两次加密，后端加密一次，数据库加密一次
     */
    private String password;

    /**
     * 盐值，进行加密
     */
    private String salt;

    /**
     * 头像
     */
    private String head;

    /**
     * 注册日期
     */
    private Date registerDate;

    /**
     * 上一次登录日期
     */
    private Date lastLoginDate;

    /**
     * 登录次数
     */
    private Integer loginCount;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}