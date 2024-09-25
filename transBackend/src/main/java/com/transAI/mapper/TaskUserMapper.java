package com.transAI.mapper;

import com.transAI.pojo.TaskUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface TaskUserMapper {

    @Select("select * from task_user where user_id=#{userId}")
    List<TaskUser> getTaskUserList(Integer userId);

    @Select("select * from task_user where user_id=#{userId} and task_id=#{taskId}")
    TaskUser getTaskUser(Integer userId, Integer taskId);

    @Insert("insert into task_user(user_id, task_id, status, point, time) values(#{userId}, #{taskId}, #{status}, #{point}, #{time})")
    void insert(TaskUser taskUser);

    @Update("update task_user set status=#{status}, point=#{point}, time=#{time} where user_id=#{userId} and task_id=#{taskId}")
    void update(TaskUser taskUser);

    @Select("select count(*) from task_user where task_id = #{id} and status = 1")
    int getFullCompletedNum(int id);

    @Select("select count(*) from task_user where task_id = #{taskId} and status = 1 and user_id = #{id}")
    int find(Integer id, int taskId);
}
