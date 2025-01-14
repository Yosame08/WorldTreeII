<template>
  <div class="page-background">
    <div class="display-header">
      <div class="fractions">
        <span class="red">1</span>
        <span class="green">1/2</span>
        <span class="blue">1/4</span>
      </div>
      <div class="squares">
        <div class="red-square"></div>
        <div class="green-square"></div>
      </div>
    </div>
    <div class="skittles-container">
      <div class="display">
        <SevenSegmentDisplay :timerValue="timerValue" />
      </div>
      <div class="keyboard">
        <button v-for="n in 9" :key="n-1" @click="handleButtonClick(n-1)" class="key">
          <font-awesome-icon :icon="['fas', 'headphones']" />
        </button>
      </div>
    </div>
    <button v-if="isPlaying" @click="stopPlayback" class="stop-button">
      <font-awesome-icon :icon="['fas', 'ban']" />
    </button>
  </div>
</template>

<script setup>
import {onMounted, onUnmounted, ref} from 'vue';
import SevenSegmentDisplay from '@/components/effects/SevenSegmentDisplay.vue';
import {universalPost} from "@/services/universalService";
import store from "@/services/storeService";

const timerValue = ref(0);
const isPlaying = ref(false);
let timerInterval = null;
let audioTimeouts = [];
const audioFiles = {};
let playInterval = 400;

const preloadAudio = () => {
  const audioNumbers = [0, 1, 2, 3, 5, 6, 7, 8];
  audioNumbers.forEach(num => {
    audioFiles[num] = new Audio(require(`@/assets/skittles/${num}.wav`));
  });
};

const startTimer = () => {
  const startTime = Date.now();
  timerInterval = setInterval(() => {
    timerValue.value = Date.now() - startTime;
  }, 10);
};

const stopTimer = () => {
  if (timerInterval) {
    clearInterval(timerInterval);
    timerValue.value = 0;
  }
};

let stopTimeout;

const playSequence = (sequence) => {
  stopTimer();
  isPlaying.value = true;
  startTimer();

  sequence.forEach((num, index) => {
    const timeout = setTimeout(() => {
      if (num !== 4) {
        const audio = audioFiles[num];
        if (audio) {
          audio.play();
        }
      }
    }, index * playInterval);
    audioTimeouts.push(timeout);
  });

  stopTimeout = setTimeout(() => {
    stopPlayback();
  }, sequence.length * playInterval);
};

const stopPlayback = () => {
  isPlaying.value = false;
  stopTimer();
  audioTimeouts.forEach(clearTimeout);
  audioTimeouts = [];
  clearTimeout(stopTimeout);
  // 移除 pressed 类
  document.querySelectorAll('.key').forEach(button => button.classList.remove('pressed'));
};

const handleButtonClick = async (num) => {
  try {
    // 停止当前的音频播放序列
    stopPlayback();

    // 移除所有按钮的 pressed 类
    document.querySelectorAll('.key').forEach(button => button.classList.remove('pressed'));

    // 添加 pressed 类到用户点击的按钮
    const button = document.querySelector(`.key:nth-child(${num + 1})`);
    if (button) {
      button.classList.add('pressed');
    }

    const response = await universalPost('/api/subtask/skittles/init', { start: num });
    if (response.data.code === 0) {
      playSequence(response.data.data);
    }
    else {
      store.commit('setErrorMsg', response.data.msg);
    }
  } catch (error) {
  }
};

onMounted(() => {
  preloadAudio();
});

onUnmounted(() => {
  stopPlayback();
});
</script>

<style scoped>
.page-background {
  background-color: #2e2e2e;
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  height: calc(100vh - 43px);
  flex-direction: column; /* 添加这一行 */
}

.skittles-container {
  background-color: #1e1e1e;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
}

.display-header {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.fractions {
  display: flex;
  gap: 10px;
}

.fractions .red {
  color: red;
}

.fractions .green {
  color: green;
}

.fractions .blue {
  color: blue;
}

.squares {
  display: flex; /* 修改这一行 */
  gap: 5px;
  margin-left: 20px;
}

.red-square, .green-square {
  width: 20px;
  height: 20px;
}

.red-square {
  background-color: red;
}

.green-square {
  background-color: green;
}

.display {
  background-color: #000;
  padding: 20px;
  border-radius: 10px;
  box-shadow: inset 0 0 10px #000;
  margin-bottom: 20px;
}

.keyboard {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.key {
  background-color: #555;
  color: white;
  border: none;
  border-radius: 50%;
  padding: 20px;
  cursor: pointer;
  box-shadow: 0 4px #333;
  transition: background-color 0.3s, box-shadow 0.1s;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5em;
}

.key:active {
  background-color: #777;
  box-shadow: 0 2px #333;
  transform: translateY(2px);
}

.key.pressed {
  background-color: #777;
  box-shadow: 0 2px #333;
  transform: translateY(2px);
}

.stop-button {
  position: absolute;
  bottom: 10%;
  padding: 10px 20px;
  background-color: #900;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.stop-button:hover {
  background-color: #b00;
}
</style>