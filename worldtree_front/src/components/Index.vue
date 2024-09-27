<script setup>
import {markRaw, nextTick, onMounted, ref} from 'vue';
import Home from './Home.vue';
import MapComponent from './Map.vue';
import Navigation from "@/components/effects/Navigation.vue";
import InScreenWindow from "@/components/InScreenWindow.vue";
import Rank from "@/components/Rank.vue";
import 'element-plus/dist/index.css';
import Mail from "@/components/Mail.vue";

const icon = ref(['fas', 'map-location-dot']);
const description = ref('地图');

const toggleView = () => {
  icon.value = icon.value[1] === 'house-chimney-user' ? ['fas', 'map-location-dot'] : ['fas', 'house-chimney-user'];
  description.value = description.value === '主页' ? '地图' : '主页';
};

const showWindow = ref(false);
const windowRef = ref(null);
const windowContent = ref(null);
const windowTitle = ref('');

const showWindowAndReset = (component, title) => {
  windowTitle.value = title;
  windowContent.value = component;
  showWindow.value = true;
  nextTick(() => {
    if (windowRef.value) {
      windowRef.value.resetPosition();
    }
  });
};

onMounted(() => {
  showWindowAndReset(markRaw(Mail), '茶楼');
});

</script>

<template>
  <InScreenWindow ref="window" v-if="showWindow" @close="showWindow = false" :title="windowTitle" class="at-top" >
    <component :is="windowContent" />
  </InScreenWindow>
  <div class='button-container'>
    <div class="hexagon-container">
      <Navigation :icon="icon" :description="description" @click="toggleView" class="map" />
      <Navigation :icon="['fas', 'mug-saucer']" description="茶楼"
                  @click="showWindowAndReset(markRaw(Mail), '茶楼')" class="mail" />
      <Navigation :icon="['fas', 'ranking-star']" description="排名"
                  @click="showWindowAndReset(markRaw(Rank), '排行榜')" class="ranking" />
    </div>
  </div>
  <div style="display: flex;">
    <div v-if="description !== '主页'" style="display: flex; flex: 1;">
      <Home style="flex: 1;" />
    </div>
    <div v-else style="flex: 1;">
      <MapComponent style="flex: 1;" />
    </div>
  </div>
</template>

<style scoped>

.button-container {
  position: absolute;
  display: flex;
  justify-content: center;
  align-items: center;
  bottom: 1%;
  //transform: translateY(-50%);
  right: 1%;
  z-index: 1001;
}

.hexagon-container {
  position: relative;
  width: 200px; /* Adjust the width as needed */
  top: 0;
  height: 340px;
  background-color: rgba(5, 49, 66, 0.28);
  border-radius: 20px;
  padding: 20px 0 20px 20px;
  user-select: none; /* 禁止用户选择图片 */
  -webkit-user-drag: none; /* 禁用图片拖动 */
}

.map {
  position: absolute;
}

.mail {
  position: absolute;
  transform: translateY(160%);
}

.ranking {
  position: absolute;
  transform: translate(55%, 80%);
}

.at-top {
  z-index: 1003;
}
</style>