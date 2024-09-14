<template>
    <div class="task-list">
      <el-card v-for="task in tasks" :key="task.id" class="task-card">
        <template #header>
          {{ task.name }}
        </template>
        <!-- 这里可以添加更多任务的详细内容 -->
        <div>任务内容</div>
      </el-card>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  import { ElCard } from 'element-plus';
  import 'element-plus/dist/index.css';
  
  const tasks = ref([
    { id: 1, name: '任务一' },
    { id: 2, name: '任务二' },
    { id: 3, name: '任务三' },
    // 可以添加更多任务
  ]);
  </script>
  
  <style scoped>
  .task-list {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    width: 100%; /* 确保 TaskList 占据父容器的全部宽度 */
    max-width: 300px; /* 设置最大宽度，防止越界 */
    border: 1px solid #ddd; /* 添加边框 */
    padding: 10px; /* 添加内边距 */
    box-sizing: border-box; /* 确保 padding 包含在宽度内 */
  }
  
  .task-card {
    margin: 5px 0;
    width: 100%; /* 确保 TaskCard 占据 TaskList 的全部宽度 */
    box-sizing: border-box; /* 确保 padding 包含在宽度内 */
  }
  </style>