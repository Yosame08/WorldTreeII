package com.transAI.mapper;

import com.transAI.pojo.bigpot.Bigpot;
import com.transAI.pojo.bigpot.Bigpot2;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface Bigpot2Mapper {

    @Select("select count(*) from bigpot2 where user_id = #{userId}")
    int checkHave(int userId);

    @Select("select level from bigpot2 where user_id = #{userId}")
    int checkLevel(int userId);

    @Insert("insert into bigpot2 (user_id, level, game_token, numbers) values (#{userId}, #{level}, #{gameToken}, #{numbers})")
    void newGame(int userId, int level, String gameToken, String numbers);

    @Update("update bigpot2 set level = #{level}, game_token = #{gameToken}, numbers = #{numbers} where user_id = #{userId}")
    void replaceGame(int userId, int level, String gameToken, String numbers);

    @Select("select numbers from bigpot2 where game_token = #{gameToken}")
    String getNumbers(String gameToken);

}
