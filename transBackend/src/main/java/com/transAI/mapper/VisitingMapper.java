package com.transAI.mapper;

import com.transAI.pojo.VisitingUnit;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface VisitingMapper {

    @Select("SELECT * FROM user_visiting WHERE user_id = #{user_id} AND visiting_id = #{visiting_id}")
    VisitingUnit getVisiting(int user_id, int visiting_id);

    @Update("UPDATE user_visiting SET x = #{x}, y = #{y}, indoor = #{indoor}, floor = #{floor} WHERE user_id = #{userId} AND visiting_id = #{visitingId}")
    void updateVisiting(VisitingUnit visitingUnit);

    @Insert("INSERT INTO user_visiting (user_id, visiting_id, x, y, indoor, floor) VALUES (#{userId}, #{visitingId}, #{x}, #{y}, #{indoor}, #{floor})")
    void insertVisiting(VisitingUnit visitingUnit);
}
