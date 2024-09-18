<template>
  <div class="index-container">
    <div :class="['sidebar', { 'sidebar-hidden': isSidebarHidden }]">
      <template v-if="isLoggedIn">
        <h2>欢迎回来,</h2>
        <h2>{{ userInfo.nickname ? userInfo.nickname : userInfo.username }}</h2>
        <br>
        <p>注册时间：{{ moment(userInfo.createTime).format('YYYY-MM-DD HH:mm:ss') }}</p>
        <p>积分：{{ userInfo.point }}</p>
        <p>货币：{{ userInfo.coin }}</p>
      </template>
      <template v-else>
        <h1>请先登录</h1>
      </template>
      <button class="toggle-button" @click="toggleSidebar">
        <font-awesome-icon :icon="isSidebarHidden ? 'arrow-right' : 'arrow-left'" />
      </button>
    </div>
    <InScreenWindow ref="window" v-if="showWindow" @close="showWindow = false" />
    <button v-if="isLoggedIn" @click="showWindowAndReset" class="floating-button">
      <font-awesome-icon :icon="['far', 'calendar-check']" />
    </button>
  </div>
</template>

<script>
import { computed, onMounted, ref } from 'vue';
import { useStore } from 'vuex';
import InScreenWindow from './InScreenWindow.vue';
import moment from "moment";

export default {
  components: {
    InScreenWindow,
  },
  setup() {
    const store = useStore();
    const userInfo = computed(() => store.state.userInfo);
    const isLoggedIn = computed(() => store.state.isLoggedIn);
    const showWindow = ref(false);
    const isSidebarHidden = ref(false);
    let timerId = null;

    const showWindowAndReset = () => {
      showWindow.value = true;
      this.$nextTick(() => {
        this.$refs.window.resetPosition();
      });
    };

    const toggleSidebar = () => {
      isSidebarHidden.value = !isSidebarHidden.value;
      if (timerId) {
        clearTimeout(timerId);
        timerId = null;
      }
    };

    onMounted(async () => {
      await store.dispatch('fetchUserInfo');
      timerId = setTimeout(() => {
        isSidebarHidden.value = true;
      }, 5000);
    });

    return {
      userInfo,
      isLoggedIn,
      showWindow,
      isSidebarHidden,
      showWindowAndReset,
      toggleSidebar,
      moment
    };
  }
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
  transition: transform 0.3s ease;
}

.sidebar-hidden {
  transform: translateX(-100%); /* Adjust this value to control how much the sidebar hides */
}

.toggle-button {
  position: absolute;
  top: 0px;
  left: 240px; /* Adjust this value to control the button's position */
  background-color: rgba(0, 0, 0, 0.5);
  border: none;
  cursor: pointer;
  color: white;
  padding: 5px;
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