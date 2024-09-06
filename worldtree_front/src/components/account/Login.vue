<!-- src/components/account/Login.vue -->
<template>
  <div>
    <ErrorMsg :message="errorMessage" />
    <h1>Log In</h1>
    <div>
      <label for="username">Username:</label>
      <input type="text" id="username" v-model="username" />
    </div>
    <div>
      <label for="password">Password:</label>
      <input type="password" id="password" v-model="password" />
    </div>
    <div>
      <img :src="captchaImage" alt="Captcha" @click="loadCaptcha" />
      <input type="text" v-model="captcha" placeholder="Enter captcha" />
    </div>
    <button @click="login">Log in</button>
  </div>
</template>

<script>
import { login, fetchCaptcha } from '@/services/userService';
import store from '@/services/storeService';
import ErrorMsg from '@/components/ErrorMsg.vue';
import { useRouter } from 'vue-router';

export default {
  name: 'Login',
  components: {
    ErrorMsg
  },
  data() {
    return {
      username: '',
      password: '',
      captcha: '',
      pic_token: '', // Token for the captcha image
      captchaImage: '', // Base64 image data
      errorMessage: ''
    };
  },
  created() {
    this.loadCaptcha();
  },
  methods: {
    async loadCaptcha() {
      try {
        const response = await fetchCaptcha();
        this.pic_token = response.data.pic_token;
        this.captchaImage = response.data.pic;
      } catch (error) {
        console.error('Failed to load captcha', error);
      }
    },
    async login() {
      try {
        const response = await login({
          username: this.username,
          password: this.password,
          pic_token: this.pic_token,
          verify: this.captcha
        });
        const code = response.data.code;
        console.log(code);
        if (code === 3040) {
          this.errorMessage = '';
          sessionStorage.setItem('token', response.data.data); // Store the token
          store.commit('setLoginState', true);
          await store.dispatch('fetchUserInfo');
          this.$router.push('/'); // Navigate to home page
        } else {
          this.errorMessage = response.data.message;
        }
      } catch (error) {
        this.errorMessage = 'Login failed, please try again.';
        console.error('Login failed', error);
      }
    },
  }
}
</script>

<style>

</style>