package com.zty.ztysite.dto;

import lombok.Data;
import java.util.List;

@Data
public class ArticleSaveDTO {
    private String title;          // 标题
    private String summary;        // 摘要
    private String content;        // Markdown 正文
    private String coverUrl;       // 封面图地址
    private String category;       // 分类
    private Integer isPublished;   // 1=发布, 0=草稿
    private List<Long> tagIds;     // 关联的标签ID列表
}