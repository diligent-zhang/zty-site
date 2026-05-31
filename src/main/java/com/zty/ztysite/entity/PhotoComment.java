package com.zty.ztysite.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("photo_comments")
public class PhotoComment {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long photoId;
    private String nickname;
    private String content;
    private String ip;
    private LocalDateTime createdAt;
}
