package com.backend.service;

import com.backend.pojo.User;

public interface UserService {
    User findByUserName(String username);

    void register(String username, String password);

    void update(User user);

    void updateAvatar(String avatarUrl);

    void updatePwd(String newPwd);

    void setTargetLanguage(String targetLanguage);

    void setSourceLanguage(String sourceLanguage);

    void setStyle(String style);


    String getLanguage();

    String getStyle();
}
