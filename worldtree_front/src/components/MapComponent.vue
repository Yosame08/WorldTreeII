<script setup>
import {computed, ref} from "vue";

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
</script>

<template>
  <div class="map-container" @mousedown="startDrag" @mousemove="onDrag" @mouseup="endDrag" @mouseleave="endDrag"
       @wheel="onWheel" @touchstart="startTouch" @touchmove="onTouchMove" @touchend="endTouch">
    <img :src="require('@/assets/fudan_map.png')" :style="mapStyle" ref="mapImage" />

    <!-- Zoom controls -->
    <div class="zoom-controls">
      <button @click="zoomIn">+</button>
      <button @click="zoomOut">-</button>
    </div>
  </div>
</template>

<style scoped>
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
</style>