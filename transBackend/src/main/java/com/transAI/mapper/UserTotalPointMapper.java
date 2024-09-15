package com.transAI.mapper;

import com.transAI.pojo.UserTotalPoint;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserTotalPointMapper {

    @Select("select * from user_total_point where user_id=#{userId} ORDER BY time ASC")
    List<UserTotalPoint> getUserTrend(int userId);
}
