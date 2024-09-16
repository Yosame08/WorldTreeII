<template>
  <el-card v-if="taskDetails" class="task-info task-card">
    <template #header>
      <div>{{ taskDetails.taskTitle }}</div>
    </template>

    <div>{{ taskDetails.taskDescription }}</div>

    <div style="margin-top: 50px;"> </div>
    <p style="font-size: small; text-align: left;">积分: {{ taskDetails.taskPoint }} &nbsp;通过奖励: {{ taskDetails.getPoint }}</p>

    <!-- task.requireInput -->
    <div v-if="taskDetails.taskStatus" style="display: flex;  gap: 10px;">
      <el-input v-model="taskAnswer" placeholder="Enter your answer"></el-input>
      <el-button type="primary" @click="submitAnswer" style="margin-top: 0px;">提交</el-button>
    </div>
    <el-button @click="getHint" style="margin-top: 0px; margin-bottom: -10px;">花费 {{ taskDetails.taskCoin }}货币获取提示</el-button>
  </el-card>

  <div class="task-info discussions">
    <el-card v-for="discussion in discussions" :key="discussion.id" class="discussion-card">
      <template #header>
        <div>{{ discussion.user }}</div>
      </template>
      <div>{{ discussion.content }}</div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElCard, ElButton, ElInput } from 'element-plus';
import 'element-plus/dist/index.css';
import { submitTask, requestHint, getTask } from "@/services/taskService";

// 接受任务的 id 作为参数
const props = defineProps({
  id: {
    type: Number,
    required: true
  }
});

// 定义任务详情和用户讨论区的状态
const taskDetails = ref({
  "taskId": 1,
  "taskTitle": "true music",
  "taskPosX": 1,
  "taskPosY": 1,
  "taskDescription": "this is true music",
  "uri": "true music uri",
  "taskPoint": 50,
  "getPoint": 30,
  "taskCoin": 100,
  "hintPrice": 100,
  "taskStatus": 1
});
const taskAnswer = ref('');
const hintCost = ref(2); // 提示的花费货币数量

const discussions = ref([]);

// 获取任务详情的函数
const fetchTaskDetails = async () => {
  try {
    const response = await getTask(props.id);
    if (response.data.code === 0) {
      taskDetails.value = response.data.data[0];
    }
  } catch (error) {
    console.error('获取任务详情失败:', error);
  }
};

// 模拟获取用户讨论区的函数
const fetchDiscussions = () => {
  discussions.value = [
    { id: 1, user: '用户1', content: '这是一个讨论内容1' },
    { id: 2, user: '用户2', content: '这是一个讨论内容2' },
    // 可以添加更多讨论内容
  ];
};

// 组件挂载时获取任务详情和用户讨论区
onMounted(() => {
  fetchTaskDetails();
  fetchDiscussions();
});

const submitAnswer = async () => {
  console.log(`Answer submitted for task ${taskDetails.value.title}:`, taskAnswer.value);

  try {
    const res = await submitTask(taskDetails.value.id, taskAnswer.value);
    if (res.data.code === 200) {
      ElMessage.success('回答正确');
    } else {
      ElMessage.error('回答错误');
    }
    // Handle response
  } catch (err) { console.error(err); }

  taskAnswer.value = '';
};

const getHint = async () => {
  console.log(`Hint requested for task ${taskDetails.value.title}, cost: ${hintCost.value} currency`);

  try {
    const msg = await requestHint(taskDetails.value.id);

    ElMessage.info(msg);
  } catch (err) { console.error(err); }
};

</script>



<style scoped>
.task-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  max-width: 600px;
  margin: -10px auto;
  padding: -10px;
  /* 调整内边距 */
  box-sizing: border-box;
}

/* 神秘的计算式，，，显示效果正确就行 */
.task-card {
  width: calc(100% + 20px);
  /* 调整卡片宽度 */
  margin-left: -10px;
  /* 确保卡片与容器左边对齐 */
  margin-right: auto;
  /* 确保卡片与容器右边对齐 */
  margin-bottom: 30px;
  padding: -20px;
}

.discussions {
  width: 100%;
}

.discussion-card {
  width: calc(100% + 20px);
  /* 调整卡片宽度 */
  margin-left: -10px;
  /* 确保卡片与容器左边对齐 */
  margin-right: -10px;
  /* 确保卡片与容器右边对齐 */
  margin-bottom: 5px;
  /* 调整卡片之间的间距 */
}
</style>