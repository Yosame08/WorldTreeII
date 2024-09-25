package com.transAI.service.impl;

import com.transAI.mapper.OpenboxMapper;
import com.transAI.mapper.TaskMapper;
import com.transAI.pojo.Orientation;
import com.transAI.pojo.Pos;
import com.transAI.service.OrientationService;
import com.transAI.utils.DateLogger;
import com.transAI.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OrientationServiceImpl implements OrientationService {

    @Autowired
    private OpenboxMapper openboxMapper;

    private final int taskId = 13;
    private final int taskNum = 5;
    private final Pos[] pos = new Pos[taskNum];

    @Autowired
    private TartsServiceImpl tartsServiceImpl;
    @Autowired
    private TaskMapper taskMapper;

    {
        pos[0] = new Pos(1, 121, 31);
        pos[1] = new Pos(2, 121, 31);
        pos[2] = new Pos(3, 121, 31);
        pos[3] = new Pos(4, 121, 31);
        pos[4] = new Pos(5, 121, 31);
    }

    @Override
    public List<Orientation> initOrientation() {
        Map<String, Object> map = ThreadLocalUtil.get();
        int userId = (Integer)map.get("id");
        List<Orientation> orientations = new ArrayList<>();
        for (int i = 0; i < taskNum; i++) {
            Orientation orientation = new Orientation();
            orientation.setPass(openboxMapper.queryPass(userId, i) != null);
            orientation.setDire(0);
            orientation.setDist(0);
            orientations.add(orientation);
        }
        return orientations;
    }

    @Override
    public Orientation checkOrientation(Pos pos) {
        Map<String, Object> map = ThreadLocalUtil.get();
        int userId = (Integer)map.get("id");
        Orientation orientation = new Orientation();
        int level = pos.getLevel();
        double dis = calcDis(this.pos[level], pos);

        // 判断是否通过
        if(dis < 30) {
            var passed = openboxMapper.queryPass(userId, level);
            if(passed == null) {
                openboxMapper.insertPass(userId, level);
                int num = openboxMapper.userPassNum(userId);

                var task = taskMapper.getTask(taskId);
                if (num == taskNum){
                    tartsServiceImpl.passTask(taskId, true);
                }
                else{
                    tartsServiceImpl.passPartialTask(userId, taskId, (int)(task.getTaskPoint() * num / (double) taskNum), false);
                }
            }
            orientation.setPass(true);
        } else {
            orientation.setPass(false);
            if(dis < 80) {
                orientation.setDist(0); // 近
            } else if(dis < 250) {
                orientation.setDist(1); // 不远
            } else {
                orientation.setDist(2); // 远
            }
        }

        // 计算方向并设置
        double angle = getAngle(this.pos[level], pos);
        int direction = calculateDirection(angle); // 根据角度计算方向
        orientation.setDire(direction);

        System.out.println("[" + DateLogger.getTime() + " Open Box] User " + map.get("username") + " " + pos + " STD:" + this.pos[level].toString() + " Dist=" + dis);

        return orientation;
    }

    private double calcDis(Pos pos1, Pos pos2) {
        // 地球半径（单位：米）
        final double R = 6371000;

        // 经纬度转换为弧度
        double lat1 = Math.toRadians(pos1.getLat());
        double lon1 = Math.toRadians(pos1.getLng());
        double lat2 = Math.toRadians(pos2.getLat());
        double lon2 = Math.toRadians(pos2.getLng());

        // 计算两点的距离（Haversine公式）
        double dlat = lat2 - lat1;
        double dlon = lon2 - lon1;
        double a = Math.sin(dlat / 2) * Math.sin(dlat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(dlon / 2) * Math.sin(dlon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // 计算距离
        double dis = R * c;
        return dis;
    }

    private double getAngle(Pos pos1, Pos pos2) {
        // 获取经纬度
        double lat1 = pos1.getLat();
        double lon1 = pos1.getLng();
        double lat2 = pos2.getLat();
        double lon2 = pos2.getLng();

        // 计算方向角度（正北为0度，顺时针方向计算）
        double dLon = Math.toRadians(lon2 - lon1);
        double y = Math.sin(dLon) * Math.cos(Math.toRadians(lat2));
        double x = Math.cos(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) -
                Math.sin(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(dLon);

        double angle = Math.toDegrees(Math.atan2(y, x));

        // 转换为0-360度范围
        if (angle < 0) {
            angle += 360;
        }

        return angle;
    }

    private int calculateDirection(double angle) {
        // 通过角度判断方向，0: 东, 1: 南, 2: 西, 3: 北
        if (angle >= 315 || angle < 45) {
            return 0; // 东
        } else if (angle >= 45 && angle < 135) {
            return 1; // 南
        } else if (angle >= 135 && angle < 225) {
            return 2; // 西
        } else {
            return 3; // 北
        }
    }

}
