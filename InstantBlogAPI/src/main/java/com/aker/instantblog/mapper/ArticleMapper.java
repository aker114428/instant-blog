package com.aker.instantblog.mapper;

import com.aker.instantblog.pojo.Article;
import com.aker.instantblog.pojo.ArticleBrief;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {

    //计算文章数目
    @Select("select count(*) from article where author_id = #{id}")
    Integer getArticleNumberByUserId(Integer id);


    @Select("SELECT * FROM article ORDER BY RAND() LIMIT 10")
    List<ArticleBrief> getRandomArticle();

    @Insert("insert into article(article_title, article_text, introduction, article_head_img, author_id, authorName, create_time, update_time)"+
            "values (#{articleTitle},#{articleText},#{introduction},#{articleHeadImg},#{authorId},#{authorName},now(),now())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void createArticle(Article article);

    @Select("select * from article where id = #{articleId}")
    Article getArticleById(String articleId);

    @Select("select * from article where author_id = #{id}")
    List<ArticleBrief> showArticles(Integer id);

    @Update("update article set article_title = #{articleTitle}, introduction = #{introduction}, article_text = #{articleText}, article_head_img = #{articleHeadImg},update_time = now() where id=#{id}")
    void updateArticleById(Article article);

    //删除文章
    @Delete("delete from article where id=#{articleId}")
    void deleteArticleById(Integer articleId);

    //搜索文章
    @Select("SELECT * FROM article WHERE article.authorName LIKE CONCAT('%', #{keyword}, '%') OR article.article_title LIKE CONCAT('%', #{keyword}, '%') OR article.introduction LIKE CONCAT('%', #{keyword}, '%')")
    List<ArticleBrief> searchArticleByKeyword(String keyword);

    //增加用户文章数
    @Update("update user set article_number=article_number+1 where id=#{userId}")
    void increaseUserArticleNumber(Integer userId);

    //减少用户文章数
    @Update("update user set article_number=article_number-1 where id=#{userId}")
    void decreaseUserArticleNumber(Integer userId);
}
