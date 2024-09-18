<template>
  <ErrorMsg :message="errorMessage" />
  <h1>This is BBS.</h1>
  <el-table
      :data="list"
      style="width: 100%"
      :row-class-name="tableRowClassName"
      @row-click="goToDetail"
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
      errorMessage: ''
    };
  },
  created() {
    this.loadBbsData();
  },
  methods: {
    async loadBbsData() {
      try {
        const response = await fetchBbsTitles();
        if (response.data.code === 3050) {
          this.list = response.data.list;
        } else {
          this.errorMessage = response.data.message;
        }
      } catch (error) {
        console.error('Failed to fetch BBS data', error);
        this.errorMessage = 'Failed to fetch BBS data';
      }
    },
    tableRowClassName({row}) {
      if (row.special !== 0) {
        return 'special-row';
      }
      return '';
    },
    goToDetail(row) {
      this.$router.push(`/bbs/${row.id}`);
    },
  }
});
</script>

<style>
.el-table .special-row {
  --el-table-tr-bg-color: #ffeba8;
}
</style>