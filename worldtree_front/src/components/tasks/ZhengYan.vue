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
    <button @click="handleSubmit">输入蒸盐程序</button>

    <div id='download-button'>
      <a href="/static/ZhengYan.pdf" download>蒸盐机器</a>
      <a href="/static/ZhengYanManual.pdf" download>蒸盐研究记录</a>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue';
import axios from 'axios';
import {universalPost} from "@/services/universalService";

export default {
  setup() {
    const icons = ['枧攕', '枧露', '枧爵', '枧筩', '枧濖', '枧堇', '枧渏', '枧禎', '枧镩'];
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
          "枧攕": 0,
          "枧露": 1,
          "枧爵": 2,
          "枧筩": 3,
          "枧濖": 4,
          "枧堇": 5,
          "枧渏": 6,
          "枧禎": 7,
          "枧镩": 8,
        };
        const answer = grid.value.map(icon => map[icon]);
        const response = await universalPost('/api/subtask/zhengyan/validate', {
            "answer": answer,
        });
        if (response.data.data) {
          alert('程序被成功启动了，你获得了新的笔记残页和贴纸');
        } else {
          alert('答案错误');
        }
      } catch (error) {
        console.error('错误:', error);
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
  src: url('@/assets/RAC.woff2') format('woff2');
  /* src: url('@/assets/Ancient_Cube-Regular.woff2') format('woff2'); */
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
  width: 56px;
  height: 56px;
  border: 3px solid #ccc;
  display: flex;
  justify-content: flex-start;
  align-items: center;
  font-size: 65px;
  cursor: pointer;
  padding: 3px;
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
  border: 1px solid black;
}

#download-button a {
  padding: 10px 20px;
  font-size: 16px;
  display: inline-block;
  margin: 20px 10px;
  /* grey */
  background-color: #eee;
  color: black;
  text-decoration: none;
  border-radius: 2px;
  border: 1px solid black;
  transition: background-color 0.3s ease;
}

</style>

