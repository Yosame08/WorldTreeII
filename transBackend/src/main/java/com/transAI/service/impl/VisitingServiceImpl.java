package com.transAI.service.impl;

import com.transAI.mapper.VisitingMapper;
import com.transAI.utils.DateLogger;
import com.transAI.utils.DistCalculator;
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
    private final Visiting visitingAnswer = new Visiting();
    {
        visitingAnswer.setPosition(new double[][]{{121.50209, 31.30552}, {121.50777,31.303138}, {121.50831, 31.30479}});
        visitingAnswer.setIndoor(new boolean[]{false, false, true});
        visitingAnswer.setFloor(new int[]{0, 0, 1});
    }
    @Override
    public void update(Visiting visiting) {
        Map<String, Object> map = ThreadLocalUtil.get();
        int userId = (int) map.get("id");
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
        StringBuilder info = new StringBuilder("[" + DateLogger.getTime() + " Visiting] User " + map.get("username") + " updated: ");
        for (int i = 0; i < 3; i++) {
            info.append("[").append(i).append("] ");
            info.append("Dist: ").append(DistCalculator.haversine(visiting.getPosition()[i][0], visiting.getPosition()[i][1], visitingAnswer.getPosition()[i][0], visitingAnswer.getPosition()[i][1])).append(" ");
            info.append("Indoor: ").append(visiting.getIndoor()[i]).append(" ");
            info.append("Floor: ").append(visiting.getFloor()[i]).append(" ");
        }
        System.out.println(info);
    }

    @Override
    public Visiting getInfo() {
        Map<String, Object> map = ThreadLocalUtil.get();
        int userId = (int) map.get("id");
        Visiting visiting = new Visiting();
        visiting.setPosition(new double[3][2]);
        visiting.setIndoor(new boolean[3]);
        visiting.setFloor(new int[3]);
        for(int i = 0;i < 3;i++){
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
