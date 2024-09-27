<template>
  <div class="visiting-container">
    <!-- 提示窗口 -->
    <div v-if="nowSetting !== null" class="position-hint">
      点击选择图片的位置
    </div>
    <!-- This is the Map -->
    <div class="map-container" @mousedown="startDrag" @mousemove="onDrag" @mouseup="endDrag" @mouseleave="endDrag"
         @wheel="onWheel" @touchstart="startTouch" @touchmove="onTouchMove" @touchend="endTouch" @click="handleMapClick">
      <img :src="require('@/assets/fudan_map.png')" :style="mapStyle" ref="mapImage" />

      <!-- Zoom controls -->
      <div class="zoom-controls">
        <button @click="zoomIn">+</button>
        <button @click="zoomOut">-</button>
      </div>

      <!-- markers.value -->
      <div v-for="(marker, index) in markers" :key="index" :style="getMarkerStyle(marker.position)" class="marker">
        <font-awesome-icon :icon="['fas', 'location-dot']" :style="{ color: getMarkerColor(index), fontSize: 32 }" @click="handleMarkerClick(index)" />
        <div v-if="clickedMarker === index" class="marker-info">
          <p>{{ `图${index+1}` }}</p>
          <p>{{ `${marker.position[0].toFixed(5)}, ${marker.position[1].toFixed(5)}` }}</p>
          <p>{{ marker.indoor ? `室内${marker.floor}层` : '室外' }}</p>
          <el-button @click="startSettingPosition(index)">修改位置</el-button>
        </div>
      </div>
    </div>
    <!-- This is the container of the images -->
    <div class="images-container">
      <img v-for="(image, index) in images" :key="index" :src="image.src" @click="showImage(index)"/>
    </div>
    <div v-if="selectedImage !== null" class="image-modal" @click.self="closeImageModal">
      <img :src="images[selectedImage].src" />
      <div class="image-modal-buttons">
        <button @click="closeImageModal">关闭显示</button>
        <button @click="saveImage(images[selectedImage].src)">下载图片</button>
        <button @click="startSettingPosition(selectedImage)">设置位置</button>
      </div>
    </div>
    <!-- This is the dialog for setting the marker -->
    <div v-if="showMarkerDialog" class="marker-dialog">
      <el-form :model="markerForm">
        <p>{{ `图${nowSetting+1}` }}</p>
        <p>{{ newPosition[0].toFixed(5) }}, {{ newPosition[1].toFixed(5) }}</p>
        <el-form-item label="在建筑物内">
          <el-switch v-model="markerForm.indoor"></el-switch>
        </el-form-item>
        <el-form-item v-if="markerForm.indoor" label="楼层">
          <el-input-number v-model="markerForm.floor" :min="1" :max="30" :style="{width:'100px'}" ></el-input-number>
        </el-form-item>
        <el-button @click="cancelMarker">复原</el-button>
        <el-button @click="confirmMarker">确定</el-button>
      </el-form>
    </div>
    <button class="update-button" @click="updateAnswers">更新答案</button>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { ElForm, ElFormItem, ElSwitch, ElInputNumber, ElButton } from 'element-plus';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import {universalGet, universalPost} from "@/services/universalService";
import store from "@/services/storeService";

const images = ref([
  { src: require('@/assets/visiting/level4.jpg') },
  { src: require('@/assets/visiting/level5.jpg') },
  { src: require('@/assets/visiting/level6.jpg') },
]);

const clickedMarker = ref(null);
const selectedImage = ref(null);
const nowSetting = ref(null);
const showMarkerDialog = ref(false);
const markerForm = ref({ indoor: false, floor: 1 });
const oldPosition = ref([0, 0]);
const newPosition = ref([0, 0]);
const markers = ref([]);

const showImage = (index) => {
  if (nowSetting.value !== null) return;
  selectedImage.value = index;
  clickedMarker.value = null;
};

// 3个按钮的功能
const closeImageModal = () => {
  selectedImage.value = null;
};

const saveImage = (src) => {
  const link = document.createElement('a');
  link.href = src;
  link.download = src.split('/').pop();
  link.click();
};

const startSettingPosition = (index) => {
  selectedImage.value = clickedMarker.value = null;
  showMarkerDialog.value = false;
  oldPosition.value = markers.value[index].position;
  // 添加短暂的延迟，避免立即触发 handleMapClick
  setTimeout(() => {
    nowSetting.value = index;
  }, 50); // 50 毫秒的延迟
};

const handleMarkerClick = (index) => {
  if (selectedImage.value || nowSetting.value !== null) return;
  clickedMarker.value = index;
};

const handleMapClick = (event) => {
  if (nowSetting.value !== null) {
    // 检查点击的目标是否是加减按钮
    if (event.target.closest('.zoom-controls')) {
      return;
    }
    const rect = mapImage.value.getBoundingClientRect();
    const x = (event.clientX - rect.left) / rect.width;
    const y = (event.clientY - rect.top) / rect.height;
    const position = calculateLatLng(x, y);
    handleMarkerSet(position);
  }
};

const handleMarkerSet = (position) => {
  if (nowSetting.value !== null) {
    markers.value[nowSetting.value].position = position;
    newPosition.value = position;
    showMarkerDialog.value = true;
  }
};

const cancelMarker = () => {
  if (nowSetting.value !== null) {
    markers.value[nowSetting.value].position = oldPosition.value;
    showMarkerDialog.value = false;
    nowSetting.value = null;
  }
};

const confirmMarker = () => {
  if (nowSetting.value !== null) {
    markers.value[nowSetting.value].indoor = markerForm.value.indoor;
    markers.value[nowSetting.value].floor = markerForm.value.floor;
    showMarkerDialog.value = false;
    nowSetting.value = null;
  }
};

// 四个角的经纬度
const topLeft = [121.498840, 31.309501];
const topRight = [121.519321, 31.309501];
const bottomLeft = [121.498840, 31.294785];
const bottomRight = [121.519321, 31.294785];

const calculateLatLng = (x, y) => {
  const lng = topLeft[0] + (topRight[0] - topLeft[0]) * x;
  const lat = topRight[1] - (topRight[1] - bottomRight[1]) * y;
  return [ lng, lat ];
};

const getMarkerStyle = (position) => {
  const [ lng, lat ] = position;
  const rect = mapImage.value.getBoundingClientRect();

  const height_ratio = (lat - bottomLeft[1]) / (topLeft[1] - bottomLeft[1]);
  const lng_interval = [
    bottomLeft[0] + (topLeft[0] - bottomLeft[0]) * height_ratio,
    bottomRight[0] + (topRight[0] - bottomRight[0]) * height_ratio
  ];
  const width_ratio = (lng - lng_interval[0]) / (lng_interval[1] - lng_interval[0]);
  const x = width_ratio * rect.width + rect.left;
  const y = (1 - height_ratio) * rect.height + rect.top;

  return {
    position: 'absolute',
    left: `${x}px`,
    top: `${y - 43}px`,
    transform: 'translate(-50%, -100%)', // Align bottom of the icon with the position
  };
};

const getMarkerColor = (index) => {
  const colors = ['red', 'blue', 'green', 'yellow', 'purple'];
  return colors[index % colors.length];
};

const updateAnswers = async () => {
  if (nowSetting.value !== null) return;
  try{
    const response = await universalPost(`/api/subtask/visiting2/update`, {
      position: [markers.value[0].position, markers.value[1].position, markers.value[2].position],
      indoor: [markers.value[0].indoor, markers.value[1].indoor, markers.value[2].indoor],
      floor: [markers.value[0].floor, markers.value[1].floor, markers.value[2].floor]
    });
    const result = response.data;
    if (result.code === 0) {
      alert('操作成功');
      window.location.reload();
    } else {
      alert('操作失败');
    }
  }
  catch (e) {
    store.commit('setErrorMsg', e);
  }
};

const fetchCurrentAnswers = async () => {
  let result = await universalGet('/api/subtask/visiting2/get_info');
  result = result.data;
  if (result.code === 0) {
    markers.value = result.data.position.map((pos, index) => ({
      position: pos,
      indoor: result.data.indoor[index],
      floor: result.data.floor[index],
    }));
  }
};

onMounted(fetchCurrentAnswers);

/* This is the script for the Map */
const scale = ref(1);
const minScale = 1;
const maxScale = 4;
const startX = ref(0);
const startY = ref(0);
const translateX = ref(0);
const translateY = ref(0);
const dragging = ref(false);
const touchStartDistance = ref(0);
const mapImage = ref(null);

const mapStyle = computed(() => ({
  transform: `scale(${scale.value}) translate(${translateX.value}px, ${translateY.value}px)`,
  transformOrigin: '0 0',
  width: '100%',
  cursor: dragging.value ? 'grabbing' : 'grab',
  userSelect: 'none',
}));

const zoom = (delta, event) => {
  const newScale = Math.min(Math.max(scale.value + delta, minScale), maxScale);
  const zoomCenter = getZoomCenter(event);
  translateX.value = (translateX.value - zoomCenter.x) * (newScale / scale.value) + zoomCenter.x;
  translateY.value = (translateY.value - zoomCenter.y) * (newScale / scale.value) + zoomCenter.y;
  scale.value = newScale;
  checkBounds();
  setTimeout(() => {
    updateMarkerPositions();
  }, 10); // 50 毫秒的延迟
};

const updateMarkerPositions = () => {
  markers.value = markers.value.map(marker => ({
    ...marker,
    style: getMarkerStyle(marker.position)
  }));
};

const zoomIn = (event) => {
  zoom(0.1, event);
};

const zoomOut = (event) => {
  zoom(-0.1, event);
};

const getZoomCenter = (event) => {
  const container = mapImage.value?.parentElement;
  if (event && container) {
    const rect = container.getBoundingClientRect();
    return {
      x: event.clientX - rect.left,
      y: event.clientY - rect.top,
    };
  } else if (container) {
    return {
      x: container.clientWidth / 2,
      y: container.clientHeight / 2,
    };
  }
  return { x: 0, y: 0 };
};

const onWheel = (event) => {
  event.preventDefault();
  if (event.deltaY < 0) {
    zoomIn(event);
  } else {
    zoomOut(event);
  }
};

const startDrag = (event) => {
  if (dragging.value || nowSetting.value !== null) return;
  event.preventDefault();
  const rect = mapImage.value.getBoundingClientRect();
  if (
      event.clientX >= rect.left &&
      event.clientX <= rect.right &&
      event.clientY >= rect.top &&
      event.clientY <= rect.bottom
  ) {
    dragging.value = true;
    startX.value = event.clientX - translateX.value;
    startY.value = event.clientY - translateY.value;
  }
};

const onDrag = (event) => {
  if (nowSetting.value !== null) return;
  if (dragging.value) {
    translateX.value = event.clientX - startX.value;
    translateY.value = event.clientY - startY.value;
    checkBounds();
  }
};

const endDrag = () => {
  dragging.value = false;
};

const startTouch = (event) => {
  if (event.touches.length === 2) {
    touchStartDistance.value = getTouchDistance(event.touches);
  }
};

const onTouchMove = (event) => {
  if (event.touches.length === 2) {
    const currentDistance = getTouchDistance(event.touches);
    if (currentDistance > touchStartDistance.value) {
      zoomIn();
    } else {
      zoomOut();
    }
    touchStartDistance.value = currentDistance;
  }
};

const endTouch = () => {
  touchStartDistance.value = 0;
};

const getTouchDistance = (touches) => {
  const [touch1, touch2] = touches;
  return Math.sqrt(
      Math.pow(touch2.clientX - touch1.clientX, 2) +
      Math.pow(touch2.clientY - touch1.clientY, 2)
  );
};

const checkBounds = () => {
  const container = mapImage.value?.parentElement;
  const img = mapImage.value;
  if (!container || !img) return;
  const minTranslateX = -(img.width * scale.value - container.clientWidth) / scale.value;
  const minTranslateY = -(img.height * scale.value - container.clientHeight) / scale.value;
  translateX.value = Math.min(Math.max(translateX.value, minTranslateX), 0);
  translateY.value = Math.min(Math.max(translateY.value, minTranslateY), 0);
};

</script>

<style scoped>
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
  z-index: 999;
}

.image-modal img {
  max-width: 90vw;
  max-height: 90vh;
}

.image-modal-buttons {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
}

.image-modal-buttons button {
  padding: 10px 20px;
  background-color: #900;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.image-modal-buttons button:hover {
  background-color: #ffaf6d;
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

/* This is the style for the Map */
.map-container {
  position: relative;
  overflow: hidden;
  width: 100%;
  height: calc(100vh - 43px);
  /* Adjust this value based on your NavBar height */
}

.map-container img {
  display: block;
  user-select: none;
}

.zoom-controls {
  position: absolute;
  bottom: 10px;
  left: 10px;
  display: flex;
  flex-direction: column;
}

.zoom-controls button {
  background-color: #fff;
  border: 1px solid #ccc;
  padding: 5px;
  margin: 2px;
  cursor: pointer;
}

.marker {
  position: absolute;
}

.marker-info {
  position: absolute;
  transform: translate(15%, -115%);
  background-color: white;
  border: 1px solid #ccc;
  border-radius: 3px;
  white-space: nowrap;
  font-size: 1em;
  z-index: 1200;
}

.position-hint {
  position: absolute;
  top: 10px;
  left: 50%;
  transform: translateX(-50%);
  background-color: rgba(255, 255, 255, 0.8);
  padding: 10px;
  border-radius: 5px;
  z-index: 1000;
}
</style>