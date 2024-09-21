package com.transAI.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface HintClueMapper {

    @Select("select hint from hint_clue where task_id = #{taskId}")
    String getHint(int taskId);

    @Select("select clue from hint_clue where task_id = #{taskId}")
    String getClue(int taskId);

}
