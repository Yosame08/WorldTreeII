package com.transAI.service.impl;

import com.transAI.mapper.BinsearchMapper;
import com.transAI.service.BinsearchService;
import com.transAI.utils.ThreadLocalUtil;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class BinsearchServiceImpl implements BinsearchService {

    @Autowired
    private BinsearchMapper binsearchMapper;

    @Override
    public boolean check() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        LocalDateTime dateTime = binsearchMapper.getDateTime(id);
        LocalDateTime now = LocalDateTime.now();
        // 如果有存储且日期相同，返回true
        if (dateTime != null && dateTime.toLocalDate().isEqual(now.toLocalDate())) {
            return true;
        }
        return false;
    }

    @Override
    public Integer query() {
        LocalDateTime now = LocalDateTime.now();
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");

        var dateTime = binsearchMapper.getDateTime(id);
        if(dateTime == null) {
            binsearchMapper.submitDateTime(id, now);
        }
        else {
            binsearchMapper.updateDateTime(id, now);
        }
        // 如果有存储且日期相同，返回true
        LocalDateTime answer = LocalDateTime.of(2021, 8, 1, 14, 24, 0);
        // 如果小时和分钟相同，返回0
        if (now.getHour() == answer.getHour() && now.getMinute() == answer.getMinute()) {
            return 0;
        }
        // 如果now的小时和分钟早了（不算秒），返回1
        if (now.getHour() < answer.getHour() || (now.getHour() == answer.getHour() && now.getMinute() < answer.getMinute())) {
            return 1;
        }

        // 如果now的小时和分钟晚了（不算秒），返回2
        if (now.getHour() > answer.getHour() || (now.getHour() == answer.getHour() && now.getMinute() > answer.getMinute())) {
            return 2;
        }

        return 0;
    }
}
