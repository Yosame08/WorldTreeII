package com.transAI.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserHintMapper {

    @Insert("insert into user_hint(user_id, task_id) values(#{userId}, #{taskId})")
    void insertUserHint(int userId, int taskId);

    @Select("select count(*) from user_hint where user_id = #{id} and task_id = #{taskId}")
    int find(Integer id, int taskId);

    @Select("select task_id from user_hint where user_id = #{id}")
    List<Integer> getHintList(Integer id);

}
