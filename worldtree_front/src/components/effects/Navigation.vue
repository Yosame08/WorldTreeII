<!-- src/components/effects/Navigation.vue -->
<script setup>
import { defineProps } from 'vue';

const props = defineProps({
  icon: {
    type: Array,
    required: true
  },
  description: {
    type: String,
    required: true
  }
});
</script>

<template>
  <div class="nav-hex">
    <div class="hexagon">
      <font-awesome-icon :icon="icon" class="fa-icon" />
      <p>{{ description }}</p>
    </div>
  </div>
</template>

<style scoped>
.nav-hex {
  display: flex;
  justify-content: center;
  align-items: center;
  //margin: 10px 0;
}

.hexagon {
  width: 118px;
  height: 132px;
  background-image: url('@/assets/hex.png');
  background-size: cover;
  position: relative;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: white;
  text-align: center;
}

.hexagon .fa-icon {
  margin-top: 20px; /* 调整这个值来控制图标向下移动的距离 */
}
</style>