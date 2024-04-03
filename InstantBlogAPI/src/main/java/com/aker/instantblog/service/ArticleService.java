package com.aker.instantblog.service;

import com.aker.instantblog.mapper.ArticleMapper;
import com.aker.instantblog.pojo.Article;
import com.aker.instantblog.pojo.ArticleBrief;
import com.aker.instantblog.pojo.PageBean;
import org.springframework.beans.factory.annotation.Autowired;

public interface ArticleService {


    PageBean<ArticleBrief> getRandomArticle();

    void createArticle(Article article);

    Article getArticleById(String articleId);

    PageBean<ArticleBrief> showArticles(Integer id);

    void updateArticleById(Article article);

    void deleteArticleById(Integer articleId);

    PageBean<ArticleBrief> searchArticleByKeyword(String keyword);
}
