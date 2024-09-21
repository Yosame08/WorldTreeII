<!-- src/App.vue -->
<template>
  <NavBar />
  <router-view></router-view>
</template>

<script setup>
import NavBar from './components/NavBar.vue';
import store from "@/services/storeService";
import {onMounted} from "vue";

onMounted(() => {
  if (JSON.stringify(store.state.userInfo) === '{}') {
    store.dispatch('fetchUserInfo');
  }
});
</script>

<style>
html, body {
  margin: 0;
  padding: 0;
}

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin: 0;
}
</style>