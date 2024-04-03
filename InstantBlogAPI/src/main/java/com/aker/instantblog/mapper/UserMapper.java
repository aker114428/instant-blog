package com.aker.instantblog.mapper;

import com.aker.instantblog.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("select * from user where username=#{username}")
    User findByUserName(String username);

    @Insert("insert into user(username,password,create_time,update_time,self_introduction,user_photo)"+
             "values(#{username},#{password},now(),now(),'这个人很懒,没有留下简介','https://source-warehouse.oss-cn-guangzhou.aliyuncs.com/img/avatar.png')")
    void add(String username, String password);//注册

    @Select("select * from user where id=#{id}")
    User findByUserId(int id);//根据id查找用户


    @Update("update user set user_photo=#{userPhoto},self_introduction=#{selfIntroduction},update_time= now() where username=#{username}")
    void update(String userPhoto,String selfIntroduction,String username);



    @Update("update user set password=#{md5String},update_time=now() where id=#{id}")
    void updatePwd(String md5String, Integer id);


    @Insert("insert into user_follows(follower_id,followed_id,follow_date)"+ "values(#{userId},#{id},now())")
    void follow(Integer userId, Integer id);

    @Delete("delete from user_follows where follower_id=#{userId} and followed_id=#{id}")
    void unfollow(Integer userId, Integer id);

    @Select("SELECT COUNT(*) > 0 FROM user_follows WHERE follower_id = #{userId} AND followed_id = #{id}")
    boolean isfollow(Integer userId, Integer id);

    //获取关注列表
    @Select("select u.* from user u join user_follows fu on u.id = fu.followed_id where fu.follower_id=#{id};")
    List<User> followList(Integer id);

    //增加用户粉丝数
    @Update("update user set fan_number=user.fan_number+1 where id=#{userId}")
    void increaseUserFanNumber(Integer userId);

    //减少用户粉丝数
    @Update("update user set fan_number=user.fan_number-1 where id=#{userId}")
    void decreaseUserFanNumber(Integer userId);

    //增加用户关注数
    @Update("update user set follow_number=user.follow_number+1 where id=#{userId}")
    void increaseUserFollowNumber(Integer userId);

    //减少用户关注数
    @Update("update user set follow_number=user.follow_number-1 where id=#{userId}")
    void decreaseUserFollowNumber(Integer userId);

}
