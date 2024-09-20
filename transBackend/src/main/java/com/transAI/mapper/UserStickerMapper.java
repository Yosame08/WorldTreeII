package com.transAI.mapper;

import com.transAI.pojo.Sticker;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserStickerMapper {


    @Select("select stk_id, `show`, x, y from user_sticker where user_id=#{userId}")
    List<Sticker> getStickers(int userId);

    @Update("update user_sticker set `show`=#{sticker.show}, x=#{sticker.x}, y=#{sticker.y} where user_id=#{userId} and stk_id=#{sticker.stkId}")
    void modifyStickers(int userId, Sticker sticker);

    @Select("select * from user_sticker where user_id=#{userId} and stk_id=#{stkId}")
    Sticker findSticker(int userId, int stkId);

    @Insert("insert into user_sticker(user_id, stk_id, `show`, x, y) values(#{userId}, #{sticker.stkId}, #{sticker.show}, #{sticker.x}, #{sticker.y})")
    void addSticker(int userId, Sticker sticker);
}
