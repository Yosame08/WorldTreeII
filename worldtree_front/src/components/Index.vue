<script setup>
import { ref } from 'vue';
import Home from './Home.vue';
import MapComponent from './Map.vue';
import { ElButton } from 'element-plus';
import 'element-plus/dist/index.css';
import { Location } from '@element-plus/icons-vue';
import TaskList from './TaskList.vue';

const currentView = ref('home');

const toggleView = () => {
  currentView.value = currentView.value === 'home' ? 'map' : 'home';
};
</script>

<template>
  <div style="display: flex;">
    <el-button
      type="primary"
      icon="Location"
      @click="toggleView"
      style="position: fixed; top: 42px; right: 20px; z-index: 1000;"
    >
      {{ currentView === 'home' ? '显示地图' : '显示主页' }}
    </el-button>
    <div v-if="currentView === 'home'" style="display: flex; flex: 1;">
      <Home style="flex: 1;" />
      <TaskList style="width: 300px; border-left: 1px solid #ddd; padding: 10px;" />
    </div>
    <div v-else style="flex: 1;">
      <MapComponent style="flex: 1;" />
    </div>
  </div>
</template>