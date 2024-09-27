package com.transAI.service.impl;

import com.transAI.mapper.PoemMapper;
import com.transAI.mapper.TaskMapper;
import com.transAI.mapper.TaskUserMapper;
import com.transAI.pojo.Task;
import com.transAI.pojo.TaskUser;
import com.transAI.pojo.poem.PoemCheck;
import com.transAI.service.PoemService;
import com.transAI.utils.DateLogger;
import com.transAI.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PoemServiceImpl implements PoemService {

    @Autowired
    private final TartsServiceImpl tartsServiceImpl;

    @Autowired
    private final TaskMapper taskMapper;

    @Autowired
    private PoemMapper poemMapper;

    @Autowired
    private TaskUserMapper taskUserMapper;

    private final int taskId = 19;

    private final List<String[]> poems = new ArrayList<>();

    {
        poems.add(new String[]{"春眠不觉晓", "处处闻啼鸟", "夜来风雨声", "花落知多少"});
        poems.add(new String[]{"白日依山尽", "黄河入海流", "欲穷千里目", "更上一层楼"});
        poems.add(new String[]{"锄禾日当午", "汗滴禾下土", "谁知盘中餐", "粒粒皆辛苦"});
        poems.add(new String[]{"床前明月光", "疑是地上霜", "举头望明月", "低头思故乡"});
        poems.add(new String[]{"天街小雨润如酥", "草色遥看近却无", "最是一年春好处", "绝胜烟柳满皇都"});
        poems.add(new String[]{"千山鸟飞绝", "万径人踪灭", "孤舟蓑笠翁", "独钓寒江雪"});
    }
    private final List<String> answers = new ArrayList<>();
    {
        answers.add("春眠不觉晓处处闻啼鸟夜来风雨声花落知多少");
        answers.add("白日依山尽黄河入海流欲穷千里目更上一层楼");
        answers.add("锄禾日当午汗滴禾下土谁知盘中餐粒粒皆辛苦");
        answers.add("床前明月光疑是地上霜举头望明月低头思故乡");
        answers.add("天街小雨润如酥草色遥看近却无最是一年春好处绝胜烟柳满皇都");
        answers.add("千山鸟飞绝万径人踪灭孤舟蓑笠翁独钓寒江雪");
    }

    public PoemServiceImpl(TartsServiceImpl tartsServiceImpl, TaskMapper taskMapper) {
        this.tartsServiceImpl = tartsServiceImpl;
        this.taskMapper = taskMapper;
    }

    @Override
    public PoemCheck getPoem(Integer times) {
        Map<String, Object> map = ThreadLocalUtil.get();
        int user_id = (int)map.get("id");
        int have = poemMapper.checkHave(user_id);
        if (have == 0) poemMapper.insert(user_id, 0);
        int level = have == 0 ? 0 : poemMapper.getLevel(user_id);
        if (poemMapper.getOver(user_id) && times == 0) {
            String[] poem = poems.get(level);
            StringBuilder sb = new StringBuilder();
            for (int i = poem.length - 1; i >= 0; i--) {
                sb.append(poem[i]);
                if (i != 0) sb.append('\n');
            }
            return new PoemCheck(false, "已经没有更多的诗句了\n" + sb);
        }
        System.out.println("[" + DateLogger.getTime() + " Poem] User " + map.get("username") + " Poem Level: " + level + " Get Poem: " + times);
        if (level > poems.size()) {
            return new PoemCheck(false, "已经没有更多的诗句了");
        }
        if (poems.get(level).length <= times) {
            System.out.println("User " + map.get("username") + " Boom! in Poem");
            poemMapper.setOver(user_id);
            // boom, next level
            if (level == poems.size() - 1) {
                // No next level
                tartsServiceImpl.onlySetFinished(user_id, taskId);
            }
            return new PoemCheck(false, "已经没有更多的诗句了");
        } else {
            return new PoemCheck(true, poems.get(level)[times]);
        }
    }

    @Override
    public PoemCheck submitAnswer(String answer) {
        Map<String, Object> map = ThreadLocalUtil.get();
        int user_id = (int)map.get("id");
        if (poemMapper.getOver(user_id)) {
            return new PoemCheck(false, "-1");
        }
        int level = poemMapper.getLevel(user_id);
        System.out.println("[" + DateLogger.getTime() + " Poem] User " + map.get("username") + " Poem Level: " + level + " Submit Answer: " + answer);
        if (answers.get(level).equals(answer)) {
            pass();
            poemMapper.setOver(user_id);
            return new PoemCheck(true, "回答正确");
        } else {
            return new PoemCheck(false, "回答错误");
        }
    }

    public void pass(){
        Map<String, Object> map = ThreadLocalUtil.get();
        int user_id = (int)map.get("id");
        Task task = taskMapper.getTask(taskId);
        System.out.println(task);
        int singleScore = task.getTaskPoint() / 10;
        TaskUser taskUser = taskUserMapper.getTaskUser(user_id, taskId);
        tartsServiceImpl.passPartialTask(user_id, taskId, (taskUser!=null ? taskUser.getPoint() : 0) + singleScore, false);
        if (poemMapper.getLevel(user_id) == poems.size() - 1) {
            tartsServiceImpl.onlySetFinished(user_id, taskId);
        }
    }

    @Override
    public Boolean next() {
        Map<String, Object> map = ThreadLocalUtil.get();
        int user_id = (int)map.get("id");
        int level = poemMapper.getLevel(user_id);
        if (level == poems.size() - 1) {
            return false;
        }
        poemMapper.update(user_id, level + 1);
        return true;
    }
}
