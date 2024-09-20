<script setup>
import { ref, onMounted } from 'vue';
import { universalGet, universalPost } from "@/services/universalService";
import {
  Check,
  Close
} from '@element-plus/icons-vue'

const stickers = ref([{
    "stkId": 1,
    "show": true,
    "x": 0.566,
    "y": 0.788,
  },
  {
    "stkId": 4,
    "show": true,
    "x": 0.6,
    "y": 0.1,
  },
  {
    "stkId": 8,
    "show": false,
    "x": 0.6,
    "y": 0.1,
  },]);

const isShovelMode = ref(false);
const shovelPosition = ref([0, 0]);

// Fetch stickers from API
const fetchStickers = async () => {
  try {
    const response = await universalGet('/api/func/get_stickers');
    if (response.data.code === 0) {
      stickers.value = response.data.data;
      console.log(stickers.value);
    }
  } catch (error) {
    console.error('Failed to fetch stickers:', error);
  }
};

onMounted(() => {
  fetchStickers();
});

// Handle dragging a sticker
const dragSticker = (sticker, event) => {
  if (isShovelMode.value) {
    removeSticker(sticker);
    return; // Prevent dragging in shovel mode
  }
  event.preventDefault(); // 防止选中图片

  const startX = event.clientX;
  const startY = event.clientY;

  const initialX = sticker.x * window.innerWidth;
  const initialY = sticker.y * window.innerHeight;

  const onMouseMove = (e) => {
    const deltaX = e.clientX - startX;
    const deltaY = e.clientY - startY;

    // Calculate new position in percentages
    let newX = (initialX + deltaX) / window.innerWidth;
    let newY = (initialY + deltaY) / window.innerHeight;

    // Ensure the sticker's top is not above 43px
    const stickerHeight = 50; // Assuming the sticker height is 50px
    const minY = 43 / window.innerHeight;
    const maxY = (window.innerHeight - stickerHeight) / window.innerHeight;

    if (newY < minY) newY = minY;
    if (newY > maxY) newY = maxY;

    sticker.x = newX;
    sticker.y = newY;
  };

  const onMouseUp = async () => {
    document.removeEventListener('mousemove', onMouseMove);
    document.removeEventListener('mouseup', onMouseUp);

    try {
      console.log(sticker.stkId);
      await universalPost('/api/func/modify_stickers', {
        stkId: sticker.stkId,
        show: true,
        x: sticker.x,
        y: sticker.y,
      });
    } catch (error) {
      console.error('Failed to update sticker position:', error);
    }

    console.log('Sticker position updated:', {id: sticker.stkId, x: sticker.x, y: sticker.y});
  };

  document.addEventListener('mousemove', onMouseMove);
  document.addEventListener('mouseup', onMouseUp);
};

const showBag = ref(false);  // 控制背包弹出状态

// 显示背包中的贴纸
const showStickerFromBag = async (sticker) => {
  sticker.show = true; // 本地显示贴纸
  sticker.x = sticker.y = 0.5;
  try {
    await universalPost('/api/func/modify_stickers', {
      stkId: sticker.stkId,
      show: true,
      x: 0.5,
      y: 0.5,
    });
  } catch (error) {
    console.error('Failed to show sticker from bag:', error);
  }
};

// Handle shovel mode
const enterShovelMode = () => {
  isShovelMode.value = true;
  document.addEventListener('mousemove', updateShovelPosition);
  setTimeout(() => document.addEventListener('click', handleShovelClick), 50);
};

const updateShovelPosition = (event) => {
  shovelPosition.value = [ event.clientX, event.clientY ];
};

const handleShovelClick = (event) => {
  if (!event.target.closest('.sticker')) exitShovelMode();
};

// Remove a sticker
const removeSticker = async (sticker) => {
  sticker.show = false; // Locally update the state
  try {
    await universalPost('/api/func/modify_stickers', {
      stkId: sticker.stkId,
      show: false,
      x: 0.5,
      y: 0.5,
    });
  } catch (error) {
    console.error('Failed to remove sticker:', error);
  }
};

const exitShovelMode = () => {
  isShovelMode.value = false;
  document.removeEventListener('mousemove', updateShovelPosition);
  document.removeEventListener('click', handleShovelClick);
};

</script>

<template>
  <!-- Render stickers and buttons -->
  <div v-for="sticker in stickers" :key="sticker.stkId" v-show="sticker.show"
       @mousedown="dragSticker(sticker, $event)" :data-sticker-id="sticker.stkId">

    <img :style="{ left: `${sticker.x * 100}%`, top: `${sticker.y * 100}%` }" :src="require('@/assets/hex.png')"
         class="sticker" />
  </div>

  <!-- Shovel icon above backpack button -->
  <button class="shovel-btn" @click="enterShovelMode">
    <img :src="require('@/assets/shovel.png')" alt="Shovel" />
  </button>

  <!-- Backpack icon in bottom-right corner -->
  <button class="backpack-btn" @click="showBag = true">背包</button>

  <!-- Backpack drawer -->
  <div v-if="showBag" class="backpack-drawer">
    <div class="header-container">
      <h2>贴纸背包</h2>
      <el-button type="danger" @click="showBag = false" :icon="Close" circle />
<!--      <button class="close-btn" >关闭</button>-->
    </div>
    <div v-for="sticker in stickers" :key="sticker.stkId">
      <div v-if="!sticker.show" class="sticker-in-bag">
        <img :src="require('@/assets/hex.png')" class="sticker-small" />
        <el-button type="success" :icon="Check" @click="showStickerFromBag(sticker)" circle />
      </div>
    </div>
  </div>

  <!-- Shovel icon following the mouse -->
  <div v-if="isShovelMode" class="shovel-follow" :style="{ left: `${shovelPosition[0]}px`, top: `${shovelPosition[1]}px` }">
    <img :src="require('@/assets/shovel.png')" alt="Shovel" style="width: 40px; transform: translate(-50%, -50%)"/>
  </div>

  <!-- Tooltip for shovel mode -->
  <div v-if="isShovelMode" class="shovel-tooltip">
    请铲除不想展示的贴纸
  </div>
</template>

<style scoped>
.sticker-container {
  position: absolute;
  z-index: 0;
  width: 100%;
  height: 100%;
}

.sticker {
  position: absolute;
  width: 50px;
  height: 50px;
  cursor: pointer;
  user-select: none; /* 禁止用户选择图片 */
}

.backpack-btn {
  position: fixed;
  bottom: 20px;
  right: 20px;
  width: 60px;
  height: 60px;
  background-color: #f0a500;
  color: white;
  border: none;
  border-radius: 50%;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  z-index: 200;
}

.shovel-btn {
  position: fixed;
  bottom: 90px;
  right: 20px;
  width: 60px;
  height: 60px;
  background-color: #f0a500;
  color: white;
  border: none;
  border-radius: 50%;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  z-index: 200;
}

.shovel-follow {
  position: fixed;
  pointer-events: none;
  z-index: 300;
}

.shovel-tooltip {
  position: fixed;
  top: 10px;
  left: 50%;
  transform: translateX(-50%);
  background-color: rgba(0, 0, 0, 0.7);
  color: white;
  padding: 10px;
  border-radius: 5px;
  z-index: 300;
}

.shovel-btn img{
  width: 40px;
}

.backpack-drawer {
  position: fixed;
  bottom: 0;
  right: 0;
  width: 250px;
  height: 50%;
  background-color: #fff;
  box-shadow: -2px -2px 10px rgba(0, 0, 0, 0.3);
  z-index: 201;
  display: flex;
  flex-direction: column;
  align-items: center;
  overflow-y: auto;
}

.sticker-in-bag {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.sticker-small {
  width: 30px;
  height: 30px;
  margin-right: 10px;
}

.header-container {
  display: flex;
  align-items: center;
  /* 垂直居中对齐 */
}

.header-container h2{
  margin-right: 25px;
  flex: 1;
}

.close-btn {
  margin-left: 10px;
  background-color: red;
  color: white;
  border: none;
  cursor: pointer;
  height: 25px;
  border-radius: 10px;
}
</style>