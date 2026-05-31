package com.zty.ztysite.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("passions")
public class Passion {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String description;
    private String mediaType;
    private String mediaUrl;
    private Integer sortOrder;
    private LocalDateTime createdAt;
}
