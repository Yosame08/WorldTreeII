<script setup>
import { ElCard } from 'element-plus';
import 'element-plus/dist/index.css';
import { defineProps, computed } from "vue";

const props = defineProps({
  discussions: {
    type: Array,
    required: true
  }
});

function stringToColor(str) {
  let hash = 0;
  for (let i = 0; i < str.length; i++) {
    hash = str.charCodeAt(i) + ((hash << 5) - hash);
  }
  let color = '#';
  for (let i = 0; i < 3; i++) {
    const value = (hash >> (i * 8)) & 0xFF;
    color += ('00' + value.toString(16)).substr(-2);
  }
  return color;
}

const cardLayers = computed(() => {
  let layer = 1;
  return props.discussions.map(discussion => {
    if (discussion.user) {
      return layer++;
    }
    return null;
  });
});
</script>

<template>
  <div class="task-info discussions-style">
    <div v-for="(discussion, index) in props.discussions" :key="discussion.content" >
      <el-card class="discussion-card" v-if="discussion.user">
        <template #header>
          <div class="username-container">
            <span class="username-line" :style="{ backgroundColor: stringToColor(discussion.user) }" ></span>
            <div :style="{ color: stringToColor(discussion.user) }" class="username">
              {{ discussion.user }}
            </div>
          </div>
        </template>
        <div>{{ discussion.content }}</div>
        <template #footer>
          <div class="card-footer">
            {{ cardLayers[index] }}F
          </div>
        </template>
      </el-card>
      <div class="no-card" v-else>
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
  box-sizing: border-box;
}

.discussions-style {
  width: 100%;
  margin: 0;
  display: flex;
  flex-direction: column;
  align-items: stretch; /* Ensure children stretch to fill the container */
}

.username-container {
  display: flex;
  align-items: center;
}

.username-line {
  width: 4px;
  height: 1em; /* Match the height of the text */
  background-color: black; /* Adjust color as needed */
  margin-left: 5px;
  margin-right: 10px; /* Space between line and username */
}

.discussion-card {
  width: 100%;
  margin: 10px 0; /* Add vertical margin */
  box-sizing: border-box; /* Ensure padding and border are included in the width */
}

:deep() .discussion-card .el-card__header { /* Adjust the padding of the card header */
  padding: 6px; /* 你可以根据需要调整这个值 */
}

:deep() .discussion-card .el-card__footer { /* Adjust the padding of the card header */
  padding: 6px; /* 你可以根据需要调整这个值 */
}

.username {
  text-align: left; /* Align username to the left */
  margin: 0; /* Remove margin */
}

.no-card{
  margin: 10px 0;
}

.card-footer {
  text-align: left;
  margin-left: 5px;
  font-size: 0.9em;
  color: #888;
}
</style>