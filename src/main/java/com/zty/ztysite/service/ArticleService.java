package com.zty.ztysite.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zty.ztysite.entity.Article;
import com.zty.ztysite.mapper.ArticleMapper;
import com.zty.ztysite.mapper.TagMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service         //标记为Spring管理的Service Bean
public class ArticleService {
    //构造器注入
    private final ArticleMapper articleMapper;
    private final TagMapper tagMapper;

    public ArticleService(ArticleMapper articleMapper, TagMapper tagMapper) {
        this.articleMapper = articleMapper;
        this.tagMapper = tagMapper;
    }
    //获取文章详情，同时阅读数+1
    public Article getById(Long id){
        Article article = articleMapper.selectById(id);
        if (article != null) {
            // 阅读数递增
            article.setViewCount(article.getViewCount() == null ? 1 : article.getViewCount() + 1);
            articleMapper.updateById(article);
            // 填充关联的标签列表
            article.setTags(articleMapper.findTagsByArticleId(id));
        }
        return article;
    }
    // 分页查询（仅查已发布的文章）
    public IPage<Article> page(int pageNum, int pageSize, String category, String search) {
        // LambdaQueryWrapper 用 Lambda 表达式构建条件，避免字符串硬编码字段名
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getIsPublished, 1);  // 只查已发布

        if (category != null && !category.isEmpty()) {
            wrapper.eq(Article::getCategory, category);  // 分类筛选
        }
        if (search != null && !search.isEmpty()) {
            // 搜索关键词在标题和摘要中模糊匹配
            wrapper.and(w -> w.like(Article::getTitle, search).or().like(Article::getSummary, search));
        }
        wrapper.orderByDesc(Article::getCreatedAt);  // 按创建时间倒序

        IPage<Article> page = articleMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        // 为每篇文章填充标签
        for (Article article : page.getRecords()) {
            article.setTags(articleMapper.findTagsByArticleId(article.getId()));
        }
        return page;
    }

    // 创建文章 + 关联标签。@Transactional 保证两步操作要么都成功要么都回滚
    @Transactional
    public Article create(Article article, List<Long> tagIds) {
        articleMapper.insert(article);  // 插入文章，MyBatis-Plus 自动回填自增 ID 到 article.id
        if (tagIds != null && !tagIds.isEmpty()) {
            for (Long tagId : tagIds) {
                articleMapper.insertArticleTag(article.getId(), tagId);
            }
        }
        return getById(article.getId());
    }

    // 更新文章 + 重新关联标签（先删旧标签关联，再插新的）
    @Transactional
    public Article update(Article article, List<Long> tagIds) {
        articleMapper.updateById(article);
        articleMapper.deleteArticleTags(article.getId());  // 清除旧关联
        if (tagIds != null && !tagIds.isEmpty()) {
            for (Long tagId : tagIds) {
                articleMapper.insertArticleTag(article.getId(), tagId);
            }
        }
        return getById(article.getId());
    }

    // 删除文章 + 清除标签关联
    @Transactional
    public void delete(Long id) {
        articleMapper.deleteArticleTags(id);  // 先删关联，否则外键或孤儿数据
        articleMapper.deleteById(id);
    }
//LambdaQueryWrapper 用 Article::getTitle 这种写法代替字符串 "title"，如果字段改名，编译时就能发现错误。
//            ▎ - @Transactional 保证数据一致性——如果中间出错，之前插入/删除的操作会回滚。
//            ▎ - 更新标签的策略是"先删后插"，比逐个比对差异更简单可靠。
}
