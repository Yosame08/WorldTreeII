package com.transAI.mapper;

import com.transAI.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select * from user where username=#{username}")
    User findByUserName(String username);

    @Insert("insert into user (username, password, create_time, update_time, random_value, coin, point)" +
    " values(#{username}, #{password}, now(), now(), UUID(), 0, 0)")
    void add(String username, String password);

    @Update("update user set username=#{username}, nickname=#{nickname}, email=#{email}, update_time=#{updateTime}, coin=#{coin}, point=#{point} where id = #{id}")
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

    @Select("select * from user ORDER BY point DESC, coin DESC")
    List<User> getUserSortByPoint();

    @Update("update user set point=#{point} where id = #{id}")
    void updatePoint(Integer id, Integer point);

    @Select("select coin from user where id=#{id}")
    int getUserCoins(int id);

    @Update("update user set coin=#{coin} where id = #{id}")
    void updateUserCoins(Integer id, int coin);

    @Select("select * from user where id=#{id}")
    User getUser(int id);
}
