<template>
  <div class="index-container">
    <InScreenWindow ref="window" v-if="showWindow" @close="showWindow = false" />
    <button @click="showWindowAndReset">Show Window</button>
  </div>
</template>

<script>
import InScreenWindow from './InScreenWindow.vue';

export default {
  components: {
    InScreenWindow,
  },
  data() {
    return {
      showWindow: false,
    };
  },
  methods: {
    showWindowAndReset() {
      this.showWindow = true;
      this.$nextTick(() => {
        this.$refs.window.resetPosition();
      });
    },
  },
};
</script>

<style>
.index-container {
  background-image: url('@/assets/background.png'); /* Ensure the image is placed in the src/assets directory */
  background-size: cover;
  width: 100%;
  height: calc(100vh - 39px); /* Adjust 39px to the actual height of your NavBar */
  position: relative;
  overflow: hidden; /* Hide any overflow content */
}
</style>