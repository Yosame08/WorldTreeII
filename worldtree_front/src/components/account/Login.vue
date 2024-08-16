<!-- src/components/account/Login.vue -->
<template>
  <div>
    <h1>Log In</h1>
    <button @click="login">Log in</button>
  </div>
</template>

<script>
import { login } from '@/services/userService';

export default {
  name: 'Login',
  methods: {
    async login() {
      try {
        const response = await login({ username: 'test', password: 'password' });
        this.$emit('login-success', response.data.data.username);
      } catch (error) {
        console.error('Login failed', error);
      }
    }
  }
}
</script>