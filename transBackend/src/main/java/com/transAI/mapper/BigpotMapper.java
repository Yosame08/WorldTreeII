package com.transAI.mapper;

import com.transAI.pojo.Bigpot;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface BigpotMapper {

    @Insert("insert into bigpot(id, level) values(#{id}, 1)")
    void initUserInfo(int id);

    @Select("select level from bigpot where id = #{id}")
    int getLevel(int id);

    @Update("update bigpot set level = #{level} where id = #{id}")
    void updateLevel(int id, int level);

    @Insert("insert into bigpottask(level, game_token, len, x, y, z, w) values(#{level}, #{gameToken}, #{len}, #{x}, #{y}, #{z}, #{w})")
    void insert(Bigpot bigpot);

    @Select("select * from bigpottask where game_token = #{gameToken}")
    Bigpot getBigpot(int id, String gameToken);

    @Update("update bigpottask set len = #{len}, x = #{x}, y = #{y}, z = #{z}, w = #{w} where game_token = #{gameToken}")
    void update(Bigpot bigpot);
}
