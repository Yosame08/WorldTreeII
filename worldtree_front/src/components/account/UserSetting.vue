<template>
  <div>
    <el-form :model="form" ref="form" label-width="120px">
      <el-form-item label="Avatar">
        <el-upload
            class="avatar-uploader"
            action="https://jsonplaceholder.typicode.com/posts/"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload">
          <img v-if="form.avatar" :src="form.avatar" class="avatar" alt="usr-avatar"/>
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
      </el-form-item>
      <el-form-item label="Signature">
        <quill-editor v-model="form.signature" :options="editorOptions" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm">Save</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { ElForm, ElFormItem, ElUpload, ElButton } from 'element-plus';

export default {
  name: 'UserSettings',
  components: {
    ElForm,
    ElFormItem,
    ElUpload,
    ElButton,
  },
  data() {
    return {
      form: {
        avatar: '',
        signature: ''
      },
      editorOptions: {
        // Quill editor options
      }
    };
  },
  methods: {
    handleAvatarSuccess(response, file) {
      this.form.avatar = URL.createObjectURL(file.raw);
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg'; // I hope PNG is also OK
      const isLt2M = file.size / 1024 / 1024 < 2; // I hope side length of the resolution is greater than 100 and less than 800

      if (!isJPG) {
        this.$message.error('Avatar picture must be JPG format!');
      }
      if (!isLt2M) {
        this.$message.error('Avatar picture size can not exceed 2MB!');
      }
      return isJPG && isLt2M;
    },
    submitForm() { // the function of submitting posts the information to server /api/user/. JSON: { "token": ..., "avatar": ...(base64), "": ... }
      this.$refs.form.validate((valid) => {
        if (valid) {
          // Handle form submission
          console.log('Form submitted:', this.form);
        } else {
          console.log('Form validation failed'); // If submitting failure happens, show the ErrorMsg component, just like login and signup.
          // If failure is caused by expired token or the user hasn't logged in, jump to the login page directly.
          return false;
        }
      });
    }
  }
};
</script>

<style>
.avatar-uploader {
  display: inline-block;
  width: 100px;
  height: 100px;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  line-height: 100px;
  text-align: center;
}

.avatar {
  width: 100%;
  height: 100%;
  display: block;
}
</style>