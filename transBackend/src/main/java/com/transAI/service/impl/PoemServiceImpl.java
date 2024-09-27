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
        poems.add(new String[]{"“我可以告诉你千万个理由，但我想，你已经有自己的答案了。”",
                "“所以，这一切的意义是什么？”",
                "“只要能拖延到那一天到来，哪怕只差一天，我们所做的一切都有意义。”",
                "“……不可理喻。”",
                "“就为了让这个脆弱的世界多存续几天，你们辗转了几千年？”",
                "“你其实非常清楚我们在做什么。”",
        });
        poems.add(new String[]{"这首小诗只这一句话。"});
        poems.add(new String[]{"“晚安。”",
                "“明天还要追逐未竟的目标。”",
                "“明天还要相遇许多难忘的人。”",
                "“明天还有更重要的事情亟待解决。”",
                "“这不是旅途的终点，不要被羁绊住脚步。”",
                "“走吧，孩子们，我的时间不多了。”"
        });
        poems.add(new String[]{"一觉睡到十月三",
                "往来的客人借五灵",
                "我家有个夜哭郎",
                "天惶惶，地惶惶"
        });
        poems.add(new String[]{"我是时光的逆行者",
                "未来是灰烬般的绝望",
                "却总能想起他自顾自地低语着",
                "面对着来时的我将去的方向",
                "“哪怕这座倒退的残缺的时间的宫殿落着叶",
                "要相信存在零落的破晓的晨曦的光”",
                "我拖着未凋敝的残躯 一路舟车",
                "为了前往一切尚未开始的地方"
        });
        poems.add(new String[]{"天地万物都造齐了。到第七日，神造物的工已经完毕，就在第七日歇了他一切的工，去吃饭了。神赐福给第七日，定为饭日；因为在这日，神歇了他一切创造的工，就去吃饭了。",
                "有薯条，有番茄酱，是第六日。",
                "神说：「要有生灵与我一起分享这诸多美食。」于是人类诞生了。",
                "神说：「这样下去太无聊了。」于是第四日，第五日被略去了，到了第六日。",
                "神说：「晚上的饭要聚在一起，使夜宵露出来。」神称深夜的饭为「夜宵」，把日落时分的饭称作「晚餐」。神说：「夜宵可以有零食，也可以有主食。」于是夜宵的花样就变多了。神看着是好的。有薯条，有番茄酱，是第三日。",
                "神说：「主食之间要有零食，将主食分为三顿。」神就创造出零食，将第一顿主食称作「早餐」，第二顿称作「午餐」，第三顿称作「晚餐」。有薯条，有番茄酱，是第二日。",
                "神说：「要有土豆」，就有了土豆。神看土豆是好的，就把土豆和番茄分开了。神称土豆为「条」，称番茄为「酱」。有薯条，有番茄酱，这是头一日。",
                "起初，神创造食物。零食是一片空白，天地间只有主食；神的灵魂运行在主食上。"
        });
        poems.add(new String[]{
                "“……”",
                "“那现在，让我猜猜，你是谁。”",
                "“白寂，对吧？”",
                "“猜不出来？那让我猜猜，大名鼎鼎的探长，来自事务所的热心人，究竟是谁呢？”",
                "“你猜~”",
                "“……你是谁？”"
        });
    }
    private final List<String> answers = new ArrayList<>();
    {
        answers.add("6");
        answers.add("1");
        answers.add("6");
        answers.add("肯德基疯狂星期四V我50");
        answers.add("8");
        answers.add("D");
        answers.add("乐逝");
    }
    private final List<String> questions = new ArrayList<>();
    {
        questions.add("这段话一共有多少行？");
        questions.add("这段话一共有多少行？");
        questions.add("这段话一共有多少行？");
        questions.add("这首诗在说什么（答案包含关键词即可）？");
        questions.add("这段话一共有多少行？");
        questions.add("乐逝花了几天创造一切？A.7 B.6 C.5 D.4（输入单个大写字母）");
        questions.add("不知道名字的人是实际是谁？");
    }
    private final List<String> titles = new ArrayList<>();
    {
        titles.add("另一个世界 其三（教学关，不计分）");
        titles.add("乐逝小诗 其三");
        titles.add("另一个世界 其二");
        titles.add("乐逝小诗 其二");
        titles.add("另一个世界 其一");
        titles.add("乐逝小诗 其一");
        titles.add("蒸旦的世界 其一");
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
            return new PoemCheck(false, "已经没有更多的文字了【此句不计入总行数】\n" + sb, titles.get(level), questions.get(level));
        }
        System.out.println("[" + DateLogger.getTime() + " Poem] User " + map.get("username") + " Poem Level: " + level + " Get Poem: " + times);
        if (level > poems.size()) {
            return new PoemCheck(false, "已经没有更多的文字了【此句不计入总行数】", titles.get(level), questions.get(level));
        }
        if (poems.get(level).length <= times) {
            System.out.println("User " + map.get("username") + " Boom! in Poem");
            poemMapper.setOver(user_id);
            // boom, next level
            if (level == poems.size() - 1) {
                // No next level
                tartsServiceImpl.onlySetFinished(user_id, taskId);
            }
            return new PoemCheck(false, "已经没有更多的文字了【此句不计入总行数】", titles.get(level), questions.get(level));
        } else {
            return new PoemCheck(true, poems.get(level)[times], titles.get(level), questions.get(level));
        }
    }

    @Override
    public PoemCheck submitAnswer(String answer) {
        Map<String, Object> map = ThreadLocalUtil.get();
        int user_id = (int)map.get("id");
        if (poemMapper.getOver(user_id)) {
            return new PoemCheck(false, "-1", "", "");
        }
        int level = poemMapper.getLevel(user_id);
        System.out.println("[" + DateLogger.getTime() + " Poem] User " + map.get("username") + " Poem Level: " + level + " Submit Answer: " + answer);

        boolean pass = false;
        if (level == 3){
            int score = 0;
            answer = answer.toLowerCase();
            if (answer.contains("肯德基") || answer.contains("kfc")) {
                score += 1;
            }
            if (answer.contains("疯狂星期四") || answer.contains("星期四") || answer.contains("thursday")) {
                score += 1;
            }
            if (answer.contains("v50") || answer.contains("v我50") || answer.contains("v你50")) {
                score += 1;
            }
            if (score >= 1) pass = true;
        }
        else if(level == 6){
            pass = answer.equals("乐逝") || answer.equals("乐逝大魔王") || answer.equals("乐逝魔王") || answer.equals("大魔王") || answer.equals("魔王");
        }
        else pass = answers.get(level).equals(answer);
        poemMapper.setOver(user_id);
        if (pass) {
            pass();
            return new PoemCheck(true, "回答正确", "", "");
        } else {
            return new PoemCheck(false, "回答错误", "", "");
        }
    }

    public void pass(){
        Map<String, Object> map = ThreadLocalUtil.get();
        int user_id = (int)map.get("id");
        if (poemMapper.getLevel(user_id) != 0){
            Task task = taskMapper.getTask(taskId);
            int singleScore = task.getTaskPoint() / 6;
            TaskUser taskUser = taskUserMapper.getTaskUser(user_id, taskId);
            tartsServiceImpl.passPartialTask(user_id, taskId, (taskUser!=null ? taskUser.getPoint() : 0) + singleScore, false);
        }
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
