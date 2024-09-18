<!-- src/components/NavBar.vue -->
<template>
  <el-progress
      v-if="this.$store.state.load > 0"
      :percentage="50"
      :indeterminate="true"
      :show-text="false"
      :color="customColors"
      :duration="2.5"
      :stroke-width="3"
      class="progress-bar"
  />
  <nav v-if="showNavBar">
    <div id="nav">
      <router-link to="/" class="nav-link" active-class="nav-link-active">Home</router-link>
      <router-link to="/map" class="nav-link" active-class="nav-link-active">Map</router-link>
      <router-link to="/rank" class="nav-link" active-class="nav-link-active">Rank</router-link>
      <router-link to="/bbs" class="nav-link" active-class="nav-link-active">BBS</router-link>
      <div class="auth-section">
        <template v-if="!isLoggedIn">
          <router-link to="/login" class="nav-link" active-class="nav-link-active">Log in</router-link>
          <router-link to="/signup" class="nav-link" active-class="nav-link-active">Sign up</router-link>
        </template>
        <template v-else>
          <button @click="goToUserSetting" class="nav-link">{{ username }}</button>
          <button @click="logout" class="nav-link">Log out</button>
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

.progress-bar {
  position: absolute;
  top: 0;
}
</style>