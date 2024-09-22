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

        passTask(taskId);
    }


    public void passTask(int taskId) {
        Map<String, Object> map = ThreadLocalUtil.get();
        int id = (int) map.get("id");

        TaskUser taskUser = taskUserMapper.getTaskUser(id, taskId);
        if(taskUser != null) {
            return ;
        }
        Task task = taskMapper.getTask(taskId);
        taskUser = new TaskUser();
        taskUser.setUserId(id);
        taskUser.setTaskId(taskId);
        taskUser.setStatus(1);
        taskUser.setPoint(task.getTaskPoint());
        taskUser.setTime(LocalDateTime.now());
        taskUserMapper.insert(taskUser);

        // 播报任务完成
        int num = taskUserMapper.getTaskUserNum(taskId);
        User user = userMapper.getUser(id);

        if(num <= 3) {
            // 播报任务完成
            try {
                broadcastTask(task.getTaskTitle(), user.getUsername(), num);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("[" + DateLogger.getTime() + " Answer submission] User " + id + " (" + user.getUsername() + ") has successfully completed the task " + taskId + " (" + task.getTaskTitle() + ")");

        int pre_point = userTotalPointMapper.getMaxPoint(id);
        // 输出pre_point

        userTotalPointMapper.addPoint(id,pre_point + task.getTaskPoint());

        userMapper.updatePoint(id, pre_point + task.getTaskPoint());


        Sticker sticker = new Sticker();
        sticker.setStkId(taskId);
        sticker.setShow(false);
        sticker.setX(0.5);
        sticker.setY(0.5);
        // userStickerMapper.modifyStickers(id, sticker);
        userStickerMapper.addSticker(id, sticker);

        int flag = userHintMapper.find(id, taskId);

        if(flag == 0) {
            userMapper.updateUserCoins(id, user.getCoin() + task.getTaskCoin());
        }
    }


    public void broadcastTask(String taskTitle, String username, int rank) throws IOException {
        String groupId = "148357672";
        // 1st【昵称】成为解决危机【题目名】的第一名外勤员，异常部特此为其颁发精金奖章。
        String message = "";
        if(rank == 1) {
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
