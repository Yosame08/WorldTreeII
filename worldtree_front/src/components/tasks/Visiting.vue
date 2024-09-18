<template>
  <div class="visiting-container">
    <!-- This is the Map -->
    <div class="map-container" @mousedown="startDrag" @mousemove="onDrag" @mouseup="endDrag" @mouseleave="endDrag"
         @wheel="onWheel" @touchstart="startTouch" @touchmove="onTouchMove" @touchend="endTouch" @click="handleMapClick">
      <img :src="require('@/assets/fudan_map.png')" :style="mapStyle" ref="mapImage" />

      <!-- Zoom controls -->
      <div class="zoom-controls">
        <button @click="zoomIn">+</button>
        <button @click="zoomOut">-</button>
      </div>

      <!-- Markers -->
      <div v-for="(marker, index) in markers" :key="index" :style="getMarkerStyle(marker.position)" class="marker">
        <font-awesome-icon :icon="['fas', 'location-dot']" :style="{ color: getMarkerColor(index) }" @mouseover="showMarkerInfo(marker)" @mouseleave="hideMarkerInfo" />
        <div v-if="hoveredMarker === marker" class="marker-info">
          <p>{{ marker.indoor ? `Indoor, Floor: ${marker.floor}` : 'Outdoor' }}</p>
        </div>
      </div>
    </div>
    <!-- This is the container of the images -->
    <div class="images-container">
      <img v-for="(image, index) in images" :key="index" :src="image.src" @click="showImage(index)" @contextmenu.prevent="saveImage(image.src)" />
    </div>
    <div v-if="selectedImage !== null" class="image-modal" @click.self="closeImageModal">
      <img :src="images[selectedImage].src" />
      <div class="image-modal-buttons">
        <button @click="closeImageModal">关闭显示</button>
        <button @click="saveImage(images[selectedImage].src)">下载图片</button>
        <button @click="startSettingPosition">设置位置</button>
      </div>
    </div>
    <!-- This is the dialog for setting the marker -->
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
import { ref, onMounted, computed } from 'vue';
import { ElForm, ElFormItem, ElSwitch, ElInputNumber, ElButton } from 'element-plus';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import {universalGet} from "@/services/universalService";
import {updateVisiting} from "@/services/taskService";

const images = ref([
  { src: require('@/assets/visiting/level1.jpg') },
  { src: require('@/assets/visiting/level2.jpg') },
  { src: require('@/assets/visiting/level3.jpg') },
  { src: require('@/assets/visiting/level3.jpg') },
  { src: require('@/assets/visiting/level3.jpg') },
]);
const selectedImage = ref(null);
const showMarkerDialog = ref(false);
const markerForm = ref({ indoor: false, floor: 1, position: null });
const markers = ref([]);
const settingPosition = ref(false);
const hoveredMarker = ref(null);

const showImage = (index) => {
  selectedImage.value = index;
};

const closeImageModal = () => {
  selectedImage.value = null;
};

const saveImage = (src) => {
  const link = document.createElement('a');
  link.href = src;
  link.download = src.split('/').pop();
  link.click();
};

const startSettingPosition = () => {
  settingPosition.value = true;
  selectedImage.value = null;
};

const handleMarkerSet = (position) => {
  if (settingPosition.value) {
    markerForm.value.position = position;
    showMarkerDialog.value = true;
    settingPosition.value = false;
  }
};

const confirmMarker = () => {
  markers.value.push({ ...markerForm.value });
  showMarkerDialog.value = false;
};

const handleMarkerClick = (marker) => {
  markerForm.value = { ...marker };
  showMarkerDialog.value = true;
};

const updateAnswers = async () => {
  const response = await updateVisiting({
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      position: markers.value.map(marker => marker.position),
      indoor: markers.value.map(marker => marker.indoor),
      floor: markers.value.map(marker => marker.indoor ? marker.floor : 0),
    }),
  });
  const result = response.data.data;
  if (result.code === 0) {
    alert('操作成功');
    await fetchCurrentAnswers();
  } else {
    alert('操作失败');
  }
};

const fetchCurrentAnswers = async () => {
  let result = await universalGet('/api/subtask/visiting/get_info');
  result = result.data;
  if (result.code === 0) {
    markers.value = result.data.position.map((pos, index) => ({
      position: pos,
      indoor: result.data.indoor[index],
      floor: result.data.floor[index],
    }));
  }
  console.log(markers.value);
};

onMounted(fetchCurrentAnswers);

/* This is the script for the Map */
const scale = ref(1);
const minScale = 1;
const maxScale = 5;
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
  if (dragging.value) return;
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

const handleMapClick = (event) => {
  if (settingPosition.value) {
    const rect = mapImage.value.getBoundingClientRect();
    const x = (event.clientX - rect.left) / rect.width;
    const y = (event.clientY - rect.top) / rect.height;
    const position = calculateLatLng(x, y);
    handleMarkerSet(position);
  }
};

const calculateLatLng = (x, y) => {
  const latTop = 31.309626 + (31.309587 - 31.309626) * x;
  const latBottom = 31.294858 + (31.295028 - 31.294858) * x;
  const lngLeft = 121.498952 + (121.498791 - 121.498952) * y;
  const lngRight = 121.519335 + (121.519173 - 121.519335) * y;
  const lat = latTop + (latBottom - latTop) * y;
  const lng = lngLeft + (lngRight - lngLeft) * x;
  return { lat, lng };
};

const getMarkerStyle = (position) => {
  const [ lng, lat ] = position;
  const rect = mapImage.value.getBoundingClientRect();
  console.log(lng, lat);

  // 四个角的经纬度
  const topLeft = [121.498952, 31.309626];
  const topRight = [121.519335, 31.309587];
  const bottomLeft = [121.498791, 31.294858];
  const bottomRight = [121.519173, 31.295028];

  const height_ratio = (lat - bottomLeft[1]) / (topLeft[1] - bottomLeft[1]);
  const lng_interval = [
    bottomLeft[0] + (topLeft[0] - bottomLeft[0]) * height_ratio,
    bottomRight[0] + (topRight[0] - bottomRight[0]) * height_ratio
  ];
  const width_ratio = (lng - lng_interval[0]) / (lng_interval[1] - lng_interval[0]);
  const x = width_ratio * rect.width + rect.left;
  const y = height_ratio * rect.height + rect;

  return {
    position: 'absolute',
    left: `${x}px`,
    bottom: `${y}px`,
    transform: 'translateY(-100%)', // Align bottom of the icon with the position
  };
};

const getMarkerColor = (index) => {
  const colors = ['red', 'blue', 'green', 'yellow', 'purple'];
  return colors[index % colors.length];
};

const showMarkerInfo = (marker) => {
  hoveredMarker.value = marker;
};

const hideMarkerInfo = () => {
  hoveredMarker.value = null;
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
  z-index: 1000;
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
  height: calc(100vh - 39px);
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
  top: -20px;
  left: 50%;
  transform: translateX(-50%);
  background-color: white;
  padding: 5px;
  border: 1px solid #ccc;
  border-radius: 3px;
  white-space: nowrap;
}
</style>