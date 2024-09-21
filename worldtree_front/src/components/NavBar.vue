<!-- src/components/NavBar.vue -->
<template>
  <el-progress
      v-if="this.$store.state.load > 0"
      :percentage="50"
      :indeterminate="true"
      :show-text="false"
      :color="customColors"
      :duration="2.5"
      :stroke-width="4"
      class="progress-bar"
  />
  <nav v-if="showNavBar">
    <div id="nav">
      <div v-if="errorMessage" :class="{'error-msg': isError, 'success-msg': !isError}">
        {{ isError ? '❌' : '✔'}} {{ errorMessage }}
      </div>
      <div class="auth-section">
        <template v-if="!isLoggedIn">
          <router-link to="/login" class="nav-link" active-class="nav-link-active">登录</router-link>
          <router-link to="/signup" class="nav-link" active-class="nav-link-active">注册</router-link>
        </template>
        <template v-else>
          <button @click="goToUserSetting" class="nav-link">用户：{{ username }}</button>
          <button @click="logout" class="nav-link">登出</button>
        </template>
      </div>
    </div>
  </nav>
</template>

<script>
import { computed } from 'vue';
import store from "@/services/storeService";

const customColors = [
  { color: '#f40'},
]

export default {
  name: 'NavBar',
  setup() {
    const showNavBar = computed(() => store.state.showNavBar);
    return { showNavBar };
  },
  data() {
    return {
      isLoggedIn: computed(() => store.state.isLoggedIn),
      username: computed(() => store.state.userInfo.username),
      customColors: customColors,
      errorMessage: computed(() => store.state.showMsg),
      isError: computed(() => store.state.isError),
    };
  },
  created() {
    this.$router.beforeEach((to, from, next) => {
      this.$store.commit('load');
      next();
    });
    this.$router.afterEach(() => {
      this.$store.commit('finish');
    });
  },
  methods: {
    logout() {
      store.commit('setUserInfo', {});
      sessionStorage.removeItem('token');
      store.commit('setLoggedIn', false);
      this.$router.push('/beginning');
    },
    goToUserSetting() {
      this.$router.push('/usersetting');
    },
    setError(message) {
      this.errorMessage = message;
    }
  }
}
</script>

<style>
#nav {
  background-color: #1a1a1a;
  margin: 0;
  display: flex;
  justify-content: space-between;
}

.nav-links {
  display: flex;
}

.nav-link {
  text-decoration: none;
  color: white;
  padding: 10px 15px;
  background-color: #333;
  transition: background-color 0.3s;
}

.nav-link:hover:not(.nav-link-active) {
  background-color: #555;
}

.nav-link-active:hover {
  background-color: #d30; /* Darker orange color */
}

.nav-link-active {
  background-color: #f40;
  font-weight: bold;
}

.auth-section {
  display: flex;
  align-items: center;
  margin-left: auto;
}

.auth-section button {
  text-decoration: none;
  color: white;
  padding: 10px 15px;
  background-color: #333;
  border: none;
  cursor: pointer;
  transition: background-color 0.3s;
  font-size: 16px;
}

.auth-section button:hover {
  background-color: #555;
}

.progress-container {
  position: relative;
}

.progress-bar {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 1000;
  height: 0;
}

.error-msg {
  display: flex;
  align-items: center;
  background-color: #f8d7da;
  color: #721c24;
  border: 1px solid #f5c6cb;
  border-radius: 3px;
  padding-left: 30px;
  padding-right: 35px;
}

.success-msg {
  display: flex;
  align-items: center;
  background-color: #d4edda;
  color: #155724;
  border: 1px solid #c3e6cb;
  border-radius: 3px;
  padding-left: 30px;
  padding-right: 35px;
}
</style>