package com.zty.ztysite.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zty.ztysite.entity.Article;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

// 只保留这一个注解！！！
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    @Select("SELECT t.* FROM tags t INNER JOIN article_tags at ON t.id = at.tag_id WHERE at.article_id = #{articleId}")
    List<com.zty.ztysite.entity.Tag> findTagsByArticleId(Long articleId);

    @Delete("DELETE FROM article_tags WHERE article_id = #{articleId}")
    void deleteArticleTags(Long articleId);

    @Insert("INSERT INTO article_tags (article_id, tag_id) VALUES (#{articleId}, #{tagId})")
    void insertArticleTag(Long articleId, Long tagId);
}