<template>
  <div id="nav">
    <router-link to="/" class="nav-link" active-class="nav-link-active">Home</router-link>
    <router-link to="/rank" class="nav-link" active-class="nav-link-active">Rank</router-link>
    <router-link to="/bbs" class="nav-link" active-class="nav-link-active">BBS</router-link>
  </div>
  <router-view></router-view>
</template>

<script>
export default {
  name: 'App',
  components: {}
}
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
}

#nav {
  background-color: #1a1a1a;
  padding: 10px;
  margin: 0; /* Ensure no margin around the nav bar */
}

.nav-link {
  text-decoration: none;
  color: white;
  padding: 10px 20px;
  margin: 0px 0;
  background-color: #333;
  transition: background-color 0.3s;
}

.nav-link:hover {
  background-color: #555;
}

.nav-link-active {
  background-color: #f40;
  font-weight: bold;
}
</style>