package com.aker.instantblog.pojo;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Article {

    private Integer id;//文章id
    @NotNull
    private Integer authorId;//作者id
    private String authorName;//作者名字

    @NotEmpty
    @Pattern(regexp = "^\\S{1,20}$")
    private String articleTitle;//文章标题
    private String articleHeadImg;//文章封面图url，文章封面图url
    private String articleText;//文章内容
    private String introduction;//文章简介

    private LocalDateTime publishTime;//创建时间
    private LocalDateTime updateTime;//更新时间
}
