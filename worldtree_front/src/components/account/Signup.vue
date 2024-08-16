<!-- src/components/account/Signup.vue -->
<template>
  <div>
    <h1>Sign Up</h1>
    <button @click="signup">Sign up</button>
  </div>
</template>

<script>
import { signup } from '@/services/userService';

export default {
  name: 'Signup',
  methods: {
    async signup() {
      try {
        const response = await signup({ username: 'newuser', password: 'password' });
        this.$emit('signup-success', response.data.data.username);
      } catch (error) {
        console.error('Signup failed', error);
      }
    }
  }
}
</script>