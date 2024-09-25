<template>
  <div class="upload-form">
    <el-form @submit.prevent="handleSubmit">
      <el-form-item label="Username">
        <el-input v-model="form.username" placeholder="Enter username"></el-input>
      </el-form-item>
      <el-form-item label="Task ID">
        <el-select v-model="form.taskId" placeholder="Select task">
          <el-option
              v-for="task in tasks"
              :key="task.id"
              :label="task.name"
              :value="task.id"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" native-type="submit">Submit</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import {universalPost} from "@/services/universalService";
import store from "@/services/storeService";
import {computed} from "vue";

export default {
  data() {
    return {
      form: {
        username: '',
        taskId: null,
        admin: computed(() => store.state.userInfo.username)
      },
      tasks: [
        { id: 11, name: '甜甜圈' },
        { id: 12, name: '扑克牌' },
        // Add more tasks as needed
      ]
    };
  },
  methods: {
    async handleSubmit() {
      try {
        const response = await universalPost('/api/100groups/up10ad/ch3ck', this.form);
        if (response.data.code === 0) {
          this.$message.success('Submission successful');
        } else {
          this.$message.error(response.data.message);
        }
      } catch (error) {
        this.$message.error('Submission failed');
      }
    }
  }
};
</script>

<style scoped>
.upload-form {
  max-width: 400px;
  margin: 0 auto;
}
</style>