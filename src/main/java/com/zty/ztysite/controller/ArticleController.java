package com.zty.ztysite.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zty.ztysite.dto.ArticleQueryDTO;
import com.zty.ztysite.dto.ArticleSaveDTO;
import com.zty.ztysite.entity.Article;
import com.zty.ztysite.service.ArticleService;
import com.zty.ztysite.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ArticleController {

    private final ArticleService articleService;
    private final AuthService authService;
    public ArticleController(ArticleService articleService, AuthService authService) {
        this.articleService = articleService;
        this.authService = authService;
    }
    //公开接口：文章列表（分页+分类+搜索）
    @GetMapping("/articles")
    public IPage<Article> list(ArticleQueryDTO query){
        return articleService.page(query.getPageNum(),query.getPageSize(),query.getCategory(),query.getSearch());
    }


    //公开接口：文章详情（阅读数自动+1）
    @GetMapping("/articles/{id}")
    public Article detail(@PathVariable Long id){
        Article article = articleService.getById(id);
        if(article == null  || article.getIsPublished()==0){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"文章不存在");
        }
        return article;
    }
//需登录:创建文章
        @PostMapping("/articles")
        public Article create(@RequestBody ArticleSaveDTO dto, HttpSession session) {
            checkLogin(session);
            Article article = new Article();
            article.setTitle(dto.getTitle());
            article.setSummary(dto.getSummary());
            article.setContent(dto.getContent());
            article.setCoverUrl(dto.getCoverUrl());
            article.setCategory(dto.getCategory());
            article.setIsPublished(dto.getIsPublished() != null ? dto.getIsPublished() : 1);
            return articleService.create(article, dto.getTagIds());
        }

    //需登录：更新文章
    @PutMapping("/articles/{id}")
    public Article update(@PathVariable Long id, @RequestBody ArticleSaveDTO dto, HttpSession session) {
        checkLogin(session);
        Article article = new Article();
        article.setId(id);
        article.setTitle(dto.getTitle());
        article.setSummary(dto.getSummary());
        article.setContent(dto.getContent());
        article.setCoverUrl(dto.getCoverUrl());
        article.setCategory(dto.getCategory());
        article.setIsPublished(dto.getIsPublished());
        return articleService.update(article, dto.getTagIds());
    }
    //需登录：删除文章
    @DeleteMapping("/articles/{id}")
    public Map<String, Object> delete(@PathVariable Long id, HttpSession session) {
        checkLogin(session);
        articleService.delete(id);
        return Map.of("ok", true);
    }
    //校验登录态，未登录抛401
    private void checkLogin(HttpSession session) {
        if (!authService.isLoggedIn(session)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "请先登录");
        }
    }
}
