package com.aker.instantblog.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class User {

    @NotNull
    private Integer id;//主键id
    @NotNull
    private String username;//用户名
    @JsonIgnore//让springmvc把当前对象转换成json字符串的时候,忽略password,最终的json字符串中就没有password这个属性了
    private String password;//密码

    private Integer articleNumber;//文章数量
    private Integer fanNumber;//粉丝数量
    private Integer followNumber;//关注数量
    private String selfIntroduction;//自我介绍
    private String userPhoto;//用户头像url

    //前端还没设置的属性
    private String email;//邮箱
    private String nickname;//昵称
}
