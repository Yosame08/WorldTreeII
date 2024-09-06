<template>
  <div class="window" :style="style" @mousedown="startDrag">
    <div class="window-header">
      <span>Window Title</span>
      <button @click="$emit('close')">X</button>
    </div>
    <div class="window-content">
      <!-- Content from backend will be displayed here -->
      <p>{{ content }}</p>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      isDragging: false,
      offsetX: 0,
      offsetY: 0,
      style: {
        top: `calc(50vh - 200px)`, // Assuming the window height is 400px
        left: `calc(50vw - 200px)`, // Assuming the window width is 400px
      },
      content: 'This is the content from the backend.',
    };
  },
  methods: {
    startDrag(event) {
      this.isDragging = true;
      this.offsetX = event.clientX - this.$el.offsetLeft;
      this.offsetY = event.clientY - this.$el.offsetTop;
      document.addEventListener('mousemove', this.onDrag);
      document.addEventListener('mouseup', this.stopDrag);
    },
    onDrag(event) {
      if (this.isDragging) {
        const newTop = event.clientY - this.offsetY;
        const newLeft = event.clientX - this.offsetX;
        const containerRect = this.$el.parentElement.getBoundingClientRect();
        const windowRect = this.$el.getBoundingClientRect();

        // Ensure the window does not go beyond the top of the container
        if (newTop < 0) {
          this.style.top = '0px';
        } else if (newTop + windowRect.height > containerRect.height) {
          // Ensure the window does not go beyond the bottom of the container
          this.style.top = `${containerRect.height - windowRect.height}px`;
        } else {
          this.style.top = `${newTop}px`;
        }

        // Ensure the window does not go beyond the left of the container
        if (newLeft < 0) {
          this.style.left = '0px';
        } else if (newLeft + windowRect.width > containerRect.width) {
          // Ensure the window does not go beyond the right of the container
          this.style.left = `${containerRect.width - windowRect.width}px`;
        } else {
          this.style.left = `${newLeft}px`;
        }
      }
    },
    stopDrag() {
      this.isDragging = false;
      document.removeEventListener('mousemove', this.onDrag);
      document.removeEventListener('mouseup', this.stopDrag);
    },
    resetPosition() {
      this.style.top = `calc(50vh - ${this.$el.offsetHeight / 2}px)`;
      this.style.left = `calc(50vw - ${this.$el.offsetWidth / 2}px)`;
    },
  },
};
</script>

<style>
.window {
  position: absolute;
  width: 400px;
  height: 400px;
  background-color: white;
  border: 1px solid #ccc;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}
.window-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #f1f1f1;
  padding: 5px;
  cursor: move;
}
.window-content {
  padding: 10px;
}
</style>