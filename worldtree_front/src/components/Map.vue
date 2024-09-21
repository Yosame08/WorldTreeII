<template>
  <div class="map-container" @mousedown="startDrag" @mousemove="onDrag" @mouseup="endDrag" @mouseleave="endDrag"
       @wheel="onWheel" @touchstart="startTouch" @touchmove="onTouchMove" @touchend="endTouch">
    <img :src="require('@/assets/abstract_map.png')" :style="mapStyle" ref="mapImage" />

    <!-- Filter slider -->
    <div class="filter-slider">
      <div>
        <el-button type="primary" @click="refreshTasks" style="width: 100%; margin-bottom: 5px;">刷新任务情况</el-button>
      </div>
      <div>
        <el-switch v-model="showUncompletedOnly" active-text="只显示未完成"></el-switch>
      </div>
    </div>

    <!-- Zoom controls -->
    <div class="zoom-controls">
      <button @click="zoomIn">+</button>
      <button @click="zoomOut">-</button>
    </div>

    <!-- Task bubbles -->
    <img v-for="task in filteredTasks" :key="task.taskId" :src="require('@/assets/task_icon.png')" class="task-icon" :style="bubbleStyle(task)"
         @click="selectTask(task)" @mouseover="(event) => showTooltip(task, event)" @mouseleave="hideTooltip" />

    <!-- Tooltip -->
    <div v-if="tooltip.visible" :style="tooltipStyle" class="tooltip">
      <p>{{ tooltip.task.taskTitle }}</p>
      <p>{{ taskStatusText(tooltip.task.taskStatus) }}</p>
    </div>

    <!-- Task description sidebar with transition -->
    <transition name="slide">
      <div class="task-sidebar" v-if="taskDetail">
        <div class="task-sidebar-content">
          <task-info v-if="discussions.length" :discussions="discussions" />
        </div>
        <div class="task-sidebar-footer">
          <!-- 0. 如果有remark，展示remark -->
          <p v-if="taskDetail.remark">{{ taskDetail.remark }}</p>
          <!-- 1. 展示积分和奖励 -->
          <p class="inline-elements">积分: {{ taskDetail.getPoint }}/{{ taskDetail.taskPoint }}pts 奖励: {{ taskDetail.taskCoin }}货币
            <el-button v-if="taskDetail.uri" type="primary" @click="openUriInNew(taskDetail.uri)">打开链接</el-button>
          </p>
          <!-- 2. 展示提交答案方式 -->
          <div v-if="taskDetail.taskStatus === 1 && taskDetail.getPoint === taskDetail.taskPoint">
            您已经完全解决了该事件！
            <el-button v-if="taskDetail.taskId !== 2" @click="getClue" class="hint-button" type="primary">查看笔记残页</el-button>
          </div>
          <div v-else-if="taskDetail.taskId === 1" class="submission-container"> <!-- 鸳鸯锅 / 时间二分 is special -->
            <el-button v-if="task1OK" @click="submitId1" style="width: 100%;" type="primary">启动中继器({{task1Times}}/2)</el-button>
            <el-button v-else style="width: 100%;" type="info">当前无法启动中继器({{task1Times}}/2)</el-button>
          </div>
          <div v-else-if="taskDetail.submission" class="submission-container">
            <el-input v-model="taskAnswer" placeholder="输入答案"></el-input>
            <el-button @click="submitAnswer" type="primary">提交答案</el-button>
          </div>
          <!-- 3. 展示提示按钮 -->
          <el-button v-if="taskDetail.hintStatus === 0 && taskDetail.taskStatus !== 1" @click="getHint" class="hint-button">花费{{ taskDetail.hintPrice }}货币获取提示</el-button>
          <el-button v-else-if="taskDetail.hintStatus === 0 && taskDetail.taskStatus === 1" @click="getHint" class="hint-button">已解决该事件，可免费获取提示</el-button>
          <el-button v-else-if="taskDetail.hintStatus === 1" @click="getHint" class="hint-button">已获取提示</el-button>
        </div>
        <el-button class="close-button" @click="closeSidebar">×</el-button>
      </div>
    </transition>

    <HintOverlay :imageBase64="imageBase64" :visible="isHintVisible" @close="isHintVisible = false" />
  </div>
</template>

<script setup>
import TaskInfo from './TaskInfo.vue';
import HintOverlay from "@/components/HintOverlay.vue";
import {computed, onMounted, ref} from 'vue';
import {ElButton, ElInput, ElSwitch} from 'element-plus';
import {universalGet, universalPost} from "@/services/universalService";
import store from "@/services/storeService";

// map variables
const scale = ref(1);
const minScale = 1;
const maxScale = 2;
const startX = ref(0);
const startY = ref(0);
const translateX = ref(0);
const translateY = ref(0);
const dragging = ref(false);
const touchStartDistance = ref(0);
const showUncompletedOnly = ref(false);
// task variables
const tasks = ref([]);
const taskDetail = ref(null);
const discussions = ref([]);
const tooltip = ref({visible: false, task: null, x: 0, y: 0});
const taskAnswer = ref('');
const task1OK = ref(false);
const task1Times = ref(0);
// hint and clue
const mapImage = ref(null);
const isHintVisible = ref(false);
const imageBase64 = ref('');

const mapStyle = computed(() => ({
  transform: `scale(${scale.value}) translate(${translateX.value}px, ${translateY.value}px)`,
  transformOrigin: '0 0',
  width: '100%',
  cursor: dragging.value ? 'grabbing' : 'grab',
  userSelect: 'none',
}));

const getAllTasks = async () => {
  try {
    const response = await universalGet('/api/task/get_task_list');
    if (response.data.code === 0) {
      tasks.value = response.data.data;
      console.log(tasks.value);
    }
    else {
      store.commit("setErrorMsg", response.data.message);
    }
  } catch (error) {
    store.commit("setErrorMsg", error);
  }
};

const refreshTasks = async () => {
  await getAllTasks();
  if (taskDetail.value != null) {
    console.log(taskDetail.value.taskId)
    await selectTask(taskDetail.value);
    closeSidebar();
  }
};

const refreshTask1 = async () => {
  let result = await universalGet('/api/subtask/binsearch/check');
  task1OK.value = result.data.data.canSubmit;
  task1Times.value = result.data.data.chance;
};

const submitId1 = async () => {
  try {
    const response = await universalGet('/api/subtask/binsearch/query');
    if (response.data.code === 0) {
      let pass = response.data.data;
      if (pass === 0) {
        await getAllTasks();
        taskDetail.value = tasks[taskDetail.value.taskId];
        store.commit("setSuccessMsg", "已成功建立连接");
      } else {
        store.commit("setErrorMsg", pass === 1 ? "中继器启动过早！" : (pass === 2 ? "中继器启动过晚！" : "能量已不足再次启动中继器！"));
      }
      await refreshTask1();
    }
  } catch (error) {
    console.error('Error submitting answer:', error);
  }
};

const submitAnswer = async () => {
  try {
    const response = await universalPost('/api/task/submit', {taskId: taskDetail.value.taskId, answer: taskAnswer.value});
    if (response.data.code === 0) {
      let pass = response.data.data;
      if (pass) {
        await getAllTasks();
        taskDetail.value = tasks[taskDetail.value.taskId];
        store.commit("setSuccessMsg", "答案正确，获得笔记残页和贴纸，请及时查看");
      } else {
        store.commit("setErrorMsg", "答案错误");
      }
    }
  } catch (error) {
    console.error('Error submitting answer:', error);
  }
};

const openUriInNew = (uri) => {
  window.open(uri, '_blank');
};

const parseTaskDescription = (description) => {
  discussions.value = description.split('`').map((item) => {
    const match = item.match(/^\[(.*?)\](.*)$/);
    if (match) {
      return {user: match[1], content: match[2]};
    } else {
      return {user: null, content: item};
    }
  });
};

const selectTask = async (task) => {
  try {
    if (task.taskId === 1) await refreshTask1();
    let detail = task;
    parseTaskDescription(detail.taskStatus === 1 ? detail.taskDescriptionFull : detail.taskDescription);
    taskDetail.value = detail;
  } catch (error) {
    alert('Error selecting task:' + error);
    taskDetail.value = null;
    discussions.value = [];
  }
};

const closeSidebar = () => {
  taskDetail.value = null;
};

const getHint = async () => {
  try {
    const msg = await universalPost('/api/task/hint', {
      taskId: taskDetail.value.taskId,
    });
    if (msg.data.code === 0) {
      closeSidebar();
      imageBase64.value = "data:image/png;base64," + msg.data.data;
      isHintVisible.value = true;
      await getAllTasks();
    } else {
      store.commit("setErrorMsg", msg.data.message);
    }
  } catch (err) {
    store.commit("setErrorMsg", err);
  }
};

const getClue = async () => {
  try {
    const msg = await universalPost('/api/task/clue', {
      taskId: taskDetail.value.taskId,
    });
    if (msg.data.code === 0) {
      imageBase64.value = "data:image/jpg;base64," + msg.data.data;
      isHintVisible.value = true;
    } else {
      store.commit("setErrorMsg", msg.data.message);
    }
  } catch (err) {
    store.commit("setErrorMsg", err);
  }
};

const bubbleStyle = (task) => {
  const mapWidth = mapImage.value?.width || 0;
  const mapHeight = mapImage.value?.height || 0;
  const scaledWidth = mapWidth * scale.value;
  const scaledHeight = mapHeight * scale.value;
  const offsetX = translateX.value * scale.value;
  const offsetY = translateY.value * scale.value;
  const picX = task.taskPosX * scaledWidth + offsetX;
  const picY = task.taskPosY * scaledHeight + offsetY;

  return {
    left: `${picX}px`,
    top: `${picY+43}px`,
    transform: `translate(-50%, calc(-100% + 30px)) scale(25%)`,
    position: 'absolute',
  };
};

const tooltipStyle = computed(() => ({
  left: `${tooltip.value.x}px`,
  top: `${tooltip.value.y}px`,
  position: 'absolute',
  backgroundColor: 'white',
  border: '1px solid black',
  padding: '5px',
  zIndex: 1000,
}));

const zoom = (delta, event) => {
  const newScale = Math.min(Math.max(scale.value + delta, minScale), maxScale);
  const zoomCenter = getZoomCenter(event);
  translateX.value = (translateX.value - zoomCenter.x) * (newScale / scale.value) + zoomCenter.x;
  translateY.value = (translateY.value - zoomCenter.y) * (newScale / scale.value) + zoomCenter.y;
  scale.value = newScale;
  checkBounds();
};

const zoomIn = (event) => {
  zoom(0.1, event);
};

const zoomOut = (event) => {
  zoom(-0.1, event);
};

const getZoomCenter = (event) => {
  const container = mapImage.value?.parentElement;
  if (event && container) {
    const rect = container.getBoundingClientRect();
    return {
      x: event.clientX - rect.left,
      y: event.clientY - rect.top,
    };
  } else if (container) {
    return {
      x: container.clientWidth / 2,
      y: container.clientHeight / 2,
    };
  }
  return {x: 0, y: 0};
};

const onWheel = (event) => {
  event.preventDefault();
  if (event.deltaY < 0) {
    zoomIn(event);
  } else {
    zoomOut(event);
  }
};

const startDrag = (event) => {
  if (dragging.value) return;
  event.preventDefault();
  const rect = mapImage.value.getBoundingClientRect();
  if (
      event.clientX >= rect.left &&
      event.clientX <= rect.right &&
      event.clientY >= rect.top &&
      event.clientY <= rect.bottom
  ) {
    dragging.value = true;
    startX.value = event.clientX - translateX.value;
    startY.value = event.clientY - translateY.value;
  }
};

const onDrag = (event) => {
  if (dragging.value) {
    translateX.value = event.clientX - startX.value;
    translateY.value = event.clientY - startY.value;
    checkBounds();
  }
};

const endDrag = () => {
  dragging.value = false;
};

const startTouch = (event) => {
  if (event.touches.length === 2) {
    touchStartDistance.value = getTouchDistance(event.touches);
  }
};

const onTouchMove = (event) => {
  if (event.touches.length === 2) {
    const currentDistance = getTouchDistance(event.touches);
    if (currentDistance > touchStartDistance.value) {
      zoomIn();
    } else {
      zoomOut();
    }
    touchStartDistance.value = currentDistance;
  }
};

const endTouch = () => {
  touchStartDistance.value = 0;
};

const getTouchDistance = (touches) => {
  const [touch1, touch2] = touches;
  return Math.sqrt(
      Math.pow(touch2.clientX - touch1.clientX, 2) +
      Math.pow(touch2.clientY - touch1.clientY, 2)
  );
};

const checkBounds = () => {
  const container = mapImage.value?.parentElement;
  const img = mapImage.value;
  if (!container || !img) return;
  const minTranslateX = -(img.width * scale.value - container.clientWidth) / scale.value;
  const minTranslateY = -(img.height * scale.value - container.clientHeight) / scale.value;
  translateX.value = Math.min(Math.max(translateX.value, minTranslateX), 0);
  translateY.value = Math.min(Math.max(translateY.value, minTranslateY), 0);
};

const filteredTasks = computed(() => {
  return showUncompletedOnly.value
      ? tasks.value.filter(task => task.taskStatus === 0)
      : tasks.value;
});

const showTooltip = (task, event) => {
  tooltip.value = {
    visible: true,
    task,
    x: event.clientX,
    y: event.clientY - 20,
  };
};

const hideTooltip = () => {
  tooltip.value.visible = false;
};

const taskStatusText = (status) => {
  return status === 0 ? '未完成' : '已完成';
};

onMounted(getAllTasks);
</script>

<style>
.map-container {
  position: relative;
  overflow: hidden;
  width: 100%;
  height: calc(100vh - 43px);
  background-image: url('@/assets/map_back.png');
  background-size: cover;
  background-position: center;
}

.zoom-controls {
  position: absolute;
  bottom: 10px;
  left: 10px;
  display: flex;
  flex-direction: column;
}

.task-sidebar {
  position: fixed;
  top: 43px; /* Start 43px from the top */
  right: 0;
  width: 25%;
  height: calc(100% - 83px); /* 43 + 20 * 2 (padding) */
  background-color: #fff;
  border-left: 1px solid #ccc;
  padding: 20px;
  box-shadow: -2px 0 5px rgba(0, 0, 0, 0.1);
  z-index: 1002;
  display: flex;
  flex-direction: column;
}

.task-sidebar-content {
  flex: 1;
  overflow-y: auto;
}

.task-sidebar-footer {
  padding: 10px;
  background-color: #f9f9f9;
  border-top: 1px solid #ccc;
  margin-bottom: 20px;
}

.close-button {
  position: absolute;
  top: 5px;
  right: 5px;
  font-size: 20px;
  background: none;
  border: none;
  cursor: pointer;
}

.filter-slider {
  position: absolute;
  top: 10px;
  left: 10px;
  z-index: 1000;
  background-color: rgba(255, 255, 255, 0.8);
  padding: 5px;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.tooltip {
  position: absolute;
  background-color: white;
  border: 1px solid black;
  padding: 5px;
  z-index: 1000;
}

.submission-container {
  display: flex;
  align-items: center;
}

.submission-container .el-input {
  flex: 1;
  margin-right: 10px;
}

/* Enter and leave transitions for the sidebar */
.slide-enter-active, .slide-leave-active {
  transition: transform 0.3s ease;
}

.slide-enter-from, .slide-leave-to {
  transform: translateX(100%);
}

.slide-enter-to, .slide-leave-from {
  transform: translateX(0);
}

.hint-button{
  margin-top: 10px;
  width: 100%;
}

.inline-elements {
  display: flex;
  align-items: center;
  justify-content: center; /* 居中对齐 */
  gap: 10px; /* 设置间隔距离，可以根据需要调整 */
}

</style>