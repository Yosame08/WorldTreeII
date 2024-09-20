<script setup>
import { ElCard } from 'element-plus';
import 'element-plus/dist/index.css';
import { onMounted, defineProps } from "vue";

const props = defineProps({
  discussions: {
    type: Array,
    required: true
  }
});

onMounted(() => {
  console.log(props.discussions);
});
</script>

<template>
  <div class="task-info discussions-style">
    <div v-for="discussion in props.discussions" :key="discussion.content">
      <el-card class="discussion-card" v-if="discussion.user">
        <template #header>
          <div>{{ discussion.user }}</div>
        </template>
        <div>{{ discussion.content }}</div>
      </el-card>
      <div class="discussion-card" v-else>
        {{ discussion.content }}
      </div>
    </div>
  </div>
</template>

<style scoped>
.task-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  max-width: 600px;
  margin: -10px auto;
  padding: -10px;
  box-sizing: border-box;
}

.discussions-style {
  width: 100%;
}

.discussion-card {
  width: calc(100% - 20px);
  margin: 0 10px 5px 10px;
  padding: 10px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  background-color: #fff;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  box-sizing: border-box; 
}
</style>