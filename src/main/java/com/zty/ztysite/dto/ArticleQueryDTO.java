package com.zty.ztysite.dto;

import lombok.Data;

@Data
public class ArticleQueryDTO {
    private Integer pageNum = 1;   // 页码，默认第1页
    private Integer pageSize = 10; // 每页条数，默认10条
    private String category;       // 分类筛选（可选）
    private String search;         // 关键词搜索（可选）
}