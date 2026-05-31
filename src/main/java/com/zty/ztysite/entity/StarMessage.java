package com.zty.ztysite.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

    @Data                       // Lombok 注解：编译时自动生成 getXxx()/setXxx()/toString() 方法
    @TableName("star_messages") // 告诉 MyBatis-Plus 这个类对应数据库的 star_messages 表
    public class StarMessage {

        @TableId(type = IdType.AUTO)  // 主键，AUTO = 数据库自增
        private Long id;

        private String nickname;      // 昵称（匿名时随机生成，实名时用户填写）
        private String message;       // 留言内容
        private Double longitude;     // 经度（Double 对应 DECIMAL(10,7)）
        private Double latitude;      // 纬度
        private Integer colorHue;     // 色相值 0-360，决定星星颜色
        private Integer isAnonymous;  // 0=实名, 1=匿名
        private Integer isApproved;   // 0=未审核, 1=已审核
        private String ipAddress;     // 留言者 IP
        private LocalDateTime createdAt;  // 创建时间
    }