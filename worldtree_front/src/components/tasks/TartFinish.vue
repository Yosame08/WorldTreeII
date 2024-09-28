<script setup>
import {onMounted, ref} from 'vue';
import { universalGet } from '@/services/universalService';
import store from "@/services/storeService";

const connected = ref(false);

onMounted(async () => {
  window.location.href = '/static/HL.z01.f7';
  try {
    const response = await universalGet('/api/subtask/tarts/w0w_f1n1sh3d');
    if (response.data.code === 0){
      connected.value = true;
    }
    else {
      store.commit('setErrorMsg', response.data.msg);
    }
  } catch (error) {
    store.commit('setErrorMsg', error);
  }
});
</script>

<template>
  <div class="centered-content" v-if="connected">
    <h1>恭喜完成任务【蛋挞】</h1>
    <p>获得贴纸，请及时查看</p>
  </div>
</template>

<style scoped>
.centered-content {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100vh; /* 使内容垂直居中 */
  text-align: center;
}

h1 {
  font-size: 2em; /* 大字 */
}

p {
  font-size: 1em; /* 小字 */
}
</style>