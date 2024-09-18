<!-- src/components/Beginning.vue -->
<template>
  <div class="not-logged-in">
    <Typewriter text="Welcome to Our Site" :speed="100" />
    <div class="buttons">
      <button @click="goToLogin">登录</button>
      <button @click="goToSignUp">注册</button>
    </div>
  </div>
</template>

<script>
import store from "@/services/storeService";
import Typewriter from "@/components/effects/Typewriter.vue";

export default {
  components: {
    Typewriter
  },
  mounted() {
    if (store.state.isLoggedIn) {
      this.$router.push('/');
    }
    store.commit('setShowNavBar', false);
  },
  methods: {
    goToLogin() {
      this.$router.push('/login');
    },
    goToSignUp() {
      this.$router.push('/signup');
    }
  },
  beforeUnmount() {
    store.commit('setShowNavBar', true);
  },
};
</script>

<style>
.not-logged-in {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  text-align: center;
}

.not-logged-in h1 {
  font-size: 2em;
  margin-bottom: 20px;
}

.not-logged-in .buttons {
  display: flex;
  gap: 10px;
}

.not-logged-in button {
  padding: 10px 20px;
  font-size: 1em;
  cursor: pointer;
}
</style>