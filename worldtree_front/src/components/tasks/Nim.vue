<template>
  <div v-if="begin">
    <el-row>
      <el-col :span="24">
        <p>选一堆瓜子来嗑吧！看谁嗑完最后一堆瓜子！</p>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24">
        <p>{{ operationMessage }}</p>
      </el-col>
    </el-row>
    <el-row>
      <el-col v-for="(pile, index) in piles" :key="index" :span="4">
        <NimPile :remaining="pile.remaining" :active="activePiles[index]" @clicked="lockOthers(index)"
                 @update-remaining="updateRemaining(index, $event)" :disabled="!canOperate"/>
      </el-col>
    </el-row>
    <el-row justify="center">
      <el-col :span="3">
        <el-button type="danger" @click="reset" :disabled="!canOperate">重置</el-button>
        <el-button type="success" @click="submit" :disabled="!canOperate" :loading="!canOperate">提交</el-button>
      </el-col>
    </el-row>
  </div>
  <div v-else>
    <h2>请选择难度</h2>
    <el-button type="success" @click="init(true)">Easy</el-button>
    <el-button type="danger" @click="init(false)">Impossible</el-button>
  </div>
</template>

<script setup>
import {ref} from 'vue';
import NimPile from "@/components/tasks/NimPile.vue";
import {useRoute} from "vue-router";
import axios from "axios";
import {universalPost} from "@/services/universalService";
import store from "@/services/storeService";

const piles = ref([]); // State holding piles info
const originalPiles = ref([]); // Original value for reset
const activePiles = ref([]);
const chosen = ref(null);
const begin = ref(false);
const canOperate = ref(false);
const token = ref(null);
const operationMessage = ref(''); // State holding operation message

const route = useRoute(); // Get the current route object
const queryParams = route.query; // 获取查询参数

async function init(easy) {
  try {
    let resp = await universalPost("/api/subtask/nim/init", {
      easy: easy,
      playerFirst: queryParams.playerFirst !== "false"
    });
    if (resp.data.code === 0){
      let data = resp.data.data;
      token.value = data.gameToken;
      piles.value = data.array.map(remaining => ({remaining}));
      begin.value = true;
      if (data.result.num > 0){
        operationMessage.value = '等待对手嗑瓜子...';
        setTimeout(() => {
          piles.value[data.result.pos].remaining -= data.result.num;
          originalPiles.value = JSON.parse(JSON.stringify(piles.value)); // Make deep copy
          activePiles.value = piles.value.map(() => true);
          operationMessage.value = `对手在第 ${data.result.pos + 1} 堆拿了 ${data.result.num} 个，等待${store.state.userInfo.username}嗑瓜子...`;
          setTimeout(() => {
            canOperate.value = true;
          }, 1200);
        }, 1200);
      }
      else {
        operationMessage.value = `等待${store.state.userInfo.username}嗑瓜子...`;
        originalPiles.value = JSON.parse(JSON.stringify(piles.value)); // Make deep copy
        activePiles.value = piles.value.map(() => true);
        canOperate.value = true;
      }
    }
    else{
      store.commit("setErrorMsg", resp.data.message);
    }
  }
  catch (error) {
    alert(error);
  }
}

async function submit() {
  if (chosen.value === null) {
    alert("请选择一堆瓜子");
    return;
  }
  try{
    canOperate.value = false;
    operationMessage.value = `${store.state.userInfo.username}在第 ${chosen.value + 1} 堆拿了 ${originalPiles.value[chosen.value].remaining - piles.value[chosen.value].remaining} 个瓜子，等待对手嗑瓜子...`;
    let resp = await axios.post("/api/subtask/nim/step", {
      gameToken: token.value,
      step: {
        pos: chosen.value,
        num: originalPiles.value[chosen.value].remaining - piles.value[chosen.value].remaining
      }
    });
    chosen.value = null;
    if (resp.data.code === 0) {
      if (resp.data.data.pass) {
        if (resp.data.data.step.num !== 0){
          setTimeout(() => {
            piles.value[resp.data.data.step.pos].remaining -= resp.data.data.step.num;
            operationMessage.value = `对手在第 ${resp.data.data.step.pos + 1} 堆拿了 ${resp.data.data.step.num} 个瓜子，获得了胜利！`;
            setTimeout(() => {
              alert(`${resp.data.data.winner}嗑完了最后一堆瓜子，获得了胜利！`);
            }, 500);
          }, 1200);
        }
        else {
          operationMessage.value = `${resp.data.data.winner}嗑完了最后一堆瓜子，获得了胜利！`;
          alert(`${resp.data.data.winner}嗑完了最后一堆瓜子，获得了胜利！`);
        }
      }
      else if (resp.data.data.step.num !== 0) {
        setTimeout(() => {
          piles.value[resp.data.data.step.pos].remaining -= resp.data.data.step.num;
          originalPiles.value = JSON.parse(JSON.stringify(piles.value)); // Make deep copy
          activePiles.value = piles.value.map(() => true);
          operationMessage.value = `对手在第 ${resp.data.data.step.pos + 1} 堆拿了 ${resp.data.data.step.num} 个瓜子，等待${store.state.userInfo.username}嗑瓜子...`;
          canOperate.value = true;
        }, 1200);
      }
      else {
        alert("发送的请求无效");
      }
    }
    else {
      store.commit("setErrorMsg", resp.data.message);
    }
  }
  catch (error) {
    alert(error);
  }
}

// Lock other piles when one is clicked
function lockOthers(clickedIndex) {
  activePiles.value = activePiles.value.map((_, index) => index === clickedIndex);
}

// Update the remaining value of a pile
function updateRemaining(index, newRemaining) {
  chosen.value = index;
  piles.value[index].remaining = newRemaining;
}

// Reset the piles and active state
function reset() {
  chosen.value = null;
  piles.value = JSON.parse(JSON.stringify(originalPiles.value)); // Reset piles to original
  activePiles.value = piles.value.map(() => true); // Reset all piles to active
}
</script>