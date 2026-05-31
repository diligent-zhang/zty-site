package com.zty.ztysite.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.zty.ztysite.entity.Tag;

import java.time.LocalDateTime;
import java.util.List;

@Data       //Lombok自动生成getter/setter/toString
@TableName("articles")  //映射到数据库articles表
public class Article {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String summary;    //摘要
    private String content; //Markdown正文
    private String coverUrl;//封面图地址
    private String category;//分类
    private Integer viewCount;//阅读数
    private Integer isPublished;//是否发布

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @TableField(exist = false)  // 标记此字段不存在于数据库表中
    private List<Tag> tags;     // 关联的标签列表，查询时动态填充
}
