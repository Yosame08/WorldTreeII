package com.transAI.mapper;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;

@Mapper
public interface BinsearchMapper {

    @Select("select time from user_binary_search where user_id = #{id}")
    public LocalDateTime getDateTime(Integer id);


    @Insert("insert into user_binary_search (user_id, time) values (#{id}, #{now})")
    public void submitDateTime(Integer id, LocalDateTime now);

    @Update("update user_binary_search set time = #{now} where user_id = #{id}")
    public void updateDateTime(Integer id, LocalDateTime now);

    @Insert("insert into user_binary_search (user_id, time) values (#{id}, '2020-01-01 00:00:00')")
    void initUserInfo(int id);
}

