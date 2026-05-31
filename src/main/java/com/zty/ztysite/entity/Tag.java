package com.zty.ztysite.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("tags")
public class Tag {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;  // 标签名，如 "Java", "Spring Boot"
}