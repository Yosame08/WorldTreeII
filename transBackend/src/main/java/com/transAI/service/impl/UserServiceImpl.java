package com.transAI.service.impl;

import com.transAI.mapper.*;
import com.transAI.pojo.User;
import com.transAI.service.UserService;
import com.transAI.utils.Md5Util;
import com.transAI.utils.ThreadLocalUtil;
import org.apache.ibatis.ognl.ObjectElementsAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BigpotMapper bigpotMapper;

    @Autowired
    private BinsearchMapper binsearchMapper;

    @Autowired
    private UserTotalPointMapper userTotalPointMapper;

    @Autowired
    private VisitingMapper visitingMapper;

    @Override
    public User findByUserName(String username) {
        User u = userMapper.findByUserName(username);
        return u;
    }

    void initUserInfo(int id) {
        bigpotMapper.initUserInfo(id);
        binsearchMapper.initUserInfo(id);
        userTotalPointMapper.initUserInfo(id);
//        visitingMapper.initUserInfo(id);
    }

    @Override
    public void register(String username, String password) {
        String md5String = Md5Util.getMD5String(password);
        userMapper.add(username, md5String);
        int id = userMapper.findByUserName(username).getId();
        initUserInfo(id);
    }

    @Override
    public void update(User user) {
        user.setUpdateTime(LocalDateTime.now());
        userMapper.update(user);
    }

    @Override
    public void updateAvatar(String avatarUrl) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        userMapper.updateAvatar(avatarUrl, id);
    }

    @Override
    public void updatePwd(String newPwd) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        userMapper.updatePwd(Md5Util.getMD5String(newPwd), id);
    }

    @Override
    public void setSourceLanguage(String sourceLanguage) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        userMapper.setSourceLanguage(sourceLanguage, id);
    }


    @Override
    public void setTargetLanguage(String targetLanguage) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        userMapper.setTargetLanguage(targetLanguage, id);
    }

    @Override
    public void setStyle(String style) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        userMapper.setStyle(style, id);
    }

    @Override
    public String getLanguage() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
//        String source_language = "source_language:".concat(userMapper.getSourceLanguage(id));
        String target_language = "target_language:".concat(userMapper.getTargetLanguage(id));
        return target_language;
    }

    @Override
    public String getStyle() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        String style = "style:".concat(userMapper.getStyle(id));
        return style;
    }

    @Override
    public String getCode() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        String code = userMapper.getCode(id);
        return code;
    }

    @Override
    public User findByRandomString(String randomString) {
        User result = userMapper.findByRandomString(randomString);
        System.out.println("Using random string to login: " + result.getUsername());
        return result;
    }
}
