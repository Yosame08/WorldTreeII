package com.backend.mapper;

import com.backend.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {
    @Select("select * from user where username=#{username}")
    User findByUserName(String username);

    @Insert("insert into user (username, password, create_time, update_time)" +
    " values(#{username}, #{password}, now(), now())")
    void add(String username, String password);

    @Update("update user set username=#{username}, nickname=#{nickname}, email=#{email}, update_time=#{updateTime} where id = #{id}")
    void update(User user);

    @Update("update user set user_pic=#{avatarUrl}, update_time=now() where id = #{id}")
    void updateAvatar(String avatarUrl, Integer id);

    @Update("update user set password=#{md5String}, update_time=now() where id = #{id}")
    void updatePwd(String md5String, Integer id);

    @Select("select source_language from user where id=#{id}")
    String getSourceLanguage(Integer id);

    @Select("select target_language from user where id=#{id}")
    String getTargetLanguage(Integer id);

    @Update("update user set target_language=#{targetLanguage} where id = #{id}")
    void setSourceLanguage(String sourceLanguage, Integer id);

    @Update("update user set target_language=#{targetLanguage} where id = #{id}")
    void setTargetLanguage(String targetLanguage, Integer id);

    @Update("update user set style=#{style} where id = #{id}")
    void setStyle(String style, Integer id);

    @Select("select style from user where id=#{id}")
    String getStyle(Integer id);

}
