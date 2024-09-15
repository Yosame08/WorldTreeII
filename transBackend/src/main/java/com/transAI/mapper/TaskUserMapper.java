package com.transAI.mapper;

import com.github.pagehelper.ISelect;
import com.transAI.pojo.TaskUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TaskUserMapper {

    @Select("select * from task_user where user_id=#{userId}")
    List<TaskUser> getTaskUserList(Integer userId);
}
