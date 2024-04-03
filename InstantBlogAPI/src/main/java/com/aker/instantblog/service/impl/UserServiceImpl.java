package com.aker.instantblog.service.impl;

import com.aker.instantblog.mapper.UserMapper;
import com.aker.instantblog.pojo.User;
import com.aker.instantblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Override
    public User findByUserName(String username) {
        User u =userMapper.findByUserName(username);
        return u;
    }

    @Override
    public void register(String username, String password) {
        userMapper.add(username,password);
    }

    @Override
    public User findByUserId(int id) {
        User u =userMapper.findByUserId(id);
        return u;
    }

    @Override
    public void update(String userPhoto,String selfIntroduction,String username) {
        userMapper.update(userPhoto,selfIntroduction,username);
    }

    @Override
    public void follow(Integer userId, Integer id) {
        userMapper.increaseUserFollowNumber(userId);
        userMapper.increaseUserFanNumber(id);
        userMapper.follow(userId,id);
    }

    @Override
    public void unfollow(Integer userId, Integer id) {
        userMapper.decreaseUserFollowNumber(userId);
        userMapper.decreaseUserFanNumber(id);
        userMapper.unfollow(userId,id);
    }

    @Override
    public boolean isfollow(Integer userId, Integer id) {

        return userMapper.isfollow(userId,id);
    }

    @Override
    public List<User> followList(Integer id) {
        return userMapper.followList(id);
    }
}
