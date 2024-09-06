<template>
  <div class="map-container" @mousedown="startDrag" @mousemove="onDrag" @mouseup="endDrag" @mouseleave="endDrag"
    @wheel="onWheel" @touchstart="startTouch" @touchmove="onTouchMove" @touchend="endTouch">
    <img :src="require('@/assets/fudan_map.png')" :style="mapStyle" ref="mapImage" />

    <!-- Filter slider -->
    <div class="filter-slider">
      <el-switch v-model="showUncompletedOnly" active-text="只显示未完成的题目"></el-switch>
    </div>

    <!-- Zoom controls -->
    <div class="zoom-controls">
      <button @click="zoomIn">+</button>
      <button @click="zoomOut">-</button>
    </div>

    <!-- Task bubbles -->
    <div v-for="task in filteredTasks" :key="task.id" class="task-bubble" :style="bubbleStyle(task)"
      @click="selectTask(task)">
      {{ task.title }}
    </div>

    <!-- Task description sidebar -->
    <div class="task-sidebar" :class="{ open: selectedTask }">
      <div v-if="selectedTask">
        <h3>{{ selectedTask.title }}</h3>
        <p>{{ selectedTask.description }}</p>
        <p>积分: {{ selectedTask.points }}</p>
        <p>通过后能得到的货币数量: {{ selectedTask.reward }}</p>
        <el-button @click="getHint">花费 {{ hintCost }} 货币获取提示</el-button>
        <div v-if="selectedTask.requiresInput">
          <el-input v-model="taskAnswer" placeholder="Enter your answer"></el-input>
          <el-button @click="submitAnswer">Submit</el-button>
        </div>
        <el-button @click="closeSidebar">Close</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { ElSwitch, ElButton, ElInput, ElMessage } from 'element-plus';
import { submitTask, requestHint } from "@/services/taskService";

const tasks = [
  { id: 1, title: 'Task 1', x: 100, y: 200, description: 'Description for Task 1', points: 10, reward: 5, requiresInput: true },
  { id: 2, title: 'Task 2', x: 300, y: 400, description: 'Description for Task 2', points: 20, reward: 10, requiresInput: false },
  // More tasks
];


const scale = ref(1);
const minScale = 1;
const maxScale = 5;
const startX = ref(0);
const startY = ref(0);
const translateX = ref(0);
const translateY = ref(0);
const dragging = ref(false);
const touchStartDistance = ref(0);
const selectedTask = ref(null);
const taskAnswer = ref('');
const hintCost = ref(2); // 提示的花费货币数量
const showUncompletedOnly = ref(false);

const mapImage = ref(null);

const mapStyle = computed(() => ({
  transform: `scale(${scale.value}) translate(${translateX.value}px, ${translateY.value}px)`,
  transformOrigin: '0 0',
  width: '100%',
  cursor: dragging.value ? 'grabbing' : 'grab',
  userSelect: 'none',
}));

const bubbleStyle = (task) => ({
  left: `${task.x * scale.value + translateX.value * scale.value}px`,
  top: `${task.y * scale.value + translateY.value * scale.value}px`,
  transform: `translate(-50%, -100%)`,
});

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
  return { x: 0, y: 0 };
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

const selectTask = (task) => {
  selectedTask.value = task;
};

const closeSidebar = () => {
  selectedTask.value = null;
};

const submitAnswer = async () => {
  console.log(`Answer submitted for task ${selectedTask.value.title}:`, taskAnswer.value);

  try {
    const res = await submitTask(selectedTask.value.id, taskAnswer.value);
    if(res.data.code === 200) {
      ElMessage.success('回答正确');
    } else {
      ElMessage.error('回答错误');
    }
    // Handle response
  } catch (err) { console.error(err); }

  taskAnswer.value = '';
  closeSidebar();
};

const getHint = async () => {
  console.log(`Hint requested for task ${selectedTask.value.title}, cost: ${hintCost.value} currency`);
  
  try {
    const msg = await requestHint(selectedTask.value.id);

    ElMessage.info(msg);
  } catch (err) { console.error(err); }
};

const filteredTasks = computed(() => {
  return showUncompletedOnly.value
    ? tasks.filter(task => !task.completed)
    : tasks;
});
</script>

<style>
.map-container {
  position: relative;
  overflow: hidden;
  width: 100%;
  height: calc(100vh - 39px);
  /* Adjust this value based on your NavBar height */
}

.map-container img {
  display: block;
  user-select: none;
}

.zoom-controls {
  position: absolute;
  bottom: 10px;
  left: 10px;
  display: flex;
  flex-direction: column;
}

.zoom-controls button {
  background-color: #fff;
  border: 1px solid #ccc;
  padding: 5px;
  margin: 2px;
  cursor: pointer;
}

.task-bubble {
  position: absolute;
  background-color: #ffeb3b;
  border: 1px solid #f0c02b;
  border-radius: 50%;
  padding: 5px 10px;
  white-space: nowrap;
  cursor: pointer;
  transform: translate(-50%, -100%);
}

.task-sidebar {
  position: fixed;
  top: 0;
  right: -100vw;
  /* Hide sidebar by default */
  width: 300px;
  height: 100%;
  background-color: #fff;
  border-left: 1px solid #ccc;
  padding: 20px;
  box-shadow: -2px 0 5px rgba(0, 0, 0, 0.1);
  transition: right 0.3s ease;
}

.task-sidebar.open {
  right: 0;
}

.task-sidebar h3 {
  margin-top: 0;
}

.task-sidebar .el-input {
  width: calc(100% - 20px);
  margin-bottom: 10px;
}

.task-sidebar .el-button {
  display: block;
  margin-top: 10px;
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
</style>@/services/taskService