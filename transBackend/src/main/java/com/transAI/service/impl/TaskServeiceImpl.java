package com.transAI.service.impl;

import com.transAI.mapper.*;
import com.transAI.pojo.Task;
import com.transAI.pojo.TaskUser;
import com.transAI.service.TaskService;
import com.transAI.utils.DateLogger;
import com.transAI.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class TaskServeiceImpl implements TaskService {

    @Autowired
    TaskMapper taskMapper;

    @Autowired
    TaskUserMapper taskUserMapper;

    @Autowired
    TartsServiceImpl tartsServiceImpl;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserHintMapper userHintMapper;

    @Autowired
    HintClueMapper hintClueMapper;

    @Override
    public List<Task> getTaskList() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");

        List<Task> taskList = taskMapper.getTaskList();
        List<TaskUser> taskUserList = taskUserMapper.getTaskUserList(id);
        List<Integer> hintList = userHintMapper.getHintList(id);

        LocalDateTime now = LocalDateTime.now();

        for(Task task : taskList) {
            boolean flag = false;
            for(TaskUser taskUser : taskUserList) {
                if(task.getTaskId().equals(taskUser.getTaskId())) {
                    task.setTaskStatus(taskUser.getStatus());
                    task.setGetPoint(taskUser.getPoint());
                    flag = true;
                    break;
                }
            }
            if(!flag) {
                task.setTaskStatus(0);
                task.setGetPoint(0);
            }
            boolean hintFlag = false;
            for(Integer hint : hintList) {
                if(task.getTaskId().equals(hint)) {
                    task.setHintStatus(1);
                    hintFlag = true;
                    break;
                }
            }
            if(!hintFlag) {
                task.setHintStatus(0);
            }
            task.setExpired(task.getDateExpire().isBefore(now));
        }

        return taskList;
    }

    @Override
    public boolean submitTask(int taskId, String flag) {
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        System.out.println("[" + DateLogger.getTime() + " Task submit] User " + username + " submitted task " + taskId + "(" + taskMapper.getTask(taskId).getTaskTitle() + ") with flag " + flag);
        if(!taskPass(taskId, flag)) {
            return false;
        }
        System.out.println("submitTask returns");
        return true;
    }

    private boolean taskPass(int taskId, String flag) {
        switch(taskId) {
            case 1:
                return false;
            case 2:
                return false;
            case 3:
                return false;
            case 4:
                return false;
            case 5:
                return false;
            case 6:
                flag = flag.toLowerCase();
                boolean pass6 = "m5".equals(flag) || "ms".equals(flag);
                if(pass6) {
                    tartsServiceImpl.passTask(6, true);
                }
                return pass6;
            case 7:
                boolean pass7 = "卷卷更好吃".equals(flag);
                if(pass7) {
                    tartsServiceImpl.passTask(7, true);
                }
                return pass7;
            case 8:
                boolean pass8 = "sunmoonlightwithhuafu".equals(flag);
                if(pass8) {
                    tartsServiceImpl.passTask(8, true);
                }
                return pass8;
            case 9:
                boolean pass9 = "Reverse!!!Zh3n9d4n_un1v3r5e".equals(flag);
                if(pass9){
                    tartsServiceImpl.passTask(9, true);
                }
                return pass9;
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
                return false;
            default:
                return false;
        }
    }

    @Override
    public String hint(int taskId) {
        int price = taskMapper.getHintPrice(taskId);
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        String hint = hintClueMapper.getHint(taskId);

        int hasHint = userHintMapper.find(id, taskId);
        if(hasHint == 1) {
            return hint;
        }
        int passedTask = taskUserMapper.find(id, taskId);
        if(passedTask == 1) {
            userHintMapper.insertUserHint(id, taskId);
            return hint;
        }
        int userCoin = userMapper.getUserCoins(id);
        if(userCoin < price) {
            return "货币不足";
        }
        userMapper.updateUserCoins(id, userCoin - price);
        userHintMapper.insertUserHint(id, taskId);
        System.out.println("[" + DateLogger.getTime() + " Hint purchase] User " + map.get("username") + " purchased hint for task " + taskId + " (" + taskMapper.getTask(taskId).getTaskTitle() + ")");
        return hint;
    }

    @Override
    public boolean checkExpire(int taskId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expire = taskMapper.getExpire(taskId);
        return now.isAfter(expire);
    }

    @Override
    public String clue(int taskId) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        int flag = taskUserMapper.find(id, taskId);

        if(flag == 0) {
            return "未完成任务";
        }
        String clue = hintClueMapper.getClue(taskId);

        return clue;
    }
}
