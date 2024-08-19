<template>
  <h1>This is BBS.</h1>
  <el-table
      :data="list"
      style="width: 100%"
      :row-class-name="tableRowClassName"
  >
    <el-table-column prop="user" label="用户名" width="180" />
    <el-table-column prop="title" label="标题" width="180" />
    <el-table-column prop="point" label="悬赏积分" />
  </el-table>
</template>

<script>
import { fetchBbsTitles } from "@/services/infoService";

export default ({
  name: 'BBS',
  data() {
    return {
      list: [],
      token: "placeholder",
    };
  },
  created() {
    this.loadBbsData();
  },
  methods: {
    async loadBbsData() {
      try {
        const response = await fetchBbsTitles(this.token);
        if (response.data.code === 3050) {
          this.list = response.data.list;
        } else {
          this.$router.push('/login');
        }
      } catch (error) {
        console.error('Failed to fetch rank data', error);
        this.$router.push('/login');
      }
    },
    tableRowClassName({row}) {
      if (row.special !== 0) {
        return 'special-row';
      }
      return '';
    },
  }
});
</script>

<style>
.el-table .special-row {
  --el-table-tr-bg-color: #cd7f32;
}
</style>