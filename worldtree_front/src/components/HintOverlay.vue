<template>
  <div class="overlay" v-if="visible">
    <div class="content">
      <img :src="imageBase64" alt="Hint Image" />
      <div class="buttons">
        <el-button type="primary" :icon="Download" @click="downloadImage" circle />
        <el-button type="success" :icon="Check" @click="closeOverlay" circle />
      </div>
    </div>
  </div>
</template>

<script>
import {Check, Download} from "@element-plus/icons-vue";

export default {
  name: 'HintOverlay',
  computed: {
    Download() {
      return Download
    },
    Check() {
      return Check
    }
  },
  props: {
    imageBase64: {
      type: String,
      required: true
    },
    visible: {
      type: Boolean,
      default: false
    }
  },
  methods: {
    downloadImage() {
      const link = document.createElement('a');
      link.href = this.imageBase64;
      link.download = 'hint_image.png';
      link.click();
    },
    closeOverlay() {
      this.$emit('close');
    }
  }
};
</script>

<style scoped>
.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.8);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.content {
  text-align: center;
  background: white;
  padding: 20px;
  border-radius: 10px;
}

img {
  max-width: 100%;
  max-height: 80vh;
}

.buttons {
  margin-top: 20px;
}

button {
  margin: 0 10px;
  padding: 10px 20px;
  font-size: 16px;
  cursor: pointer;
}
</style>