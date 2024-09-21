<template>
  <div class="window" :style="style" @mousedown="onMouseDown">
    <div class="header" @mousedown.stop="initDrag">
      <h4>{{ title }}</h4>
      <button @click="$emit('close')">Close</button>
    </div>
    <div class="content">
      <slot></slot>
    </div>
    <div class="resizer" @mousedown="initResize"></div>
  </div>
</template>

<script>
export default {
  props: {
    title: {
      type: String,
      default: 'Window Title'
    }
  },
  data() {
    return {
      style: {
        width: '500px',
        height: '500px',
        top: '100px',
        left: '300px',
      },
      isDragging: false,
      isResizing: false,
      dragStartX: 0,
      dragStartY: 0,
    };
  },
  methods: {
    resetPosition() {
      this.style.top = '100px';
      this.style.left = '100px';
    },
    initDrag(event) {
      this.isDragging = true;
      this.dragStartX = event.clientX - this.$el.getBoundingClientRect().left;
      this.dragStartY = event.clientY - this.$el.getBoundingClientRect().top;
      window.addEventListener('mousemove', this.drag);
      window.addEventListener('mouseup', this.stopDrag);
    },
    drag(event) {
      if (this.isDragging) {
        this.style.left = `${event.clientX - this.dragStartX}px`;
        this.style.top = `${event.clientY - this.dragStartY}px`;
      }
    },
    stopDrag() {
      this.isDragging = false;
      window.removeEventListener('mousemove', this.drag);
      window.removeEventListener('mouseup', this.stopDrag);
    },
    initResize(event) {
      this.isResizing = true;
      window.addEventListener('mousemove', this.resize);
      window.addEventListener('mouseup', this.stopResize);
    },
    resize(event) {
      if (this.isResizing) {
        this.style.width = `${event.clientX - this.$el.getBoundingClientRect().left}px`;
        this.style.height = `${event.clientY - this.$el.getBoundingClientRect().top}px`;
      }
    },
    stopResize() {
      this.isResizing = false;
      window.removeEventListener('mousemove', this.resize);
      window.removeEventListener('mouseup', this.stopResize);
    },
  },
};
</script>

<style scoped>
.window {
  position: absolute;
  border: 1px solid #ccc;
  background: #fff;
  z-index: 1000;
  resize: both;
  overflow: auto;
}
.header {
  background: #f1f1f1;
  cursor: move;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-left: 20px;
  padding-right: 20px;
  margin: 0;
}
.content {
  padding: 10px;
}
.resizer {
  width: 10px;
  height: 10px;
  background: #ccc;
  position: absolute;
  right: 0;
  bottom: 0;
  cursor: se-resize;
}
.header h4 {
  margin-top: 10px; /* 调整这个值来控制上边距 */
  margin-bottom: 10px; /* 调整这个值来控制下边距 */
}
</style>