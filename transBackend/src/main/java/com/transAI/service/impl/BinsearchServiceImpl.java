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

    @Autowired
    private TartsServiceImpl tartsServiceImpl;



    @Override
    public int check() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        var dateTime = binsearchMapper.getDateTime(id);
        LocalDateTime now = LocalDateTime.now();
        int size = 0 ;
        for(LocalDateTime dt : dateTime) {
            if(dt.toLocalDate().isEqual(now.toLocalDate())) {
                size++;
            }
        }
        return size;
    }

    @Override
    public Integer query() {
        LocalDateTime now = LocalDateTime.now();
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        System.out.println("now: " + now);

        var dateTimeList = binsearchMapper.getDateTime(id);
        if(dateTimeList == null) {
            binsearchMapper.submitDateTime(id, now);
        }
        else {
            // 如果有两个且两个日期都与now相同，返回-1
//            if(dateTime.toLocalDate().isEqual(now.toLocalDate())) {
//                return -1;
//            }
            if(dateTimeList.size() == 2 && dateTimeList.get(0).toLocalDate().isEqual(now.toLocalDate()) && dateTimeList.get(1).toLocalDate().isEqual(now.toLocalDate())) {
                return -1;
            }
//            binsearchMapper.updateDateTime(id, now);
            binsearchMapper.submitDateTime(id, now);
        }
        // 如果有存储且日期相同，返回true
        System.out.println("my:now: " + now);
//        Integer hashed = ((id + 5) * (id + 2) - 2) * (id + 1) % 160 + 30;
        Integer hashed = 65;
        LocalDateTime answer = LocalDateTime.of(2021, 8, 1, 18+hashed/60, hashed%60, 0);
        // 如果小时和分钟相同，返回0
        if (now.getHour() == answer.getHour() && now.getMinute() == answer.getMinute()) {
            tartsServiceImpl.passTask(1);
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
