<!-- src/components/ErrorMsg.vue -->
<template>
  <div v-if="message" class="error-msg">
    <span class="error-icon">❌</span>
    <span class="error-text">{{ message }}</span>
  </div>
</template>

<script>
export default {
  name: 'ErrorMsg',
  props: {
    message: {
      type: String,
      required: true
    }
  }
};
</script>

<style>
.error-msg {
  display: flex;
  align-items: center;
  background-color: #f8d7da;
  color: #721c24;
  padding: 10px;
  border: 1px solid #f5c6cb;
  border-radius: 5px;
  margin-bottom: 10px;
  width: 40%;
  margin-left: 30%;
}

.error-icon {
  margin-right: 10px;
}

.error-text {
  flex: 1;
}
</style>