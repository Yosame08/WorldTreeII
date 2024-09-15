<template>
  <div>
    <ErrorMsg :message="errorMessage" />
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
  </div>
</template>

<script>
import { signup, fetchCaptcha } from '@/services/userService';
import ErrorMsg from "@/components/ErrorMsg.vue";

export default {
  name: 'Signup',
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
    async signup() {
      try {
        const response = await signup({
          username: this.username,
          password: this.password,
          pic_token: this.pic_token,
          verify: this.captcha
        });
        const code = response.data.code;
        if (code === 0) {
          this.errorMessage = '';
          this.$router.push('/login'); // Navigate to login page
        } else {
          this.errorMessage = response.data.message;
        }
      } catch (error) {
        this.errorMessage = 'Other error, please try again.';
        console.error('Signup failed', error);
      }
    },
  }
}
</script>

<style>

</style>