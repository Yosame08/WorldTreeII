package com.transAI.service.impl;

import com.transAI.mapper.*;
import com.transAI.pojo.*;
import com.transAI.service.TartsService;
import com.transAI.utils.DateLogger;
import com.transAI.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

@Service
@Transactional
public class TartsServiceImpl implements TartsService {
    @Autowired
    private TaskUserMapper taskUserMapper;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private UserTotalPointMapper userTotalPointMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserHintMapper userHintMapper;

    @Autowired
    private UserStickerMapper userStickerMapper;

    @Override
    public void add() {
        Map<String, Object> map = ThreadLocalUtil.get();

        int id = (int) map.get("id");
        int taskId = 2;

        passTask(taskId, true);
    }

    public void userPassesTask(int userId, int taskId) {
        passTaskWithUser(userId, taskId, false);
    }


    public void passTask(int taskId, boolean broadcast) {
        Map<String, Object> map = ThreadLocalUtil.get();
        int id = (int) map.get("id");
        passTaskWithUser(id, taskId, broadcast);
        Task task = taskMapper.getTask(taskId);
        System.out.println("[" + DateLogger.getTime() + " Task Pass] User " + map.get("username") + " has successfully completed the task " + taskId + " (" + task.getTaskTitle() + ")");
    }

    private void passTaskWithUser(int id, int taskId, boolean broadcast) {
        TaskUser taskUser = taskUserMapper.getTaskUser(id, taskId);
        boolean update = true;
        if(taskUser == null) {
            update = false;
            taskUser = new TaskUser();
            taskUser.setPoint(0);
        }
        Task task = taskMapper.getTask(taskId);

        taskUser.setUserId(id);
        taskUser.setTaskId(taskId);
        taskUser.setStatus(1);
        taskUser.setTime(LocalDateTime.now());

        int pointDelta = task.getTaskPoint() - taskUser.getPoint();
        taskUser.setPoint(pointDelta);

        if (update) taskUserMapper.update(taskUser);
        else taskUserMapper.insert(taskUser);

        int prevPoint = userTotalPointMapper.getMaxPoint(id);
        userTotalPointMapper.addPoint(id, prevPoint + pointDelta);
        userMapper.updatePoint(id, prevPoint + pointDelta);

        if (userStickerMapper.findSticker(id, taskId) == null) {
            Sticker sticker = new Sticker();
            sticker.setStkId(taskId);
            sticker.setShow(false);
            sticker.setX(0.5);
            sticker.setY(0.5);
            userStickerMapper.addSticker(id, sticker);
        }
        else broadcast = false;

        User user = userMapper.getUser(id);

        int flag = userHintMapper.find(id, taskId);
        if(flag == 0 || task.getHintPrice() == 0) {
            int coinDelta = (int)(pointDelta * task.getTaskCoin() / (double)task.getTaskPoint());
            userMapper.updateUserCoins(id, user.getCoin() + coinDelta);
        }

        // 播报任务完成
        if (broadcast){
            int num = taskUserMapper.getFullCompletedNum(taskId);
            if(num <= 3 || taskId == 22) {
                try {
                    broadcastTask(task.getTaskTitle(), user.getUsername(), num);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void passPartialTask(int userId, int taskId, int point, boolean markPassed) {
        Map<String, Object> map = ThreadLocalUtil.get();
        int pre_point = userTotalPointMapper.getMaxPoint(userId);
        User user = userMapper.getUser(userId);

        Task task = taskMapper.getTask(taskId);
        double ratio =  (double)task.getTaskCoin() / task.getTaskPoint();

        TaskUser taskUser = taskUserMapper.getTaskUser(userId, taskId);
        if(taskUser != null) {
            if (taskUser.getPoint() >= point) return;
            int delta = point - taskUser.getPoint();
            taskUser.setPoint(point);
            taskUser.setTime(LocalDateTime.now());
            taskUser.setStatus(markPassed ? 1 : 0);

            taskUserMapper.update(taskUser);
            userTotalPointMapper.addPoint(userId,pre_point + delta);
            userMapper.updatePoint(userId, pre_point + delta);
            userMapper.updateUserCoins(userId, user.getCoin() + (int)(delta * ratio));
        }
        else{
            taskUser = new TaskUser();
            taskUser.setUserId(userId);
            taskUser.setTaskId(taskId);
            taskUser.setPoint(point);
            taskUser.setTime(LocalDateTime.now());
            taskUser.setStatus(markPassed ? 1 : 0);
            taskUserMapper.insert(taskUser);
            userTotalPointMapper.addPoint(userId,pre_point + point);
            userMapper.updatePoint(userId, pre_point + point);
            userMapper.updateUserCoins(userId, user.getCoin() + (int)(point * ratio));
        }
        if(taskUser.getStatus() == 1) {
            Sticker sticker = new Sticker();
            sticker.setStkId(taskId);
            sticker.setShow(false);
            sticker.setX(0.5);
            sticker.setY(0.5);
            userStickerMapper.addSticker(userId, sticker);
        }
        System.out.println("[" + DateLogger.getTime() + " Answer partial] User " + userId + " (" + user.getUsername() + ") has get " + point + " points of the task " + taskId + " (" + task.getTaskTitle() + ")");
    }

    public void onlySetFinished(int userId, int taskId) {
        TaskUser taskUser = taskUserMapper.getTaskUser(userId, taskId);
        if(taskUser == null) {
            taskUser = new TaskUser();
            taskUser.setUserId(userId);
            taskUser.setTaskId(taskId);
            taskUser.setPoint(0);
            taskUser.setTime(LocalDateTime.now());
            taskUser.setStatus(1);
            taskUserMapper.insert(taskUser);
        } else {
            if (taskUser.getStatus() == 1) return;
            taskUser.setStatus(1);
            taskUserMapper.update(taskUser);
        }
        Sticker sticker = new Sticker();
        sticker.setStkId(taskId);
        sticker.setShow(false);
        sticker.setX(0.5);
        sticker.setY(0.5);
        userStickerMapper.addSticker(userId, sticker);
    }

    private void broadcastTask(String taskTitle, String username, int rank) throws IOException {
        String groupId = "148357672";
        // 1st【昵称】成为解决危机【题目名】的第一名外勤员，异常部特此为其颁发精金奖章。
        String message = "";
        if (Objects.equals(taskTitle, "上古方块（真）")) {
            message = "【" + username + "】" + "找回了记忆。";
        } else if(rank == 1) {
            message = rank + "st【" + username + "】成为解决危机【" + taskTitle + "】的第一名外勤员，异常部特此为其颁发精金奖章。";
        } else if(rank == 2) {
            message = rank + "nd【" + username + "】成为解决危机【" + taskTitle + "】的第二名外勤员，异常部特此为其颁发秘银奖章。";
        } else if(rank == 3) {
            message = rank + "rd【" + username + "】成为解决危机【" + taskTitle + "】的第三名外勤员，异常部特此为其颁发山铜奖章。";
        }
        System.out.println(DateLogger.getTime() + ' ' + message);


        // 对整个消息进行URL编码
        String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8.toString());

        // 构造URL
        String urlString = "http://127.0.0.1:8083/send_group_msg?group_id=" + groupId + "&message=" + encodedMessage;

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            System.out.println("消息发送成功！");
        } else {
            System.out.println("消息发送失败，响应码: " + responseCode);
        }

        connection.disconnect();
    }
}
