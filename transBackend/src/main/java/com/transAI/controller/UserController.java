package com.transAI.controller;

import com.transAI.pojo.Result;
import com.transAI.pojo.User;
import com.transAI.service.UserService;
import com.transAI.utils.JwtUtil;
import com.transAI.utils.Md5Util;
import com.transAI.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/user")
@Validated
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/signup")
    public Result register(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        User u = userService.findByUserName(username);
        logger.info("This is an info message in UserController");
        logger.warn("This is an info message in UserController");
        if(u==null) {
            userService.register(username, password);
            return Result.success();
        }
        else {
            return Result.error("用户名已被占用");
        }
    }
    @PostMapping("/login")
    public Result<String> login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        User loginUser = userService.findByUserName(username);
        if(loginUser==null) {
            return Result.error("用户名不存在");
        }
        if (Md5Util.getMD5String(password).equals(loginUser.getPassword())) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", loginUser.getId());
            claims.put("username", loginUser.getUsername());
            String token =  JwtUtil.genToken(claims);
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.set(token, token, 24, TimeUnit.HOURS);
            return Result.success(token);
        }
        return Result.error("密码错误");
    }
    @GetMapping("/get_info")
    public Result<User> userInfo() {
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User user = userService.findByUserName(username);

        return Result.success(user);
    }
    @PostMapping("/set_info")
    public Result update(@RequestBody User user) {
        userService.update(user);
        return Result.success();
    }

    @PostMapping("/get_id")
    public Result get_id(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        int id = userService.findByUserName(username).getId();
        return Result.success(id);
    }

    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam @URL String avatarUrl) {
        userService.updateAvatar(avatarUrl);
        return Result.success();
    }

    // todo :更新的密码格式校验
    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody Map<String, String> params, @RequestHeader("Authorization") String token) {
        String oldPwd = params.get("old_pwd");
        String newPwd = params.get("new_pwd");
        String rePwd = params.get("re_pwd");

        if(!StringUtils.hasLength(oldPwd) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)) {
            return Result.error("密码不能为空");
        }
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User loginUser = userService.findByUserName(username);
        if(!loginUser.getPassword().equals(Md5Util.getMD5String(oldPwd))) {
            return Result.error("原密码错误");
        }
        if(!newPwd.equals(rePwd)) {
            return Result.error("两次密码不一致");
        }

        userService.updatePwd(newPwd);
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.getOperations().delete(token);
        return Result.success();
    }


    @PostMapping("/setLanguage")
    public Result setLanguage(@RequestBody Map<String, String> body) {
        if(body.get("target_language") != null) {
            // debug
            userService.setTargetLanguage(body.get("target_language"));
            return Result.success("set target language success");
        }
        if(body.get("source_Language") != null) {
            // debug
            userService.setSourceLanguage(body.get("source_language"));
        }
        return Result.error("set language failed");
    }

    @PostMapping("/setStyle")
    public Result setStyle(@RequestBody Map<String, String> body) {
        if(body.get("style") != null) {
            // debug
            userService.setStyle(body.get("style"));
            return Result.success("set style success");
        }
        return Result.error("set style failed");
    }

    @GetMapping("/getLanguage")
    public Result getLanguage() {
        // debug
        return Result.success(userService.getLanguage());
    }

    @GetMapping("/getStyle")
    public Result getStyle() {
        // debug
        return Result.success(userService.getStyle());
    }
}
