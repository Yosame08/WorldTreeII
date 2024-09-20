<template>
  <ErrorMsg :message="errorMessage" />
  <el-table
      :data="list"
      style="width: 100%"
      :row-class-name="tableRowClassName"
      @row-click="goToDetail"
  >
    <el-table-column prop="user" label="发件人" width="180" />
    <el-table-column prop="title" label="邮件标题" />
  </el-table>
</template>

<script>
import {universalGet} from "@/services/universalService";
import store from "@/services/storeService";

export default ({
  name: 'Mail',
  data() {
    return {
      list: [],
    };
  },
  created() {
    this.loadBbsData();
  },
  methods: {
    async loadBbsData() {
      try {
        const response = await universalGet('/api/mail');
        if (response.data.code === 0) {
          store.commit("clearErrorMsg");
          this.list = response.data.list;
        }
      } catch (error) {
        console.error('Failed to fetch BBS data', error);
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