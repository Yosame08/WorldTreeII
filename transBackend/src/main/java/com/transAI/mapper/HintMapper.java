package com.transAI.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface HintMapper {

    @Select("select hint from hint where task_id = #{taskId}")
    String getHint(int taskId);
}
