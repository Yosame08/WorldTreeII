package com.transAI.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

@Mapper
public interface UserSubmitMapper {

    @Select("select count(*) from user_submit where id=#{id}")
    int userSubmitSize(int id);

    @Insert("insert into user_submit (id, time) values(#{id}, now())")
    void insertUserSubmit(int id);

    @Select("select min(time) from user_submit where id=#{id}")
    LocalDateTime getEarliestTime(int id);

    @Delete("delete from user_submit where id=#{id} and time=#{earliestTime}")
    void deleteUserSubmit(int id, LocalDateTime earliestTime);
}
