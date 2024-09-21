<script setup>
import { ref } from 'vue';
import axios from 'axios';
import {universalPost} from "@/services/universalService";
import store from "@/services/storeService";

const answer = ref('');
const gridStatus = ref(Array(9).fill(false));
const isModalVisible = ref(false);
let lastRequestTime = 0;

const conditions = [
  '',//'2', // '带有数字2',
  '',//'<= 11', // '长度不超过11',
  '',//'Abc', //以大写字母开头',
  '',//'- zxcvbnm', //没有键盘上第三排字母(zxcvbnm)',
  '',//'aabbcc', //相同的字符必须相邻',
  '',//'<= 2 * [aeiou]', //至多2种元音字母(aeiou)',
  '',//'ASCII [1000,1100]', //ascii码值之和在区间[1000,1100]之中',
  '',//'ar, bs, ct', //相邻字符ascii码值作差出现±17(其中之一)',
  '',//'Yy', //带有字母y'
];

const submitAnswer = async () => {
  const currentTime = Date.now();
  lastRequestTime = currentTime;

  try {
    const response = await universalPost('/api/subtask/cake/submit', { answer: answer.value });
    if (response.data.code === 0 && lastRequestTime === currentTime) {
      gridStatus.value = response.data.data.split('').map(char => char === '1');
      if (gridStatus.value.every(status => status)) {
        isModalVisible.value = true;
      }
    }
  } catch (error) {
    console.error(error);
  }
};

const closeModal = () => {
  isModalVisible.value = false;
};
</script>

<template>
  <div class="cake-container">
    <div class="input-container">
      <input v-model="answer" placeholder="输入咒语点亮蜡烛" />
      <button @click="submitAnswer">Check</button>
    </div>
    <div class="grid">
      <div v-for="(condition, index) in conditions" :key="index" :class="['grid-item', { 'lit': gridStatus[index] }]">
        {{ condition }}
      </div>
    </div>
    <div v-if="isModalVisible" class="modal">
      <div class="modal-content">
        <span class="close" @click="closeModal">&times;</span>
        <p>恭喜点亮所有蜡烛！</p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.cake-container {
  background-color: #f8f8f8;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
}

.input-container {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

input {
  width: 240px;
  padding: 10px;
  font-size: 16px;
}

button {
  margin-left: 10px;
  padding: 10px 20px;
  font-size: 16px;
}

.grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);

  background-color: #eee;
  background-image: url('@/assets/cake_top.png');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
}

.grid-item {
  width: 100px;
  height: 100px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #ddd;
  border: 1px solid #ccc;
  font-size: 1em;
  text-align: center;
  padding: 10px;  
}

.grid-item.lit {
  background: radial-gradient(circle, #ff0 0%, rgba(255, 224, 0, 0.5) 100%);
}

.modal {
  display: flex;
  justify-content: center;
  align-items: center;
  position: fixed;
  z-index: 1;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
}

.modal-content {
  background-color: #fff;
  padding: 20px;
  border: 1px solid #888;
  width: 300px;
  text-align: center;
}

.close {
  color: #aaa;
  float: right;
  font-size: 28px;
  font-weight: bold;
  cursor: pointer;
}

.close:hover,
.close:focus {
  color: black;
  text-decoration: none;
  cursor: pointer;
}
</style>
