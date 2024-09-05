<template>
  <div class="index-container">
    <div class="sidebar">
      <template v-if="isLoggedIn">
        <p>Welcome, {{ userInfo.username }}</p>
        <p>Nickname: {{ userInfo.nickname }}</p>
        <p>Create Time: {{ userInfo.createTime }}</p>
        <p>Coin: {{ userInfo.coin }}</p>
        <p>Point: {{ userInfo.point }}</p>
      </template>
      <template v-else>
        <p>Please log in</p>
      </template>
    </div>
    <InScreenWindow ref="window" v-if="showWindow" @close="showWindow = false" />
    <button v-if="isLoggedIn" @click="showWindowAndReset" class="floating-button">
      <font-awesome-icon :icon="['far', 'calendar-check']" />
    </button>
  </div>
</template>

<script>
import { computed } from 'vue';
import { useStore } from 'vuex';
import InScreenWindow from './InScreenWindow.vue';

export default {
  components: {
    InScreenWindow,
  },
  setup() {
    const store = useStore();
    const userInfo = computed(() => store.state.userInfo);
    const isLoggedIn = computed(() => store.state.isLoggedIn);
    return {
      userInfo,
      isLoggedIn,
    };
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
  display: flex;
  background-image: url('@/assets/background.png'); /* Ensure the image is placed in the src/assets directory */
  background-size: cover;
  width: 100%;
  height: calc(100vh - 39px); /* Adjust 39px to the actual height of your NavBar */
  position: relative;
  overflow: hidden; /* Hide any overflow content */
}

.sidebar {
  width: 200px;
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
  padding: 20px;
}

.main-content {
  flex-grow: 1;
  padding: 20px;
}

.floating-button {
  position: absolute;
  right: 20%;
  top: 20%;
  background-color: transparent;
  border: none;
  cursor: pointer;
  font-size: 24px;
  color: white;
  width: 20px;
  height: 20px;
}

.floating-button:hover {
  color: #f40;
}
</style>