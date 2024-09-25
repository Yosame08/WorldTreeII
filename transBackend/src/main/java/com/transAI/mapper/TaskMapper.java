package com.transAI.mapper;

import com.transAI.pojo.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface TaskMapper {

    @Select("select * from task")
    List<Task> getTaskList();

    @Select("select hint_price from task where task_id = #{taskId}")
    int getHintPrice(int taskId);

    @Select("select * from task where task_id = #{taskId}")
    Task getTask(int taskId);

    @Select("select date_expire from task where task_id = #{taskId}")
    LocalDateTime getExpire(int taskId);
}
