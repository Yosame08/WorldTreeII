<script setup>
import { ref, onMounted } from 'vue';
import {universalGet, universalPost} from "@/services/universalService";
import store from "@/services/storeService";

const poemText = ref('');
const inputText = ref('');
const times = ref(0);
const nextLevel = ref(false);
const banOther = ref(false);
const loading = ref(false);
const title = ref('');
const question = ref('');

const fetchPoem = async () => {
  try {
    loading.value = true;
    console.log(times);
    const response = await universalPost('/api/subtask/poem/check', {times: times.value});
    if (response.data.code === 0) {
      poemText.value = response.data.data.result + '\n' + poemText.value;
      if (response.data.data.ok){
        times.value += 1;
      }
      else {
        nextLevel.value = true;
        banOther.value = true;
        alert('这首诗已经全部显示完毕');
      }
      title.value = response.data.data.title;
      question.value = response.data.data.question;
      loading.value = false;
    }
    else {
      store.commit('setErrorMsg', response.data.message);
    }
  } catch (error) {
    console.error('Error fetching poem:', error);
  }
};

const submitAnswer = async () => {
  try {
    loading.value = true;
    const response = await universalPost('/api/subtask/poem/submit', {answer: inputText.value});
    if (response.data.code === 0) {
      if (response.data.data.ok) {
        nextLevel.value = true;
        alert('回答正确，可以继续下一首');
      }
      else {
        nextLevel.value = true;
        alert('回答错误');
      }
      loading.value = false;
    }
    else {
      store.commit('setErrorMsg', response.data.message);
    }
  } catch (error) {
    console.error('Error submitting answer:', error);
  }
};

const getNext = async () => {
  try {
    loading.value = true;
    const response = await universalGet('/api/subtask/poem/next');
    if (response.data.code === 0) {
      times.value = 0;
      loading.value = false;
      banOther.value = false;
      nextLevel.value = false;
    }
    else {
      store.commit('setErrorMsg', response.data.message);
    }
    poemText.value = '';
    await fetchPoem();
  } catch (error) {
    console.error('Error fetching next poem:', error);
  }
};

onMounted(() => {
  fetchPoem();
});
</script>

<template>
  <div class="poem-container">
    <h4 style="margin: 0;">如果你已经找到答案了，就回答下面的问题吧</h4>
    <p style="margin: 0">全文显示完毕以及回答错误都会导致本题失败</p>
    <br>
    <h2>{{title}}</h2>
    <div class="quote">
      <p v-html="poemText.replace(/\n/g, '<br>')"></p>
    </div>
    <el-button @click="fetchPoem" :loading="loading" :disabled="banOther" >Next</el-button>
    <div class="task">
      <p>{{question}}</p>
      <p>输入你的回答：</p>
      <el-input v-model="inputText" type="text" />
      <div>
        <el-button @click="submitAnswer" :loading="loading" :disabled="nextLevel" style="margin: 15px; width: 80px" >提交</el-button>
        <el-button @click="getNext" :disabled="loading || !nextLevel" style="width: 80px; margin-right: 10px;" >下一首</el-button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.poem-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background-color: rgba(0, 93, 255, 0.2);
  padding: 20px;
}

.quote {
  background-color: rgba(255, 255, 255, 0.8);
  padding: 20px;
  border-radius: 10px;
  margin-bottom: 20px;
  text-align: center;
}

.task {
  display: flex;
  flex-direction: column;
  align-items: center;
}

input {
  margin-bottom: 10px;
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #ccc;
}

button {
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  background-color: #007bff;
  color: white;
  cursor: pointer;
  transition: background-color 0.3s;
}

button:hover {
  background-color: #0056b3;
}
</style>