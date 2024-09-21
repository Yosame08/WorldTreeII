package com.transAI.service.impl;

import com.transAI.mapper.VisitingMapper;
import com.transAI.pojo.Visiting;
import com.transAI.pojo.VisitingUnit;
import com.transAI.service.VisitingService;
import com.transAI.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class VisitingServiceImpl implements VisitingService {

    @Autowired
    private VisitingMapper visitingMapper;
    @Override
    public void update(Visiting visiting) {
        Map<String, Object> map = ThreadLocalUtil.get();
        int userId = (int) map.get("id");
        System.out.println("visiting:" + visiting);
        for(int i = 0;i < 3;i++){
            VisitingUnit visitingUnit = new VisitingUnit();
            visitingUnit.setUserId(userId);
            visitingUnit.setVisitingId(i);
            visitingUnit.setX(visiting.getPosition()[i][0]);
            visitingUnit.setY(visiting.getPosition()[i][1]);
            visitingUnit.setIndoor(visiting.getIndoor()[i]);
            visitingUnit.setFloor(visiting.getFloor()[i]);


            var tmp = visitingMapper.getVisiting(userId, i);
            if(tmp == null){
                visitingMapper.insertVisiting(visitingUnit);
            }else{
                visitingMapper.updateVisiting(visitingUnit);
            }
        }
    }

    @Override
    public Visiting getInfo() {
        Map<String, Object> map = ThreadLocalUtil.get();
        int userId = (int) map.get("id");
        Visiting visiting = new Visiting();
        visiting.setPosition(new double[5][2]);
        visiting.setIndoor(new boolean[5]);
        visiting.setFloor(new int[5]);
        for(int i = 0;i < 5;i++){
            var tmp = visitingMapper.getVisiting(userId, i);
            if(tmp != null){
                visiting.getPosition()[i][0] = tmp.getX();
                visiting.getPosition()[i][1] = tmp.getY();
                visiting.getIndoor()[i] = tmp.isIndoor();
                visiting.getFloor()[i] = tmp.getFloor();
            }
        }
        return visiting;
    }
}
