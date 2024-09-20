<!-- src/components/account/Signup.vue -->
<template>
  <div>
    <h1 class="form-title">欢迎来到穿越时空的旅程</h1>
    <el-form :model="form" ref="form" label-width="80px" class="login-form">
      <el-form-item label="用户名">
        <el-input v-model="form.username" />
      </el-form-item>
      <el-form-item label="密码">
        <el-input type="password" v-model="form.password" />
      </el-form-item>
      <el-form-item label="确认密码">
        <el-input type="password" v-model="form.confirmPassword" />
      </el-form-item>
      <el-form-item label="验证码">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-input v-model="form.captcha" placeholder="输入验证码" />
          </el-col>
          <el-col :span="12">
            <img :src="captchaImage" alt="Captcha" @click="loadCaptcha" />
          </el-col>
        </el-row>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="signup">加入！</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { signup, fetchCaptcha } from '@/services/userService';
import store from "@/services/storeService";

export default {
  name: 'Signup',
  data() {
    return {
      form: {
        username: '',
        password: '',
        confirmPassword: '',
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
    async signup() {
      if (this.form.password !== this.form.confirmPassword) {
        store.commit("setErrorMsg", '密码和确认密码不一致');
        return;
      }
      try{
        const response = await signup({
          username: this.form.username,
          password: this.form.password,
          pic_token: this.pic_token,
          verify: this.form.captcha
        });
        if (response.data.code === 0) {
          store.commit("clearErrorMsg");
          this.$router.push('/login'); // Navigate to login page
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