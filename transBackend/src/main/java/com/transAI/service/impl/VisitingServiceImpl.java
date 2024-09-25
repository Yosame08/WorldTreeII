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

import java.util.List;
import java.util.Map;

@Service
public class VisitingServiceImpl implements VisitingService {

    @Autowired
    private VisitingMapper visitingMapper;

    @Autowired
    TartsServiceImpl tartsServiceImpl;

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

    @Override
    public void expireAndJudge() {
        List<Integer> userIds = visitingMapper.getAllUserIds();
        for (int userId : userIds) {
            double totalScore = 0;
            for (int i = 0; i < 3; i++) {
                VisitingUnit userVisiting = visitingMapper.getVisiting(userId, i);
                if (userVisiting == null) {
                    userVisiting = new VisitingUnit();
                    userVisiting.setX(0);
                    userVisiting.setY(0);
                    userVisiting.setIndoor(false);
                    userVisiting.setFloor(0);
                }

                double distance = DistCalculator.haversine(
                        userVisiting.getX(), userVisiting.getY(),
                        visitingAnswer.getPosition()[i][0], visitingAnswer.getPosition()[i][1]
                );
                int distanceScore = calculateDistanceScore(distance);
                int indoorScore = calculateIndoorScore(userVisiting, i);
                System.out.println("User = " + userId + " Dist = " + distance + " Score = " + calculateDistanceScore(distance) + " Indoor Score = " + calculateIndoorScore(userVisiting, i));

                totalScore += (distanceScore + indoorScore) * (i < 2 ? 0.3 : 0.4);
            }
            tartsServiceImpl.passPartialTask(userId, 4, (int)totalScore, true);
        }
    }

    private int calculateDistanceScore(double distance) {
        if (distance <= 50) {
            return 80;
        } else if (distance >= 300) {
            return 0;
        } else {
            return (int) (80 * (300 - distance) / 350);
        }
    }

    private int calculateIndoorScore(VisitingUnit userVisiting, int index) {
        int score = 0;
        if (userVisiting.isIndoor() == visitingAnswer.getIndoor()[index]) {
            if (userVisiting.isIndoor()) {
                int floorDiff = Math.abs(userVisiting.getFloor() - visitingAnswer.getFloor()[index]);
                if (floorDiff <= 1) {
                    score = 20;
                } else if (floorDiff == 2) {
                    score = 15;
                } else if (floorDiff == 3) {
                    score = 10;
                } else if (floorDiff == 4) {
                    score = 5;
                }
            } else {
                score = 20;
            }
        }
        return score;
    }
}
