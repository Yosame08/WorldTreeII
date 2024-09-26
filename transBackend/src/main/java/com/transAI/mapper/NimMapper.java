package com.transAI.mapper;

import org.apache.ibatis.annotations.*;

@Mapper
public interface NimMapper {

    @Insert("insert into nim_game (game_token, stones, easy) values (#{token}, #{stones}, #{easy})")
    void createIndex(String token, String stones, boolean easy);

    @Select("select stones from nim_game where game_token = #{token}")
    String getStones(String token);

    @Update("update nim_game set stones = #{stones} where game_token = #{token}")
    void updateStones(String token, String stones);

    @Delete("delete from nim_game where game_token = #{token}")
    void gameEnd(String token);

}
