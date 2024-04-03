package com.aker.instantblog.controller;

import com.aker.instantblog.pojo.Result;
import com.aker.instantblog.pojo.User;
import com.aker.instantblog.service.UserService;
import com.aker.instantblog.utils.JwtUtil;
import com.aker.instantblog.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result register(@RequestParam("username")String username, @RequestParam("password") String password){
        User u= userService.findByUserName(username);
        if(u == null){
            //没有占用，注册
            userService.register(username,password);
            return Result.success();
        }else{
            //占用
            return Result.error("用户名已占用");
        }

    }

    @PostMapping("/login")
    public Result<String> login( String username, String password) {
        //根据用户名查询用户
        User loginUser = userService.findByUserName(username);
        //判断该用户是否存在
        if (loginUser == null) {
            return Result.error("用户名错误");
        }

        //判断密码是否正确  loginUser对象中的password是密文
        if (password.equals(loginUser.getPassword())) {
            //登录成功
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", loginUser.getId());
            claims.put("username", loginUser.getUsername());
            String token = JwtUtil.genToken(claims);
//            //把token存储到redis中
//            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
//            operations.set(token,token,1, TimeUnit.HOURS);
            return Result.success(token);
        }
        return Result.error("密码错误");
    }


    @PostMapping("/get_current_user_info")
    public Result<User> userInfo(/*@RequestHeader(name = "Authorization") String token*/) {
        //根据用户名查询用户
        /*Map<String, Object> map = JwtUtil.parseToken(token);
        String username = (String) map.get("username");*/
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User user = userService.findByUserName(username);
        return Result.success(user);
    }

    @PostMapping("/get_user_info_by_id")
    public Result<User> userInfo(int id) {
        //根据用户id查询用户
        /*Map<String, Object> map = JwtUtil.parseToken(token);
        String username = (String) map.get("username");*/
        User user = userService.findByUserId(id);
        return Result.success(user);
    }

    @PostMapping("/modify_user_info")//修改用户信息
    public Result updata(String userPhoto,String selfIntroduction){
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        userService.update(userPhoto,selfIntroduction,username);
        return Result.success();
    }

    @PostMapping("/follow_user")//关注
    public Result follow(Integer followUserId){
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        userService.follow(userId,followUserId);
        return Result.success();
    }

    @PostMapping("/unfollow_user")
    public Result unfollow(Integer unfollowUserId){
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        userService.unfollow(userId,unfollowUserId);
        return Result.success();
    }

    @PostMapping("is_followed")
    public Result isfollow(Integer id){
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        boolean result = userService.isfollow(userId,id);
        return Result.success(result);
    }

    @PostMapping("/follow_list")
    public Result<List<User>> followList(Integer id){
        List<User> list = userService.followList(id);
        return Result.success(list);
    }
}
