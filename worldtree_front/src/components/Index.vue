<script setup>
import StickerManager from './StickerManager.vue';
import { markRaw, nextTick, ref } from 'vue';
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


</script>

<template>
  <div class="sticker-manager-container">
    <StickerManager />
  </div>
  <InScreenWindow ref="window" v-if="showWindow" @close="showWindow = false" class="at-top">
    <component :is="windowContent" />
  </InScreenWindow>
  <div class='button-container'>
    <div class="hexagon-container">
      <Navigation :icon="icon" :description="description" @click="toggleView" class="map" />
      <Navigation :icon="['fas', 'envelopes-bulk']" description="邮件"
                  @click="showWindowAndReset(markRaw(Mail), '邮件')" class="mail" />
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
.sticker-manager-container {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 2000;
  /* 确保 StickerManager 在最上层 */
}

.button-container {
  position: absolute;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 4%;
  right: 1%;
  z-index: 1000;
}

.hexagon-container {
  position: relative;
  width: 200px; /* Adjust the width as needed */
  top: 0;
  height: 340px;
  transform: translateY(50%);
  background-color: rgba(5, 49, 66, 0.28);
  border-radius: 20px;
  padding: 20px 0 20px 20px;
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
  z-index: 1000;
}
</style>