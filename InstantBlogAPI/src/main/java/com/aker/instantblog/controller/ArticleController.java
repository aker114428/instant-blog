
package com.aker.instantblog.controller;

import com.aker.instantblog.pojo.Article;

import com.aker.instantblog.pojo.ArticleBrief;
import com.aker.instantblog.pojo.PageBean;
import com.aker.instantblog.pojo.Result;
import com.aker.instantblog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;


    @GetMapping("/get_random_ten_articles")
    public Result<PageBean<ArticleBrief>> getRandomArticle(){
        PageBean<ArticleBrief> pb = articleService.getRandomArticle();
        return Result.success(pb);
    }

    @PostMapping("/create_article")
    public Result<Integer> createArticle(Article article){
        articleService.createArticle(article);

        return Result.success(article.getId());//id自动回填
    }

    @PostMapping("/show_article_by_id")
    public Result<Article> getArticleById(String article_id){
        Article article = articleService.getArticleById(article_id);
        return Result.success(article);
    }

    //根据作者id返回文章
    @PostMapping("/show_articles")
    public Result<PageBean<ArticleBrief>> showArticles(Integer user_id){
        PageBean<ArticleBrief> pb = articleService.showArticles(user_id);
        System.out.println("获取到的用户文章");
        System.out.println(pb.getItems());
        return Result.success(pb);
    }

    //更新文章
    @PostMapping("/update_article_by_id")
    public Result<Integer> updateArticleById(Article article){
        articleService.updateArticleById(article);
        System.out.println("获取到的用户跟新请求");
        System.out.println(article);
        if(article.getId()==null){
            return Result.error("跟新文章信息失败");//
        }
        return Result.success(article.getId());//id自动回填
    }

    @PostMapping("/delete_article_by_id")
    public Result deleteArticleById(Integer article_id){
        articleService.deleteArticleById(article_id);

        return Result.success("删除成功");
    }

    //关键词搜索文章
    @PostMapping("/search_article_by_keyword")
    public Result<PageBean<ArticleBrief>> searchArticleByKeyword(String keyword){
        PageBean<ArticleBrief> pb = articleService.searchArticleByKeyword(keyword);
        return Result.success(pb);
    }
}
