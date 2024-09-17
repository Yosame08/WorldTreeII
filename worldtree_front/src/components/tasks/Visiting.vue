<template>
  <div class="visiting-container">
    <MapComponent></MapComponent>
    <div class="images-container">
      <img v-for="(image, index) in images" :key="index" :src="image.src" @click="showImage(index)" @contextmenu.prevent="saveImage(image.src)" />
    </div>
    <div v-if="selectedImage !== null" class="image-modal" @click="selectedImage = null">
      <img :src="images[selectedImage].src" />
    </div>
    <div v-if="showMarkerDialog" class="marker-dialog">
      <el-form :model="markerForm">
        <el-form-item label="是否在室内">
          <el-switch v-model="markerForm.indoor"></el-switch>
        </el-form-item>
        <el-form-item v-if="markerForm.indoor" label="楼层">
          <el-input-number v-model="markerForm.floor" :min="1" :max="10"></el-input-number>
        </el-form-item>
        <el-button @click="confirmMarker">确定</el-button>
      </el-form>
    </div>
    <button class="update-button" @click="updateAnswers">更新答案</button>

  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import MapComponent from '@/components/MapComponent.vue';
import { ElForm, ElFormItem, ElSwitch, ElInputNumber, ElButton } from 'element-plus';

const images = ref([
  { src: require('@/assets/visiting/level1.jpg') },
  { src: require('@/assets/visiting/level2.jpg') },
  { src: require('@/assets/visiting/level3.jpg') },
  { src: require('@/assets/visiting/level3.jpg') },
  { src: require('@/assets/visiting/level3.jpg') },
]);
const selectedImage = ref(null);
const showMarkerDialog = ref(false);
const markerForm = ref({ indoor: false, floor: 1 });
const markers = ref([]);

const showImage = (index) => {
  selectedImage.value = index;
};

const saveImage = (src) => {
  const link = document.createElement('a');
  link.href = src;
  link.download = src.split('/').pop();
  link.click();
};

const handleMarkerSet = (position) => {
  markerForm.value.position = position;
  showMarkerDialog.value = true;
};

const confirmMarker = () => {
  markers.value.push({ ...markerForm.value });
  showMarkerDialog.value = false;
};

const updateAnswers = async () => {
  const response = await fetch('/visiting/update', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      position: markers.value.map(marker => marker.position),
      indoor: markers.value.map(marker => marker.indoor),
      floor: markers.value.map(marker => marker.indoor ? marker.floor : 0),
    }),
  });
  const result = await response.json();
  if (result.code === 0) {
    alert('操作成功');
  } else {
    alert('操作失败');
  }
};

</script>

<style>
.visiting-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
}

.images-container {
  display: flex;
  flex-direction: column;
  position: absolute;
  right: 0;
  top: 0;
}

.images-container img {
  width: 100px;
  height: 100px;
  margin: 10px;
  cursor: pointer;
}

.image-modal {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: rgba(0, 0, 0, 0.8);
  padding: 20px;
  z-index: 1000;
}

.image-modal img {
  max-width: 90vw;
  max-height: 90vh;
}

.marker-dialog {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: white;
  padding: 20px;
  z-index: 1000;
}

.update-button {
  position: absolute;
  bottom: 10px;
  right: 10px;
  padding: 10px 20px;
  background-color: #900;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.update-button:hover {
  background-color: #ffaf6d;
}

</style>