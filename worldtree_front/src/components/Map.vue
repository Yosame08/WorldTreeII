<template>
  <div class="map-container" @mousedown="startDrag" @mousemove="onDrag" @mouseup="endDrag" @mouseleave="endDrag" @wheel="onWheel" @touchstart="startTouch" @touchmove="onTouchMove" @touchend="endTouch">
    <img :src="require('@/assets/fudan_map.png')" :style="mapStyle" ref="mapImage" />
    <div class="zoom-controls">
      <button @click="zoomIn">+</button>
      <button @click="zoomOut">-</button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Map',
  data() {
    return {
      scale: 1,
      minScale: 1,
      maxScale: 5,
      startX: 0,
      startY: 0,
      translateX: 0,
      translateY: 0,
      dragging: false,
      touchStartDistance: 0,
    };
  },
  computed: {
    mapStyle() {
      return {
        transform: `scale(${this.scale}) translate(${this.translateX}px, ${this.translateY}px)`,
        transformOrigin: '0 0',
        width: '100%',
        cursor: this.dragging ? 'grabbing' : 'grab',
        userSelect: 'none',
      };
    },
  },
  methods: {
    zoomIn(event) {
      this.zoom(0.1, event);
    },
    zoomOut(event) {
      this.zoom(-0.1, event);
    },
    zoom(delta, event) {
      const newScale = Math.min(Math.max(this.scale + delta, this.minScale), this.maxScale);
      const zoomCenter = this.getZoomCenter(event);
      this.translateX = (this.translateX - zoomCenter.x) * (newScale / this.scale) + zoomCenter.x;
      this.translateY = (this.translateY - zoomCenter.y) * (newScale / this.scale) + zoomCenter.y;
      this.scale = newScale;
      this.checkBounds();
    },
    getZoomCenter(event) {
      const container = this.$el;
      if (event) {
        const rect = container.getBoundingClientRect();
        return {
          x: event.clientX - rect.left,
          y: event.clientY - rect.top,
        };
      } else {
        return {
          x: container.clientWidth / 2,
          y: container.clientHeight / 2,
        };
      }
    },
    onWheel(event) {
      event.preventDefault();
      if (event.deltaY < 0) {
        this.zoomIn(event);
      } else {
        this.zoomOut(event);
      }
    },
    startDrag(event) {
      if (this.dragging) return;
      event.preventDefault(); // Prevent default behavior
      const mapImage = this.$refs.mapImage;
      const rect = mapImage.getBoundingClientRect();
      if (event.clientX >= rect.left && event.clientX <= rect.right && event.clientY >= rect.top && event.clientY <= rect.bottom) {
        this.dragging = true;
        this.startX = event.clientX - this.translateX;
        this.startY = event.clientY - this.translateY;
      }
    },
    onDrag(event) {
      if (this.dragging) {
        this.translateX = event.clientX - this.startX;
        this.translateY = event.clientY - this.startY;
        this.checkBounds();
      }
    },
    endDrag() {
      if (this.dragging) {
        this.dragging = false;
      }
    },
    startTouch(event) {
      if (event.touches.length === 2) {
        this.touchStartDistance = this.getTouchDistance(event.touches);
      }
    },
    onTouchMove(event) {
      if (event.touches.length === 2) {
        const currentDistance = this.getTouchDistance(event.touches);
        if (currentDistance > this.touchStartDistance) {
          this.zoomIn();
        } else {
          this.zoomOut();
        }
        this.touchStartDistance = currentDistance;
      }
    },
    endTouch() {
      this.touchStartDistance = 0;
    },
    getTouchDistance(touches) {
      const [touch1, touch2] = touches;
      return Math.sqrt(
          Math.pow(touch2.clientX - touch1.clientX, 2) +
          Math.pow(touch2.clientY - touch1.clientY, 2)
      );
    },
    checkBounds() {
      const mapImage = this.$refs.mapImage;
      const container = this.$el;
      const minTranslateX = -(mapImage.width * this.scale - container.clientWidth) / this.scale;
      const minTranslateY = -(mapImage.height * this.scale - container.clientHeight) / this.scale;
      this.translateX = Math.min(Math.max(this.translateX, minTranslateX), 0);
      this.translateY = Math.min(Math.max(this.translateY, minTranslateY), 0);
      console.clear()
      console.log('scale:', this.scale)
      console.log('translateX:', this.translateX, 'translateY:', this.translateY)
      console.log('minTranslateX:', minTranslateX, 'minTranslateY:', minTranslateY)
      console.log('container.clientWidth:', container.clientWidth, 'container.clientHeight:', container.clientHeight)
      console.log('mapImage.width*this.scale:', mapImage.width * this.scale, 'mapImage.height*this.scale:', mapImage.height * this.scale)
    },
  },
};
</script>

<style>
.map-container {
  position: relative;
  overflow: hidden;
  width: 100%;
  height: calc(100vh - 39px); /* Adjust this value based on your NavBar height */
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