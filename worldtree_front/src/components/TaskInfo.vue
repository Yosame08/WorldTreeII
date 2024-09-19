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
    <el-card v-for="discussion in discussions" :key="discussion.content" class="discussion-card" v-if="discussion.user">
      <template #header>
        <div>{{ discussion.user }}</div>
      </template>
      <div>{{ discussion.content }}</div>
    </el-card>
    <div v-for="discussion in discussions" :key="discussion.content" v-else>
      {{ discussion.content }}
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
  width: calc(100% + 20px);
  margin-left: -10px;
  margin-right: -10px;
  margin-bottom: 5px;
}
</style>