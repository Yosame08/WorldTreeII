<template>
  <div>
    <h1 class="form-title">欢迎再次回到穿越时空的旅程</h1>
    <el-form :model="form" ref="form" label-width="80px" class="login-form">
      <el-form-item label="用户名">
        <el-input v-model="form.username" />
      </el-form-item>
      <el-form-item label="密码">
        <el-input type="password" v-model="form.password" />
      </el-form-item>
<!--      <el-form-item label="验证码">-->
<!--        <el-row :gutter="20">-->
<!--          <el-col :span="12">-->
<!--            <el-input v-model="form.captcha" placeholder="输入验证码" />-->
<!--          </el-col>-->
<!--          <el-col :span="12">-->
<!--            <img :src="captchaImage" alt="Captcha" @click="loadCaptcha" />-->
<!--          </el-col>-->
<!--        </el-row>-->
<!--      </el-form-item>-->
      <el-form-item>
        <el-button type="primary" @click="login">启动！</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { login, fetchCaptcha } from '@/services/userService';
import store from '@/services/storeService';

export default {
  name: 'Login',
  data() {
    return {
      form: {
        username: '',
        password: '',
        captcha: ''
      },
      pic_token: '', // Token for the captcha image
      captchaImage: '', // Base64 image data
    };
  },
  created() {
    // this.loadCaptcha();
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
      try{
        const response = await login({
          username: this.form.username,
          password: this.form.password,
          pic_token: this.pic_token,
          verify: this.form.captcha
        });
        const code = response.data.code;
        if (code === 0) {
          sessionStorage.setItem('token', response.data.data); // Store the token
          store.commit('setLoggedIn', true);
          this.$router.push('/'); // Navigate to home page
        }
      }
      catch (error) {}
    },
  }
}
</script>

<style>
.form-title {
  width: 500px; /* Set the desired width */
  margin: 32px auto; /* Center the title */
}

.login-form {
  width: 350px; /* Match the width of the title */
  margin: 0 auto; /* Center the form */
}
</style>