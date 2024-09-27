package com.transAI.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface PoemMapper {

    @Select("select count(*) from poem_level where user_id = #{id}")
    int checkHave(int id);

    @Select("select level from poem_level where user_id = #{id}")
    int getLevel(int id);

    @Update("update poem_level set level = #{level}, `over` = false where user_id = #{user_id}")
    void update(int user_id, int level);

    @Insert("insert into poem_level (user_id, level, `over`) values (#{user_id}, #{level}, false)")
    void insert(int user_id, int level);

    @Select("select `over` from poem_level where user_id = #{user_id}")
    boolean getOver(int user_id);

    @Update("update poem_level set `over` = true where user_id = #{user_id}")
    void setOver(int user_id);

}
