package com.zty.ztysite.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;


//admin     admin123
@Data
@TableName("users")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;      // 登录用户名
    private String passwordHash;  // BCrypt 加密后的密码哈希值
    private LocalDateTime createdAt;
}

// 解释： 密码字段存的是 passwordHash 而不是明文 password，登录时用 BCrypt 比对哈希值。