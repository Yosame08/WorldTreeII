package com.transAI.mapper;

import com.transAI.pojo.TelegramRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TelegramMapper {
    @Select("select * from telegram_record ORDER BY score DESC")
    List<TelegramRecord> getTelegramRecordList();

    @Select("select telegramId from telegram_record where usernameA = #{name} or usernameB = #{name}")
    List<Integer> getPlayed(String name);

    @Insert("insert into telegram_record(telegramId, usernameA, usernameB, score, monsterScore, time, hp)" +
            " values(#{telegramId}, #{usernameA}, #{usernameB}, #{score}, #{monsterScore}, #{time}, #{hp})")
    void submit(TelegramRecord telegramRecord);
}
