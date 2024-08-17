<template>
  <div>
    <h1>Sign Up</h1>
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
    <button @click="signup">Sign up</button>
    <div v-if="errorMessage" class="error-box">{{ errorMessage }}</div>
  </div>
</template>

<script>
import { signup, fetchCaptcha } from '@/services/userService';

export default {
  name: 'Signup',
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
    async signup() {
      try {
        const response = await signup({
          username: this.username,
          password: this.password,
          pic_token: this.pic_token,
          verify: this.captcha
        });
        const code = response.data.code;
        if (code === 3030) {
          this.errorMessage = '';
          localStorage.setItem('token', response.data.data.token); // Store the token
          this.$router.push('/'); // Navigate to home page
        } else {
          this.handleError(code);
        }
      } catch (error) {
        this.errorMessage = 'Other error, please try again.';
        console.error('Signup failed', error);
      }
    },
    handleError(code) {
      switch (code) {
        case 3031:
          this.errorMessage = 'Captcha error.';
          break;
        case 3032:
          this.errorMessage = 'Username already exists.';
          break;
        case 3033:
          this.errorMessage = 'Username does not meet requirements.';
          break;
        case 3034:
          this.errorMessage = 'Password does not meet requirements.';
          break;
        default:
          this.errorMessage = 'Other error, please try again.';
      }
    }
  }
}
</script>

<style>
.error-box {
  color: red;
  margin-top: 10px;
}
</style>