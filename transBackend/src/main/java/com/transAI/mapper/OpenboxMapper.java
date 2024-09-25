package com.transAI.mapper;

import com.transAI.pojo.UserLevel;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OpenboxMapper {
    @Insert("insert into user_openbox (user_id, level_id) values (#{userId}, #{levelId})")
    void insertPass(int userId, int levelId);

    @Select("select * from user_openbox where user_id = #{userId} and level_id = #{levelId}")
    UserLevel queryPass(int userId, int levelId);

    @Select("select count(*) from user_openbox where user_id = #{userId}")
    Integer userPassNum(int userId);
}
