package com.aker.instantblog.service;

import com.aker.instantblog.pojo.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    User findByUserName(String username);

    void register(String username, String password);

    User findByUserId(int id);

    void update(String userPhoto,String selfIntroduction,String username);

    void follow(Integer userId, Integer id);

    void unfollow(Integer userId, Integer followUserId);

    boolean isfollow(Integer userId, Integer id);

    List<User> followList(Integer id);
}
