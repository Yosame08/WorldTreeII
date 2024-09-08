package com.transAI.mapper;

import com.transAI.pojo.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TaskMapper {

    @Select("select * from task")
    List<Task> getTaskList();
}
