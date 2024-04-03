package com.aker.instantblog.service.impl;

import com.aker.instantblog.mapper.ArticleMapper;
import com.aker.instantblog.pojo.Article;
import com.aker.instantblog.pojo.ArticleBrief;
import com.aker.instantblog.pojo.PageBean;
import com.aker.instantblog.service.ArticleService;
import com.aker.instantblog.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    ArticleMapper articleMapper;
    @Override
    public PageBean<ArticleBrief> getRandomArticle() {
        List<ArticleBrief> list = articleMapper.getRandomArticle();
        PageBean<ArticleBrief> pb=new PageBean();
        pb.setTotal(list.size());
        pb.setItems(list);
        return pb;
    }

    //创建文章
    @Override
    public void createArticle(Article article) {
        articleMapper.increaseUserArticleNumber(article.getAuthorId());
        articleMapper.createArticle(article);
    }

    @Override
    public Article getArticleById(String articleId) {
        return articleMapper.getArticleById(articleId);
    }

    //查询作者所有文章
    @Override
    public PageBean<ArticleBrief> showArticles(Integer id) {
        List<ArticleBrief> list = articleMapper.showArticles(id);
        PageBean<ArticleBrief> pb=new PageBean();
        pb.setTotal(list.size());
        pb.setItems(list);
        return pb;
    }

    //更新文章
    @Override
    public void updateArticleById(Article article) {
        articleMapper.updateArticleById(article);
    }

    //删除文章
    @Override
    public void deleteArticleById(Integer articleId) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        articleMapper.decreaseUserArticleNumber(id);
        articleMapper.deleteArticleById(articleId);
    }

    @Override
    public PageBean<ArticleBrief> searchArticleByKeyword(String keyword) {
        List<ArticleBrief> list = articleMapper.searchArticleByKeyword(keyword);
        PageBean<ArticleBrief> pb=new PageBean();
        pb.setTotal(list.size());
        pb.setItems(list);
        return pb;
    }
}
