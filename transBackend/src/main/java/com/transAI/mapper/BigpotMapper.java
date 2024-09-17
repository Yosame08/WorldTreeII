package com.transAI.mapper;

import com.transAI.pojo.Bigpot;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BigpotMapper {

    @Select("select level from bigpot where id = #{id}")
    int getLevel(int id);

    @Insert("insert into bigpottask(level, game_token, len, x, y, z, w) values(#{level}, #{gameToken}, #{len}, #{x}, #{y}, #{z}, #{w})")
    void insert(Bigpot bigpot);
}
