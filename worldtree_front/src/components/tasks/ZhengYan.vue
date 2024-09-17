<template>
  <div class="zheng-yan">
    <div class="grid">
      <div
        v-for="(cell, index) in grid"
        :key="index"
        :class="['cell', { selected: index === selectedCell }]"
        @click="handleCellClick(index)"
      >
        {{ cell }}
      </div>
    </div>
    <div class="icons">
      <div
        v-for="(icon, index) in icons"
        :key="index"
        :class="['icon', { selected: selectedIcon === icon, used: usedIcons.includes(icon) }]"
        @click="handleIconSelect(icon)"
      >
        {{ icon }}
      </div>
    </div>
    <button @click="handleSubmit">输入蒸言程序</button>
  </div>
</template>

<script>
import { ref } from 'vue';
import axios from 'axios';

export default {
  setup() {
    const icons = ['丨零', '丨一', '丨二', '丨三', '丨四', '丨五', '丨六', '丨七', '丨八'];
    const grid = ref(Array(9).fill(null));
    const selectedIcon = ref(null);
    const usedIcons = ref([]);
    const selectedCell = ref(null);

    const handleCellClick = (index) => {
      if (selectedIcon.value !== null) {
        const newGrid = [...grid.value];
        if (newGrid[index] !== null) {
          usedIcons.value = usedIcons.value.filter(icon => icon !== newGrid[index]);
        }
        newGrid[index] = selectedIcon.value;
        grid.value = newGrid;
        usedIcons.value = [...usedIcons.value, selectedIcon.value];
        selectedIcon.value = null;
      } else if (selectedCell.value === null && grid.value[index] !== null) {
        selectedCell.value = index;
      } else if (selectedCell.value !== null) {
        const newGrid = [...grid.value];
        [newGrid[selectedCell.value], newGrid[index]] = [newGrid[index], newGrid[selectedCell.value]];
        grid.value = newGrid;
        selectedCell.value = null;
      }
    };

    const handleIconSelect = (icon) => {
      if (!usedIcons.value.includes(icon)) {
        selectedIcon.value = icon;
      }
    };

    const handleSubmit = async () => {
      try {
        const map = {
          "丨零": 0,
          "丨一": 1,
          "丨二": 2,
          "丨三": 3,
          "丨四": 4,
          "丨五": 5,
          "丨六": 6,
          "丨七": 7,
          "丨八": 8,
        };
        const answer = grid.value.map(icon => map[icon]);
        const response = await axios.post('/api/subtask/zhengyan/validate', JSON.stringify({
            "answer": answer,
        }));
        const result = await response.json();
        alert(result.message);
      } catch (error) {
        console.error('错误:', error);
        if (error instanceof Error) {
          alert('提交失败，错误信息：' + error.message);
        } else {
          alert('提交失败，发生未知错误');
        }
      }
    };

    return {
      icons,
      grid,
      selectedIcon,
      usedIcons,
      selectedCell,
      handleCellClick,
      handleIconSelect,
      handleSubmit,
    };
  },
};
</script>

<style scoped>
@font-face {
  font-family: 'AncientCube';
  src: url('@/assets/Ancient_Cube-Regular.woff2') format('woff2');
  font-weight: normal;
  font-style: normal;
}

.zheng-yan {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
}

.grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 5px;
  margin-bottom: 20px;
}

.cell {
  font-family: 'AncientCube', sans-serif;
  width: 60px;
  height: 60px;
  border: 3px solid #ccc;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 65px;
  cursor: pointer;
}

.icons {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.icon {
  font-family: 'AncientCube', sans-serif;
  font-size: 24px;
  cursor: pointer;
}

.icon.selected {
  border: 2px solid blue;
}

.icon.used {
  opacity: 0.4;
  cursor: not-allowed;
}

button {
  padding: 10px 20px;
  font-size: 16px;
}
</style>

