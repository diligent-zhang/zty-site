package com.zty.ztysite.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("photos")
public class Photo {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String url;
    private String location;
    private Integer sortOrder;
    private LocalDateTime createdAt;
}